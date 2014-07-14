
/**
 * <p>TabInfo interface.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import ru.extas.model.security.ExtaDomain;

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

	/**
	 * <p>getDomain.</p>
	 *
	 * @return a {@link ru.extas.model.security.ExtaDomain} object.
	 */
	ExtaDomain getDomain();
}
