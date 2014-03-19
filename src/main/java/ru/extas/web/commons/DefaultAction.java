package ru.extas.web.commons;

/**
 * <p>Abstract DefaultAction class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 22:37
 * @version $Id: $Id
 */
public abstract class DefaultAction extends ItemAction {
    /**
     * <p>Constructor for DefaultAction.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param iconStyle a {@link java.lang.String} object.
     */
    public DefaultAction(String name, String description, String iconStyle) {
        super(name, description, iconStyle);
    }
}
