package ru.extas.web.commons;

import java.io.Serializable;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 16:16
 */
public abstract class UIAction implements Serializable {

    // Имя действия
    protected String name;
    // Описание действия
    protected String description;
    // Стиль иконки
    protected String iconStyle;

    public UIAction(String name, String description, String iconStyle) {
        this.description = description;
        this.name = name;
        this.iconStyle = iconStyle;
    }

    // Функция
    abstract public void fire(final Object itemId);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }
}
