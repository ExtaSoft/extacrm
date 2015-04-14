package ru.extas.web.commons.container;

import java.util.Collection;

/**
 * <p>RefreshBeanContainer class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
public class RefreshBeanContainer<BEANTYPE> extends ExtaBeanContainer<BEANTYPE> {

    /**
     * <p>Constructor for RefreshBeanContainer.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @throws java.lang.IllegalArgumentException if any.
     */
    public RefreshBeanContainer(final Class<? super BEANTYPE> type) throws IllegalArgumentException {
        super(type);
    }

    /**
     * <p>Constructor for RefreshBeanContainer.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param collection a {@link java.util.Collection} object.
     * @throws java.lang.IllegalArgumentException if any.
     */
    public RefreshBeanContainer(final Class<? super BEANTYPE> type, final Collection<? extends BEANTYPE> collection) throws IllegalArgumentException {
        super(type, collection);
    }

    /**
     * <p>refreshItems.</p>
     */
    public void refreshItems() {
        fireItemSetChange();
    }

}
