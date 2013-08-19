package ru.extas.web.commons;

import com.vaadin.server.*;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This specializes {@link FileDownloader} in a way, such that both the file
 * name and content can be determined on-demand, i.e. when the user has clicked
 * the component.
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

    public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource) {
        super(new StreamResource(onDemandStreamResource, "some.file.ext"));
        this.onDemandStreamResource = checkNotNull(onDemandStreamResource, "The given on-demand stream resource may never be null!");
        setErrorHandler(new ErrorHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Notification.show("Ошибка печати файла", event.getThrowable().getMessage(), Type.ERROR_MESSAGE);
            }
        });

    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
        getResource().setFilename(onDemandStreamResource.getFilename());
        return super.handleConnectorRequest(request, response, path);
    }

    private StreamResource getResource() {
        return (StreamResource) this.getFileDownloadResource();
    }

}