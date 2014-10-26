package ru.extas.web.commons;

import ru.extas.model.security.ExtaDomain;


/**
 * <p>Abstract AbstractTabInfo class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class SubdomainInfoImpl implements SubdomainInfo {
    private static final long serialVersionUID = -4891758708180700074L;
    private final String caption;
	private final ExtaDomain domain;
    private final boolean editInPage;

    protected SubdomainInfoImpl(final String caption, final ExtaDomain domain) {
        this(caption, domain, false);
    }

    /**
	 * <p>Constructor for AbstractTabInfo.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param domain a {@link ru.extas.model.security.ExtaDomain} object.
	 */
	public SubdomainInfoImpl(final String caption, final ExtaDomain domain, final boolean editInPage) {
		this.caption = caption;
		this.domain = domain;
        this.editInPage = editInPage;
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

    @Override
    public boolean isEditInPage() {
        return editInPage;
    }
}
