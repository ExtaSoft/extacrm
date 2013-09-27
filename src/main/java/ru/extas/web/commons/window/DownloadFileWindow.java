package ru.extas.web.commons.window;

import com.google.common.base.Throwables;
import com.vaadin.server.FileDownloader;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.OnDemandFileDownloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Окошко с информацией о файле и кнопкой "Скачать"
 *
 * @author Valery Orlov
 */
public class DownloadFileWindow extends Window {

    private static final long serialVersionUID = -1869372339151029572L;
    private final Logger logger = LoggerFactory.getLogger(DownloadFileWindow.class);

    public DownloadFileWindow(final byte[] file, String fileName) {
        initWindow("Файл готов к загрузке...", file, fileName);
    }

    private void initWindow(String caption, byte[] file, String fileName) {

        setCaption(caption);

        final VerticalLayout contentContainer = new VerticalLayout();
        contentContainer.setMargin(true);
        contentContainer.setSpacing(true);

        final Label infoLbl = new Label(fileName);
        contentContainer.addComponent(infoLbl);

        final Button downloadBtn = new Button("Скачать");
        //downloadBtn.setStyleName("icon-download");
        createPolicyDownloader(file, fileName).extend(downloadBtn);

        contentContainer.addComponent(downloadBtn);
        contentContainer.setComponentAlignment(downloadBtn, Alignment.MIDDLE_CENTER);

        setContent(contentContainer);
    }

    private FileDownloader createPolicyDownloader(final byte[] file, String fileName) {

        // Подготовить имя файла
        final String webFileName;
        try {
            webFileName = URLEncoder.encode(fileName
                    .replaceAll(" ", ".").replaceAll("/", "-")
                    , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Can't convert file name to web URI", e);
            throw Throwables.propagate(e);
        }


        return new OnDemandFileDownloader(new OnDemandFileDownloader.OnDemandStreamResource() {
            @Override
            public String getFilename() {
                return webFileName;
            }

            @Override
            public InputStream getStream() {
                return new ByteArrayInputStream(file);
            }
        });
//        return new FileDownloader(
//                new StreamResource(new StreamResource.StreamSource() {
//                    @Override
//                    public InputStream getStream() {
//                        return new ByteArrayInputStream(file);
//                    }
//
//                }, webFileName));
    }

    public void showModal() {
        setClosable(true);
        setResizable(false);
        setModal(true);

        UI.getCurrent().addWindow(this);
    }

}
