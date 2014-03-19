/**
 *
 */
package ru.extas.web.commons.component;

import ru.extas.security.ExtaDomain;

/**
 *
 */
public abstract class AbstractTabInfo implements TabInfo {
    private static final long serialVersionUID = -4891758708180700074L;
    private final String caption;
	private final ExtaDomain domain;

	public AbstractTabInfo(final String caption, final ExtaDomain domain) {
		this.caption = caption;
		this.domain = domain;
	}

	/**
     * @return the caption
     */
    @Override
    public String getCaption() {
        return caption;
    }

	@Override
	public ExtaDomain getDomain() {
		return domain;
	}
}