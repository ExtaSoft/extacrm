package ru.extas.web.commons.container;

import com.vaadin.data.util.filter.Compare;
import ru.extas.model.common.ArchivedObject;
import ru.extas.model.common.AuditedObject;
import ru.extas.model.common.IdentifiedObject;
import ru.extas.web.commons.ArchivedContainer;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * <p>ExtaDataContainer class.</p>
 *
 * @author Valery Orlov
 *         Date: 02.04.2014
 *         Time: 18:11
 * @version $Id: $Id
 * @since 0.3.0
 */
public class ExtaDbContainer<TEntityType extends IdentifiedObject> extends JpaLazyListContainer<TEntityType> implements ArchivedContainer {

    private static final long serialVersionUID = -7891940552175752858L;

    private final Compare.Equal archiveFilter = new Compare.Equal(ArchivedObject.PROPERTY_NAME, false);
    private boolean archiveExcluded;

    /**
     * <p>Constructor for ExtaDataContainer.</p>
     *
     * @param entityClass a {@link java.lang.Class} object.
     */
    public ExtaDbContainer(final Class<TEntityType> entityClass) {
        super(entityClass);

        // Отсекаем архивные записи
        setArchiveExcluded(true);

        // По умолчанию последние изменившиеся записи вверху
        if (AuditedObject.class.isAssignableFrom(entityClass))
            sort(new Object[]{"lastModifiedDate"}, new boolean[]{false});
    }

    @Override
    public boolean isArchiveExcluded() {
        return archiveExcluded;
    }

    @Override
    public void setArchiveExcluded(final boolean archiveExcluded) {
        if (this.archiveExcluded != archiveExcluded && ArchivedObject.class.isAssignableFrom(getEntityClass())) {
            this.archiveExcluded = archiveExcluded;
            if (archiveExcluded)
                addContainerFilter(archiveFilter);
            else
                removeContainerFilter(archiveFilter);
        }
    }

    public List<TEntityType> getEntitiesList() {
        return getBackingList();
    }

    public Set<TEntityType> getEntitiesSet() {
        return newHashSet(getEntitiesList());
    }

}
