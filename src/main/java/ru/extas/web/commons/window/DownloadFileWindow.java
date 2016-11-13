package ru.extas.web.commons.window;

import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.OnDemandFileDownloader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Окошко с информацией о файле и кнопкой "Скачать"
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class DownloadFileWindow extends Window {

	private static final long serialVersionUID = -1869372339151029572L;
	private final static Logger logger = LoggerFactory.getLogger(DownloadFileWindow.class);

	/**
	 * <p>Constructor for DownloadFileWindow.</p>
	 *
	 * @param file an array of byte.
	 * @param fileName a {@link java.lang.String} object.
	 */
	public DownloadFileWindow(final byte[] file, final String fileName) {
		initWindow("Файл готов к загрузке...", file, fileName);
	}

	private void initWindow(final String caption, final byte[] file, final String fileName) {

		setCaption(caption);

		final VerticalLayout contentContainer = new VerticalLayout();
		contentContainer.setMargin(true);
		contentContainer.setSpacing(true);

		final Label infoLbl = new Label(fileName);
		contentContainer.addComponent(infoLbl);

		final HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(true);

		final Button downloadBtn = new Button("Скачать", Fontello.DOWNLOAD);
		createDownloader(file, fileName).extend(downloadBtn);
		toolbar.addComponent(downloadBtn);

		final Button viewBtn = new Button("Посмотреть", Fontello.SEARCH_1);
		final BrowserWindowOpener opener =
				new BrowserWindowOpener(new StreamResource(
                        () -> new ByteArrayInputStream(file), encodeWebFileName(fileName)));
		opener.extend(viewBtn);
		toolbar.addComponent(viewBtn);

		contentContainer.addComponent(toolbar);
		contentContainer.setComponentAlignment(toolbar, Alignment.MIDDLE_CENTER);

		setContent(contentContainer);
	}

	private FileDownloader createDownloader(final byte[] file, final String fileName) {

		// Подготовить имя файла
		final String webFileName = encodeWebFileName(fileName);


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

	private String encodeWebFileName(final String fileName) {
//		try {
//			return URLEncoder.encode(fileName
//					.replaceAll(" ", ".").replaceAll("/", "-")
//					, "UTF-8");
//		} catch (final UnsupportedEncodingException e) {
//			logger.error("Can't convert file name to web URI", e);
//			throw Throwables.propagate(e);
//		}
		return fileName;
	}

	/**
	 * <p>showModal.</p>
	 */
	public void showModal() {
		setClosable(true);
		setResizable(false);
		setModal(true);

		UI.getCurrent().addWindow(this);
	}

}
