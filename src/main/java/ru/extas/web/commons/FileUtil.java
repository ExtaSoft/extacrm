package ru.extas.web.commons;

import com.vaadin.server.FontIcon;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by valery on 25.10.16.
 */
public class FileUtil {

    public static FontIcon getFileIconByMime(final String mimeType) {
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
}
