package ru.extas.web.commons;

import com.vaadin.data.util.BeanItemContainer;

import java.util.Collection;

/**
 * <p>RefreshBeanContainer class.</p>
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
public class RefreshBeanContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE> {

    /**
     * <p>Constructor for RefreshBeanContainer.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @throws java.lang.IllegalArgumentException if any.
     */
    public RefreshBeanContainer(Class<? super BEANTYPE> type) throws IllegalArgumentException {
        super(type);
    }

    /**
     * <p>Constructor for RefreshBeanContainer.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param collection a {@link java.util.Collection} object.
     * @throws java.lang.IllegalArgumentException if any.
     */
    public RefreshBeanContainer(Class<? super BEANTYPE> type, Collection<? extends BEANTYPE> collection) throws IllegalArgumentException {
        super(type, collection);
    }

    /**
     * <p>refreshItems.</p>
     */
    public void refreshItems() {
        fireItemSetChange();
    }

}
