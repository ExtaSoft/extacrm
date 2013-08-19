/**
 *
 */
package ru.extas.web.commons.component;

public abstract class AbstractTabInfo implements TabInfo {
    private static final long serialVersionUID = -4891758708180700074L;
    private final String caption;

    /**
     * @param caption
     */
    public AbstractTabInfo(String caption) {
        this.caption = caption;
    }

    /**
     * @return the caption
     */
    @Override
    public String getCaption() {
        return caption;
    }

}