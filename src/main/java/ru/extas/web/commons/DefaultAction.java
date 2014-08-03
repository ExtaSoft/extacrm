package ru.extas.web.commons;

import com.vaadin.server.Resource;

/**
 * <p>Abstract DefaultAction class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 22:37
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class DefaultAction extends ItemAction {
    /**
     * <p>Constructor for DefaultAction.</p>
     *  @param name a {@link String} object.
     * @param description a {@link String} object.
     * @param icon a {@link String} object.
     */
    public DefaultAction(String name, String description, Resource icon) {
        super(name, description, icon);
    }
}
