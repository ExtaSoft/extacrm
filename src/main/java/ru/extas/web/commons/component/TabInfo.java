/**
 *
 */
package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import ru.extas.security.ExtaDomain;

import java.io.Serializable;

public interface TabInfo extends Serializable {

    public String getCaption();

    public Component createComponent();

	ExtaDomain getDomain();
}