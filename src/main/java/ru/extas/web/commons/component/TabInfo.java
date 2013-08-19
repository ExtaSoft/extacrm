/**
 *
 */
package ru.extas.web.commons.component;

import com.vaadin.ui.Component;

import java.io.Serializable;

public interface TabInfo extends Serializable {

    public String getCaption();

    public Component createComponent();
}