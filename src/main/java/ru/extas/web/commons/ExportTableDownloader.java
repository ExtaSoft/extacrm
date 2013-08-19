/**
 *
 */
package ru.extas.web.commons;

import com.google.common.base.Throwables;
import com.vaadin.ui.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Valery Orlov
 */
public class ExportTableDownloader extends OnDemandFileDownloader {

    private static final long serialVersionUID = 7611037576590036208L;

    /**
     * @author Valery Orlov
     */
    private static final class CSVStreamResource implements OnDemandStreamResource {
        private static final long serialVersionUID = 7100189998481006969L;
        private final Logger logger = LoggerFactory.getLogger(ExportTableDownloader.class);
        private final Table table;
        private final String fileName;

        /**
         * @param table
         */
        private CSVStreamResource(final Table table, final String fileName) {
            this.table = table;
            this.fileName = fileName;
        }

        @Override
        public InputStream getStream() {
            try {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                CsvUtil.tableToCsv(table, out);
                return new ByteArrayInputStream(out.toByteArray());
            } catch (final IOException e) {
                logger.error("Export table error", e);
                throw Throwables.propagate(e);
            }
        }

        @Override
        public String getFilename() {
            return fileName;
        }
    }

    /**
     * @param table
     */
    public ExportTableDownloader(final Table table, final String fileName) {
        super(new CSVStreamResource(table, fileName));
    }

}
