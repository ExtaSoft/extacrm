package ru.extas.web.commons;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import java.io.Serializable;

/**
 * <p>Abstract UIAction class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 16:16
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class UIAction implements Serializable {

    // Имя действия
    protected String name;
    // Описание действия
    protected String description;
    // Стиль иконки
    protected String iconStyle;

    /**
     * <p>Constructor for UIAction.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param iconStyle a {@link java.lang.String} object.
     */
    public UIAction(String name, String description, String iconStyle) {
        this.description = description;
        this.name = name;
        this.iconStyle = iconStyle;
    }

    /**
     * <p>createButton.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    public Component createButton() {
        final Button button = new Button(getName());
        button.setDescription(getDescription());
        button.addStyleName(getIconStyle());
        return button;
    }
    // Функция
    /**
     * <p>fire.</p>
     *
     * @param itemId a {@link java.lang.Object} object.
     */
    abstract public void fire(final Object itemId);

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>iconStyle</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getIconStyle() {
        return iconStyle;
    }

    /**
     * <p>Setter for the field <code>iconStyle</code>.</p>
     *
     * @param iconStyle a {@link java.lang.String} object.
     */
    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }
}
