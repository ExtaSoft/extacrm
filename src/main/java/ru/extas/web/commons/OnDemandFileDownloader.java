package ru.extas.web.commons;

import com.vaadin.server.*;
import com.vaadin.server.StreamResource.StreamSource;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This specializes {@link com.vaadin.server.FileDownloader} in a way, such that both the file
 * name and content can be determined on-demand, i.e. when the user has clicked
 * the component.
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
public class OnDemandFileDownloader extends FileDownloader {

    /**
     * Provide both the {@link StreamSource} and the filename in an on-demand
     * way.
     */
    public interface OnDemandStreamResource extends StreamSource {
        String getFilename();
    }

    private static final long serialVersionUID = 1L;
    private final OnDemandStreamResource onDemandStreamResource;

    /**
     * <p>Constructor for OnDemandFileDownloader.</p>
     *
     * @param onDemandStreamResource a {@link ru.extas.web.commons.OnDemandFileDownloader.OnDemandStreamResource} object.
     */
    public OnDemandFileDownloader(final OnDemandStreamResource onDemandStreamResource) {
        super(new StreamResource(onDemandStreamResource, "some.file.ext"));
        this.onDemandStreamResource = checkNotNull(onDemandStreamResource, "The given on-demand stream resource may never be null!");
        setErrorHandler(new ErrorHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public void error(final com.vaadin.server.ErrorEvent event) {
                NotificationUtil.showError("Ошибка печати файла", event.getThrowable().getMessage());
            }
        });

    }

    /** {@inheritDoc} */
    @Override
    public boolean handleConnectorRequest(final VaadinRequest request, final VaadinResponse response, final String path) throws IOException {
        getResource().setFilename(onDemandStreamResource.getFilename());
        return super.handleConnectorRequest(request, response, path);
    }

    private StreamResource getResource() {
        return (StreamResource) this.getFileDownloadResource();
    }

}
