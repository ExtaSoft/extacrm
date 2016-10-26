package ru.extas.web.commons;

import com.google.common.io.ByteStreams;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;
import ru.extas.model.common.OwnedFileContainer;
import ru.extas.web.commons.component.FileUploader;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 15.09.2014
 *         Time: 23:51
 */
public class FilesManageField<TFileContainer extends OwnedFileContainer> extends CustomField<List> {

    private final Class<TFileContainer> containerClass;
    private ExtaBeanContainer<TFileContainer> container;
    private ProgressBar progress;

    private Mode mode;
    private ItemGrid filesContainer;

    public FilesManageField(final Class<TFileContainer> containerClass) {
        this.containerClass = containerClass;
        setBuffered(true);
        setRequiredError("Раздел файлов должен содержать хотябы один файл!");
    }


    @Override
    protected Component initContent() {
        final List<TFileContainer> list = getValue() != null ? getValue() : newArrayList();
        container = new ExtaBeanContainer<>(containerClass);
        if (list != null) {
            container.addAll(list);
        }

        final VerticalLayout root = new VerticalLayout();
        root.setWidth(100, Unit.PERCENTAGE);
        root.addStyleName("drop-area");

        filesContainer = new ItemGrid();
        filesContainer.setWidth(100, Unit.PERCENTAGE);
        filesContainer.setContainerDataSource(container);
        filesContainer.setItemGenerator(new ItemGenerator() {
            @Override
            public Component generateItem(final AbstractItemLayout pSource, final Object pItemId) {
                return getItemComponent(pSource, (TFileContainer) pItemId);
            }

            @Override
            public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId, final Object pPropertyChanged) {
                return true;
            }
        });

        root.addComponent(filesContainer);
        setMode(Mode.LIST);

        final Label infoLabel = new Label("Перетащите файлы для загрузки сюда, или нажмите кнопку \"Добавить файлы...\"");
        //infoLabel.addStyleName(ExtaTheme.WRLABEL_LIGHT);
        infoLabel.setWidth(100, Unit.PERCENTAGE);
        root.addComponent(infoLabel);

        progress = new ProgressBar();
        progress.setWidth(100, Unit.PERCENTAGE);
        progress.setIndeterminate(true);
        progress.setVisible(false);
        root.addComponent(progress);

        final UploadFinishedHandler handler = (inputStream, filename, mimeType, length) -> {
            try {
                addUploadedFile(ByteStreams.toByteArray(inputStream), filename, mimeType, length);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        };
        final FileUploader fileUploader = new FileUploader("Добавить файлы...", handler);
        fileUploader.setWidth(100, Unit.PERCENTAGE);
        root.addComponent(fileUploader);

        final FileDropBox dropBox = new FileDropBox(root);

        addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
            infoLabel.setVisible(!isRedOnly);
//            progress.setVisible(!isRedOnly);
            fileUploader.setVisible(!isRedOnly);
            filesContainer.setReadOnly(isRedOnly);
            dropBox.setReadOnly(isRedOnly);
        });

        return dropBox;
    }

    private Component getItemComponent(final AbstractItemLayout pSource, final TFileContainer item) {
        final ComponentContainer root;
        if (mode == Mode.LIST) {
            final HorizontalLayout layout = new HorizontalLayout();
            layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            //layout.addStyleName(ExtaTheme.LAYOUT_COMPONENT_GROUP);
            root = layout;
        } else {
            final VerticalLayout layout = new VerticalLayout();
            layout.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
            root = layout;
        }

        final Button dwnBtn = new FileDownloadButton(item);
        root.addComponent(dwnBtn);

        final Button delBtn = new Button("Удалить", Fontello.TRASH_4);
        delBtn.setDescription("Удалить файл");
        delBtn.addStyleName(ExtaTheme.BUTTON_QUIET);
        delBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        delBtn.setData(item);
        delBtn.addClickListener(this::deleteClickListener);
        root.addComponent(delBtn);

        if (mode == Mode.LIST) {
            delBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        } else {
            dwnBtn.addStyleName(ExtaTheme.BUTTON_ICON_ALIGN_TOP);
            dwnBtn.addStyleName(ExtaTheme.LARGE_ICON);
        }

        if (pSource.isReadOnly())
            delBtn.setVisible(false);

        return root;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(final Mode mode) {
        if (this.mode != mode) {
            this.mode = mode;
            if (mode == Mode.LIST) {
                filesContainer.setColumns(1);
            } else {
                filesContainer.setColumns(4);
            }
        }
    }

    public void addUploadedFile(final byte[] inputStream, final String filename, final String mimeType, final long length) {
        TFileContainer fileContainer = null;
        try {
            fileContainer = containerClass.newInstance();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        fileContainer.setName(filename);
        fileContainer.setMimeType(mimeType);
        fileContainer.setFileSize(length);
        fileContainer.setFileData(inputStream);
        container.addBean(fileContainer);
        setValue(newArrayList(container.getItemIds()));
    }


    private void deleteClickListener(final Button.ClickEvent clickEvent) {
        container.removeItem(clickEvent.getButton().getData());
        setValue(newArrayList(container.getItemIds()));
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private class FileDropBox extends DragAndDropWrapper implements DropHandler {
        private static final long FILE_SIZE_LIMIT = 20 * 1024 * 1024; // 20MB

        public FileDropBox(final Component root) {
            super(root);
            setWidth(100, Unit.PERCENTAGE);
            setDropHandler(this);
        }

        @Override
        public void drop(final DragAndDropEvent dropEvent) {

            if (!isReadOnly()) {
                // expecting this to be an html5 drag
                final WrapperTransferable tr = (WrapperTransferable) dropEvent.getTransferable();
                final Html5File[] files = tr.getFiles();
                if (files != null) {
                    for (final Html5File html5File : files) {
                        final String fileName = html5File.getFileName();

                        if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
                            NotificationUtil.showError("Ошибка загрузки",
                                    MessageFormat.format("Файл {0} не может быть загрузен, Поскольку превышает лимит в 20MB", fileName));
                        } else {

                            final ByteArrayOutputStream bas = new ByteArrayOutputStream((int) html5File.getFileSize());
                            final StreamVariable streamVariable = new StreamVariable() {

                                @Override
                                public OutputStream getOutputStream() {
                                    return bas;
                                }

                                @Override
                                public boolean listenProgress() {
                                    return false;
                                }

                                @Override
                                public void onProgress(final StreamingProgressEvent event) {
                                }

                                @Override
                                public void streamingStarted(final StreamingStartEvent event) {
                                }

                                @Override
                                public void streamingFinished(final StreamingEndEvent event) {
                                    progress.setVisible(false);
                                    addUploadedFile(bas.toByteArray(), fileName, html5File.getType(), html5File.getFileSize());
                                }

                                @Override
                                public void streamingFailed(final StreamingErrorEvent event) {
                                    progress.setVisible(false);
                                }

                                @Override
                                public boolean isInterrupted() {
                                    return false;
                                }
                            };
                            html5File.setStreamVariable(streamVariable);
                            progress.setVisible(true);
                        }
                    }

                }
            }
        }

        @Override
        public AcceptCriterion getAcceptCriterion() {
            return AcceptAll.get();
        }
    }

    private enum Mode {
        LIST,
        GRID
    }
}
