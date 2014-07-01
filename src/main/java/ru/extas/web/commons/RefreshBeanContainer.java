package ru.extas.web.commons;

import com.vaadin.data.util.BeanItemContainer;

import java.util.Collection;

public class RefreshBeanContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE> {

    public RefreshBeanContainer(Class<? super BEANTYPE> type) throws IllegalArgumentException {
        super(type);
    }

    public RefreshBeanContainer(Class<? super BEANTYPE> type, Collection<? extends BEANTYPE> collection) throws IllegalArgumentException {
        super(type, collection);
    }

    public void refreshItems() {
        fireItemSetChange();
    }

}