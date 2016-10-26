package ru.extas.web.commons;

import org.vaadin.viritin.button.MButton;
import ru.extas.model.common.FileContainer;
import ru.extas.web.commons.window.DownloadFileWindow;

/**
 * Кнопка загрузки файла
 *
 * Created by valery on 26.10.16.
 */
public class FileDownloadButton extends MButton {

    private FileContainer file;

    public FileDownloadButton(FileContainer file) {
        updateFileData(file);
        addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addStyleName(ExtaTheme.BUTTON_SMALL);
        addClickListener(e -> new DownloadFileWindow(file.getFileData(), file.getName()).showModal());
    }

    public void updateFileData(FileContainer file) {
        this.file = file;
        if(file != null) {
            setIcon(FileUtil.getFileIconByMime(file.getMimeType()));
            setCaption(file.getName());
        }
    }

    public FileContainer getFile() {
        return file;
    }

    public void setFile(FileContainer file) {
        this.file = file;
    }
}
