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
 */
public class FileUploader extends CustomComponent {

    public FileUploader(String caption, UploadFinishedHandler handler, boolean multiple) {
        UploadStateWindow window = new UploadStateWindow();
        window.setUploadStatusCaption("Статус загрузки");
        window.setCancelButtonCaption("Отменить");
        window.setWindowPosition(UploadStateWindow.WindowPosition.CENTER);
        window.setModal(true);
        ConfirmDialog confDialog = new ConfirmDialog();
        confDialog.setConfirmHeader("Прерывание загрузки...");
        confDialog.setConfirmQuestion("Это действие отменит загрузку всех файлов. Продолжить?");
        confDialog.setConfirmYes("Да");
        confDialog.setConfirmNo("Нет");
        window.setConfirmDialog(confDialog);

        MultiFileUpload multiFileUpload = new MultiFileUpload(handler, window, multiple);
        multiFileUpload.setMaxFileSize(128*1024*1024);
        multiFileUpload.setInterruptedMsg("Загрузка прервана...");
        multiFileUpload.setSizeErrorMsgPattern(
                "Файл '{2}', размером {1}, не может быть загружен, поскольку превышает допустимый размер файла для загрузки - {0}.");
        multiFileUpload.setUploadButtonCaptions(caption, caption);

        setCompositionRoot(multiFileUpload);
    }

    public FileUploader(String caption, UploadFinishedHandler handler) {
        this(caption, handler, true);
    }
}
