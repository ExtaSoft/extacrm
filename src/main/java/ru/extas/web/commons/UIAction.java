package ru.extas.web.commons;

import com.vaadin.server.Resource;
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
    protected Resource icon;

    /**
     * <p>Constructor for UIAction.</p>
     *  @param name a {@link String} object.
     * @param description a {@link String} object.
     * @param icon a {@link String} object.
     */
    public UIAction(String name, String description, Resource icon) {
        this.description = description;
        this.name = name;
        this.icon = icon;
    }

    /**
     * <p>createButton.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    public Component createButton() {
        final Button button = new Button(getName());
        button.setDescription(getDescription());
        button.setIcon(getIcon());
        return button;
    }
    /**
     * Функция
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
     * <p>Getter for the field <code>icon</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public Resource getIcon() {
        return icon;
    }

    /**
     * <p>Setter for the field <code>icon</code>.</p>
     *
     * @param icon a {@link String} object.
     */
    public void setIcon(Resource icon) {
        this.icon = icon;
    }
}
