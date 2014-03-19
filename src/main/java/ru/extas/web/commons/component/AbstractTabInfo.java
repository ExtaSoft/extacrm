
/**
 * <p>Abstract AbstractTabInfo class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 */
package ru.extas.web.commons.component;
public abstract class AbstractTabInfo implements TabInfo {
    private static final long serialVersionUID = -4891758708180700074L;
    private final String caption;

    /**
     * <p>Constructor for AbstractTabInfo.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public AbstractTabInfo(String caption) {
        this.caption = caption;
    }

    /** {@inheritDoc} */
    @Override
    public String getCaption() {
        return caption;
    }

}
