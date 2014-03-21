package ru.extas.web.commons;

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
     *
     * @param name a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param iconStyle a {@link java.lang.String} object.
     */
    public ItemAction(String name, String description, String iconStyle) {
        super(name, description, iconStyle);
    }
}
