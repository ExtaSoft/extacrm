package ru.extas.web.commons;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gwt.thirdparty.guava.common.io.Closeables;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.textile.core.TextileLanguage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Класс для загрузки вспомогательных документов
 *
 * @author Valery Orlov
 *         Date: 11.04.2014
 *         Time: 9:27
 * @version $Id: $Id
 * @since 0.4.2
 */
public class HelpContent {

    /**
     * Загружает документ markdown из classpath
     *
     * @param resource путь к документу в classpath
     * @return Подготовленный html документ
     * @throws java.io.IOException if any.
     */
    public static String loadMarkDown(String resource) throws IOException {
        // Читаем файл помоци
        InputStream contentStream = HelpContent.class.getResourceAsStream(resource);
        String content = CharStreams.toString(new InputStreamReader(contentStream, Charsets.UTF_8));
        Closeables.closeQuietly(contentStream);

        // Читаем файл стилей
        InputStream cssStream = HelpContent.class.getResourceAsStream("/help/rest/help.css");
        String cssContent = CharStreams.toString(new InputStreamReader(cssStream, Charsets.UTF_8))
                .replaceAll(System.getProperty("line.separator"), "");
        Closeables.closeQuietly(contentStream);

        // Преобразуем в html
        StringBuilder out = new StringBuilder();
        HtmlDocumentBuilder builder = new HtmlDocumentBuilder(CharStreams.asWriter(out));
        HtmlDocumentBuilder.Stylesheet stylesheet = new HtmlDocumentBuilder.Stylesheet(new StringReader(cssContent));
        builder.addCssStylesheet(stylesheet);
        MarkupParser markupParser = new MarkupParser();
        markupParser.setMarkupLanguage(new TextileLanguage());
        markupParser.setBuilder(builder);
        markupParser.parse(content);
        return out.toString();
    }
}
