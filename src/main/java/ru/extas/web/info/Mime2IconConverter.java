package ru.extas.web.info;

import com.vaadin.data.util.converter.Converter;
import ru.extas.web.commons.FileUtil;

import java.util.Locale;

/**
 * Конвертирует mime тип в html код с иконкой
 *
 * Created by valery on 24.10.16.
 */
public class Mime2IconConverter implements Converter<String, String> {
    @Override
    public String convertToModel(String string, Class<? extends String> aClass, Locale locale) throws ConversionException {
        return "not implemented";
    }

    @Override
    public String convertToPresentation(String string, Class<? extends String> aClass, Locale locale) throws ConversionException {
        return FileUtil.getFileIconByMime(string).getHtml();
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
