
/**
 * <p>TabInfo interface.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 */
package ru.extas.web.commons.component;

import com.vaadin.ui.Component;

import java.io.Serializable;
public interface TabInfo extends Serializable {

    /**
     * <p>getCaption.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCaption();

    /**
     * <p>createComponent.</p>
     *
     * @return a {@link com.vaadin.ui.Component} object.
     */
    public Component createComponent();
}
