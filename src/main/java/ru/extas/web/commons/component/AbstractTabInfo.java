package ru.extas.web.commons.component;

import ru.extas.model.security.ExtaDomain;



/**
 * <p>Abstract AbstractTabInfo class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class AbstractTabInfo implements TabInfo {
    private static final long serialVersionUID = -4891758708180700074L;
    private final String caption;
	private final ExtaDomain domain;

	/**
	 * <p>Constructor for AbstractTabInfo.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param domain a {@link ru.extas.model.security.ExtaDomain} object.
	 */
	public AbstractTabInfo(final String caption, final ExtaDomain domain) {
		this.caption = caption;
		this.domain = domain;
	}

    /** {@inheritDoc} */
    @Override
    public String getCaption() {
        return caption;
    }

	/** {@inheritDoc} */
	@Override
	public ExtaDomain getDomain() {
		return domain;
	}
}
