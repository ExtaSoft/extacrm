package ru.extas.web.commons;

import com.vaadin.data.util.converter.Converter;

import java.io.Serializable;
import java.util.EnumSet;

/**
 * Привязка пользовательского интерфейса с данным
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class DataDeclMapping implements Serializable {

    private final String propName;
    private final String caption;
    private final EnumSet<PresentFlag> presentFlags;
    private final Converter<String, ?> converter;
    private final GridDataDecl.GridColumnGenerator generator;

    public DataDeclMapping(String propName, String caption, GridDataDecl.GridColumnGenerator generator, EnumSet<PresentFlag> presentFlags) {
        this(propName, caption, presentFlags, null, generator);
    }

    /**
     * Параметры отображения
     *
     * @author Valery Orlov
     */
    public enum PresentFlag {
        /**
         * Свернутый столбец в гриде
         */
        COLLAPSED
    }

    /**
     * <p>Constructor for DataDeclMapping.</p>
     *
     * @param propName a {@link java.lang.String} object.
     * @param caption a {@link java.lang.String} object.
     */
    public DataDeclMapping(String propName, String caption) {
        this(propName, caption, null, null);
    }

    /**
     * <p>Constructor for DataDeclMapping.</p>
     *
     * @param propName a {@link java.lang.String} object.
     * @param caption a {@link java.lang.String} object.
     * @param presentFlags a {@link java.util.EnumSet} object.
     */
    public DataDeclMapping(String propName, String caption, EnumSet<PresentFlag> presentFlags) {
        this(propName, caption, presentFlags, null, null);
    }

    /**
     * <p>Constructor for DataDeclMapping.</p>
     *
     * @param propName a {@link java.lang.String} object.
     * @param caption a {@link java.lang.String} object.
     * @param converter a {@link com.vaadin.data.util.converter.Converter} object.
     */
    public DataDeclMapping(String propName, String caption, Converter<String, ?> converter) {
        this(propName, caption, null, converter, null);
    }

    /**
     * <p>Constructor for DataDeclMapping.</p>
     *
     * @param propName a {@link java.lang.String} object.
     * @param caption a {@link java.lang.String} object.
     * @param presentFlags a {@link java.util.EnumSet} object.
     * @param converter a {@link com.vaadin.data.util.converter.Converter} object.
     */
    public DataDeclMapping(String propName, String caption, EnumSet<PresentFlag> presentFlags, Converter<String, ?> converter, GridDataDecl.GridColumnGenerator generator) {
        super();
        this.propName = propName;
        this.caption = caption;
        if (presentFlags == null)
            this.presentFlags = EnumSet.noneOf(PresentFlag.class);
        else
            this.presentFlags = presentFlags;
        this.converter = converter;
        this.generator = generator;
    }

    public GridDataDecl.GridColumnGenerator getGenerator() {
        return generator;
    }

    /**
     * <p>Getter for the field <code>propName</code>.</p>
     *
     * @return the propName
     */
    public final String getPropName() {
        return propName;
    }

    /**
     * <p>Getter for the field <code>caption</code>.</p>
     *
     * @return the caption
     */
    public final String getCaption() {
        return caption;
    }

    /**
     * <p>isCollapsed.</p>
     *
     * @return the collapsed
     */
    public final boolean isCollapsed() {
        return presentFlags.contains(PresentFlag.COLLAPSED);
    }

    /**
     * <p>Getter for the field <code>converter</code>.</p>
     *
     * @return the converter
     */
    public Converter<String, ?> getConverter() {
        return converter;
    }

}
