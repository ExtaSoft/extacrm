package ru.extas.web.commons;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

/**
 * <p>Abstract ItemAction class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 22:36
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ItemAction extends UIAction {
    /**
     * <p>Constructor for ItemAction.</p>
     *  @param name a {@link String} object.
     * @param description a {@link String} object.
     * @param icon a {@link String} object.
     */
    public ItemAction(String name, String description, Resource icon) {
        super(name, description, icon);
    }

    /** {@inheritDoc} */
    @Override
    public Component createButton() {
        Component button = super.createButton();
        button.addStyleName("item-action");
        return button;
    }
}
