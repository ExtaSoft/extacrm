package ru.extas.web.commons.component;

import com.vaadin.ui.CustomComponent;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.ConfirmDialog;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

/**
 * Компонент отправки файлов
 *
 * @author Valery Orlov
 *         Date: 18.04.2014
 *         Time: 16:48
 * @version $Id: $Id
 * @since 0.4.2
 */
public class FileUploader extends CustomComponent {

    /**
     * <p>Constructor for FileUploader.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param handler a {@link com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler} object.
     * @param multiple a boolean.
     */
    public FileUploader(final String caption, final UploadFinishedHandler handler, final boolean multiple) {
        final UploadStateWindow window = new UploadStateWindow();
        window.setUploadStatusCaption("Статус загрузки");
        window.setCancelButtonCaption("Отменить");
        window.setWindowPosition(UploadStateWindow.WindowPosition.CENTER);
        window.setModal(true);
        final ConfirmDialog confDialog = new ConfirmDialog();
        confDialog.setConfirmHeader("Прерывание загрузки...");
        confDialog.setConfirmQuestion("Это действие отменит загрузку всех файлов. Продолжить?");
        confDialog.setConfirmYes("Да");
        confDialog.setConfirmNo("Нет");
        window.setConfirmDialog(confDialog);

        final MultiFileUpload multiFileUpload = new MultiFileUpload(handler, window, multiple);
        multiFileUpload.setMaxFileSize(128*1024*1024);
        multiFileUpload.setInterruptedMsg("Загрузка прервана...");
        multiFileUpload.setSizeErrorMsgPattern(
                "Файл '{2}', размером {1}, не может быть загружен, поскольку превышает допустимый размер файла для загрузки - {0}.");
        multiFileUpload.setUploadButtonCaptions(caption, caption);

        setCompositionRoot(multiFileUpload);
    }

    /**
     * <p>Constructor for FileUploader.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param handler a {@link com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler} object.
     */
    public FileUploader(final String caption, final UploadFinishedHandler handler) {
        this(caption, handler, true);
    }
}
