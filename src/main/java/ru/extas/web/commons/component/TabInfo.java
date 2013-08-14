/**
 * 
 */
package ru.extas.web.commons.component;

import java.io.Serializable;

import com.vaadin.ui.Component;

public interface TabInfo extends Serializable {

	public String getCaption();

	public Component createComponent();
}