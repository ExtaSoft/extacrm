package ru.extas.web.info;

import com.google.common.io.ByteStreams;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.*;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import org.vaadin.viritin.fields.MTextArea;
import ru.extas.model.info.InfoFile;
import ru.extas.server.info.InfoFileRepository;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.FileDownloadButton;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FileUploader;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.motor.MotorBrandMultiselect;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Ввод/редактирование информационного материала
 * <p>
 * Created by valery on 25.10.16.
 */
public class InfoFileEditForm extends ExtaEditForm<InfoFile> {

    private ProgressBar progress;
    private FileDownloadButton fileNameBtn;

    @PropertyId("description")
    private MTextArea descriptionField;

    @PropertyId("permitBrands")
    private MotorBrandMultiselect brandsField;

    public InfoFileEditForm(final InfoFile infoFile) {
        super("Информационный материал", infoFile);
    }

    @Override
    protected void initEntity(final InfoFile infoFile) {

    }

    @Override
    protected InfoFile saveEntity(InfoFile infoFile) {
        infoFile = lookup(InfoFileRepository.class).save(infoFile);
        NotificationUtil.showSuccess("Информационный материал сохранен");
        return infoFile;
    }

    @Override
    protected ComponentContainer createEditFields() {
        final InfoFile file = getEntity();

        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();
        form.setMargin(true);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Файл"));
        final VerticalLayout root = new VerticalLayout();
        fileNameBtn = new FileDownloadButton(file);
        root.addComponent(fileNameBtn);

        root.setWidth(100, Unit.PERCENTAGE);
        root.addStyleName("drop-area");
        final Label infoLabel = new Label("Перетащите файл для загрузки сюда, или нажмите кнопку \"Загрузить файл...\"");
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
        final FileUploader fileUploader = new FileUploader("Загрузить файл...", handler);
        fileUploader.setWidth(100, Unit.PERCENTAGE);
        root.addComponent(fileUploader);

        final FileDropBox dropBox = new FileDropBox(root);
        form.addComponent(dropBox);

        descriptionField = new MTextArea("Описание");
        form.addComponent(descriptionField);

        ////////////////////////////////////////////////////////////////////////////////////////////
        form.addComponent(new FormGroupHeader("Доступ"));
        brandsField = new MotorBrandMultiselect("Только брендам");
        form.addComponent(brandsField);


        brandsField.addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
            infoLabel.setVisible(!isRedOnly);
//            progress.setVisible(!isRedOnly);
            fileUploader.setVisible(!isRedOnly);
            dropBox.setReadOnly(isRedOnly);
        });
        return form;
    }

    private void addUploadedFile(final byte[] inputStream, final String filename, final String mimeType, final long length) {
        final InfoFile file = getEntity();
        file.setName(filename);
        file.setMimeType(mimeType);
        file.setFileSize(length);
        file.setFileData(inputStream);
        fileNameBtn.updateFileData(file);
        setModified(true);
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
                    if (files.length > 0) {
                        final Html5File html5File = files[0];
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
}
