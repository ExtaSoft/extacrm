package ru.extas.vaadin.addon.jdocontainer;

import com.vaadin.data.util.BeanItem;

/**
 * LazyJdoContainer enables using JPA entities with lazy batch loading, filter, sort
 * and buffered writes.
 * 
 * @param <E>
 *            Jdo class.
 * @author Valery Orlov
 */
public final class LazyJdoContainer<E> extends LazyQueryContainer {
	/**
	 * Java serialization version UID.
	 */
	private static final long serialVersionUID = 1L;
	private Class<E> entityClass;

	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 * 
	 * @param pm
	 *            The JPA JdoManager.
	 * @param entityClass
	 *            The entity class.
	 * @param idPropertyId
	 *            The ID of the ID property or null if item index is used as ID.
	 * @param batchSize
	 *            The batch size.
	 * @param applicationManagedTransactions
	 *            True if application manages transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from PersistenceContext.
	 */
	public LazyJdoContainer(final Class<E> entityClass, final int batchSize, final Object idPropertyId) {
		super(new JdoQueryDefinition<E>(entityClass, batchSize, idPropertyId),
				new JdoQueryFactory());
		this.entityClass = entityClass;
	}

	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 * 
	 * @param pm
	 *            The JPA JdoManager.
	 * @param applicationManagedTransactions
	 *            True if application manages
	 *            transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from
	 *            PersistenceContext.
	 *            items until commit.
	 * @param compositeItems
	 *            True if native items should be wrapped to
	 *            CompositeItems.
	 * @param entityClass
	 *            The entity class.
	 * @param batchSize
	 *            The batch size.
	 * @param nativeSortPropertyIds
	 *            Properties participating in the native sort.
	 * @param nativeSortPropertyAscendingStates
	 *            List of property sort directions
	 *            for the native sort.
	 * @param idPropertyId
	 *            Property containing the property ID.
	 */
	public LazyJdoContainer(final Class<E> entityClass, final int batchSize,
							final Object[] nativeSortPropertyIds, final boolean[]
							nativeSortPropertyAscendingStates,
							final Object idPropertyId) {
		super(new JdoQueryDefinition<E>(entityClass, batchSize, idPropertyId),
				new JdoQueryFactory());
		getQueryView().getQueryDefinition().setDefaultSortState(nativeSortPropertyIds,
				nativeSortPropertyAscendingStates);
	}

	/**
	 * Adds entity to the container as first item i.e. at index 0.
	 * 
	 * @return the new constructed entity.
	 */
	public E addJdoEntity() {
		final Object itemId = addItem();
		return getJdoEntity(indexOfId(itemId));
	}

	/**
	 * Removes given entity at given index and returns it.
	 * 
	 * @param index
	 *            Index of the entity to be removed.
	 * @return The removed entity.
	 */
	public E removeJdoEntity(final int index) {
		final E entityToRemove = getJdoEntity(index);
		removeItem(getIdByIndex(index));
		return entityToRemove;
	}

	/**
	 * Gets entity by ID.
	 * 
	 * @param id
	 *            The ID of the entity.
	 * @return the entity.
	 */
	public E getJdoEntity(final Object id) {
		return getJdoEntity(indexOfId(id));
	}

	/**
	 * Gets entity at given index.
	 * 
	 * @param index
	 *            The index of the entity.
	 * @return the entity.
	 */
	@SuppressWarnings("unchecked")
	public E getJdoEntity(final int index) {
		return ((BeanItem<E>)getItem(getIdByIndex(index))).getBean();
	}

	/**
	 * @return the entityClass
	 */
	public Class<E> getEntityClass() {
		return entityClass;
	}

}
