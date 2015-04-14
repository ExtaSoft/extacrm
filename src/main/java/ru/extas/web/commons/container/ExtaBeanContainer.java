package ru.extas.web.commons.container;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import ru.extas.model.common.ArchivedObject;
import ru.extas.web.commons.ArchivedContainer;

import java.util.Collection;

/**
 * Контейнер объектов, поддерживающий архивные записи
 *
 * @author Valery Orlov
 *         Date: 23.11.2014
 *         Time: 20:30
 */
public class ExtaBeanContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE> implements ArchivedContainer {

    private final Compare.Equal archiveFilter = new Compare.Equal(ArchivedObject.PROPERTY_NAME, false);
    private boolean archiveExcluded;

    public ExtaBeanContainer(final Class<? super BEANTYPE> type) throws IllegalArgumentException {
        super(type);
        // Отсекаем архивные записи
        setArchiveExcluded(true);
    }

    public ExtaBeanContainer(final Class<? super BEANTYPE> type, final Collection<? extends BEANTYPE> collection) throws IllegalArgumentException {
        super(type, collection);
        // Отсекаем архивные записи
        setArchiveExcluded(true);
    }

    @Override
    public boolean isArchiveExcluded() {
        return archiveExcluded;
    }

    @Override
    public void setArchiveExcluded(final boolean archiveExcluded) {
        if (this.archiveExcluded != archiveExcluded && ArchivedObject.class.isAssignableFrom(getBeanType())) {
            this.archiveExcluded = archiveExcluded;
            if (archiveExcluded)
                addContainerFilter(archiveFilter);
            else
                removeContainerFilter(archiveFilter);
        }
    }
}
