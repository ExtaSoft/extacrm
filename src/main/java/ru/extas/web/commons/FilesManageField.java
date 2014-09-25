package ru.extas.web.commons;

import com.google.common.io.ByteStreams;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import ru.extas.model.common.FileContainer;
import ru.extas.web.commons.component.FileUploader;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 15.09.2014
 *         Time: 23:51
 */
public class FilesManageField<TFileContainer extends FileContainer> extends CustomField<List> {

    private final Class<TFileContainer> containerClass;
    private BeanItemContainer<TFileContainer> container;
    private ProgressBar progress;

    private Mode mode;
    private ItemGrid filesContainer;

    public FilesManageField(Class<TFileContainer> containerClass) {
        this.containerClass = containerClass;
        setBuffered(true);
    }


    @Override
    protected Component initContent() {
        final Property dataSource = getPropertyDataSource();
        final List<TFileContainer> list = dataSource != null ? (List<TFileContainer>) dataSource.getValue() : new ArrayList<>();
        container = new BeanItemContainer<>(containerClass);
        if (list != null) {
            container.addAll(list);
        }

        VerticalLayout root = new VerticalLayout();
        root.addStyleName("drop-area");

        filesContainer = new ItemGrid();
        filesContainer.setContainerDataSource(container);
        filesContainer.setItemGenerator((pSource, pItemId) -> getItemComponent((TFileContainer) pItemId));
        root.addComponent(filesContainer);
        setMode(Mode.LIST);

        final Label infoLabel = new Label("Перетащите файлы для загрузки сюда, или нажмите кнопку \"Добавить файлы...\"");
//        infoLabel.setIcon(Fontello.INFO_CIRCLED);
        root.addComponent(infoLabel);

        progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);
        root.addComponent(progress);

        UploadFinishedHandler handler = (inputStream, filename, mimeType, length) -> {
            try {
                addUploadedFile(ByteStreams.toByteArray(inputStream), filename, mimeType, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        FileUploader fileUploader = new FileUploader("Добавить файлы...", handler);
        root.addComponent(fileUploader);

        return new FileDropBox(root);
    }

    private Component getItemComponent(TFileContainer item) {
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

        Button dwnBtn = new Button(item.getName());
        dwnBtn.setIcon(getFileIcon(item.getMimeType()));
        dwnBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        dwnBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        dwnBtn.setData(item);
        dwnBtn.addClickListener(this::downloadClickListener);
        root.addComponent(dwnBtn);

        Button delBtn = new Button("Удалить", Fontello.TRASH_4);
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

        return root;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        if (this.mode != mode) {
            this.mode = mode;
            if (mode == Mode.LIST) {
                filesContainer.setColumns(1);
            } else {
                filesContainer.setColumns(4);
            }
        }
    }

    private Resource getFileIcon(String mimeType) {
        // TODO: Move to map
        if (!isNullOrEmpty(mimeType)) {
            if (mimeType.startsWith("image/"))
                return Fontello.FILE_IMAGE;
            else if (mimeType.equals("application/pdf"))
                return Fontello.FILE_PDF;
            else if (mimeType.equals("application/vnd.ms-excel")
                    || mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                return Fontello.FILE_EXCEL;
            else if (mimeType.equals("application/msword")
                    || mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                return Fontello.FILE_WORD;
            else if (mimeType.equals("application/vnd.ms-powerpoint")
                    || mimeType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation"))
                return Fontello.FILE_POWERPOINT;
            else if (mimeType.equals("application/zip")
                    || mimeType.equals("application/x-rar-compressed")
                    || mimeType.equals("application/x-gzip"))
                return Fontello.FILE_ARCHIVE;
            else if (mimeType.startsWith("audio/"))
                return Fontello.FILE_AUDIO;
            else if (mimeType.startsWith("video/"))
                return Fontello.FILE_VIDEO;
        }
        return Fontello.FILE_CODE;
    }

    public void addUploadedFile(byte[] inputStream, String filename, String mimeType, long length) {
        TFileContainer fileContainer = null;
        try {
            fileContainer = containerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fileContainer.setName(filename);
        fileContainer.setMimeType(mimeType);
        fileContainer.setFileSize(length);
        fileContainer.setFileData(inputStream);
        container.addBean(fileContainer);
        setValue(newArrayList(container.getItemIds()));
    }


    private void deleteClickListener(Button.ClickEvent clickEvent) {
        container.removeItem(clickEvent.getButton().getData());
        setValue(newArrayList(container.getItemIds()));
    }

    private void downloadClickListener(Button.ClickEvent clickEvent) {
        TFileContainer fileContainer = (TFileContainer) clickEvent.getButton().getData();
        new DownloadFileWindow(fileContainer.getFileData(), fileContainer.getName()).showModal();
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private class FileDropBox extends DragAndDropWrapper implements DropHandler {
        private static final long FILE_SIZE_LIMIT = 20 * 1024 * 1024; // 20MB

        public FileDropBox(final Component root) {
            super(root);
            setDropHandler(this);
        }

        @Override
        public void drop(final DragAndDropEvent dropEvent) {

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
