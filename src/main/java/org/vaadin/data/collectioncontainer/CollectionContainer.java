package org.vaadin.data.collectioncontainer;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

import java.util.*;

/**
 * Container implementation for java.util.Collection.
 * <p>
 * This container can be used to wrap Java collections into Vaadin data
 * container. It can be in two ways:
 * <ul>
 * <li>A list of basic types like <code>Integer</code> or <code>String</code> (default).
 * <li>A list of JavaBeans.
 * </ul>
 * In the first case, container contains only single property. The id of the
 * property is <code>CollectionContainer.ITEM_PROPERTY_ID</code>.
 * <p>
 * In the later case a <code>BeanItem</code> is created for each collection
 * element, and the bean properties are used as container properties.
 * <p>
 * The BeanItems are instantiated on-demand, but to initialize the the container
 * properties the first item is created at construction time.
 * <p>
 * Only the properties of the bean (or the <code>ITEM_PROPERTY_ID</code> in the
 * simple case) are included as container properties. Furthermore, the
 * CollectionContainer does not allow addition or removal of container
 * properties.
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.3
 */
public class CollectionContainer implements Container, Container.Ordered,
        Container.Sortable {

    private static final long serialVersionUID = 8723750722557170944L;

    /**
     * Use index of item as item id.
     */
    private static final int ITEM_ID_MODE_INDEX = 1;

    /**
     * Use property value as item id.
     */
    private static final int ITEM_ID_MODE_PROPERTY = 2;

    /**
     * Automatically create new unique id.
     */
    private static final int ITEM_ID_MODE_UNIQUE = 3;

    /**
     * Use objects in collection itself as item id.
     */
    private static final int ITEM_ID_MODE_OBJECT = 4;

    /**
     * The property id of collection objects if they are not treated as beans.
     */
    private static final String ITEM_PROPERTY_ID_OBJECT = "OBJECT";

    /**
     * Mapping from id to instantiated item
     */
    private final Map<Object, Object> items = new HashMap<>();

    /**
     * Mapping from id to original object
     */
    private final Map<Object, Object> objects = new HashMap<>();

    /**
     * The collection which is being wrapped
     */
    private Collection<?> collection = null;

    /**
     * List of properties in this data source
     */
    private final Collection<Object> propertyIds = new ArrayList<>();

    /**
     * Mapping from container (and item) property ids to type
     */
    private final HashMap<Object, Class<?>> types = new HashMap<>();

    /**
     * Should a bean item be instantiated for each item in collection.
     */
    private boolean createBeanItems = false;

    /**
     * Should a collection item be instantiated for each item in collection.
     */
    private boolean createCollectionItems = false;

    /**
     * Storage for generated item IDs
     */
    private ArrayList<Object> itemIds = null;

    /**
     * The id mode of the container.
     */
    private int itemIdMode = ITEM_ID_MODE_UNIQUE;

    /**
     * Internal counter for unique item id generation
     */
    private int counter = 0;

    private final Object idPropertyId;

    private boolean itemIdsInitialized;

    /**
     * Create new CollectionContainer of simple types or JavaBeans. Properties
     * are initialized from the first item.
     *
     * @param collection      The collection to be wrapped.
     * @param createBeanItems Should the items be treated as JavaBeans
     * @param itemIdMode      One of the ITEM_ID_MODE_* constants.
     */
    private CollectionContainer(final Collection<?> collection,
                                final boolean createBeanItems, final int itemIdMode) {
        if (!createBeanItems && itemIdMode == ITEM_ID_MODE_PROPERTY)
            throw new IllegalArgumentException(
                    "Invalid Item id mode: ITEM_ID_MODE_PROPERTY.");
        this.itemIdMode = itemIdMode;
        this.collection = collection;
        this.itemIds = new ArrayList<>(this.collection.size());
        this.createBeanItems = createBeanItems;
        this.idPropertyId = null;
        initializePropertiesFromFirstItem();
    }

    /**
     * Create new CollectionContainer of two dimensional array of primitive
     * types. Properties are initialized from the first item, so the collection
     * cannot be empty.
     * <p>
     * Note that the ITEM_ID_MODE_INDEX is used.
     *
     * @param list The collection of lists to be wrapped.
     */
    private CollectionContainer(final List<Object[]> list, final int idIndex) {
        this.itemIdMode = idIndex < 0 ? ITEM_ID_MODE_INDEX
                : ITEM_ID_MODE_PROPERTY;
        this.idPropertyId = idIndex;
        this.collection = list;
        this.itemIds = new ArrayList<>(this.collection.size());
        this.createBeanItems = false;
        this.createCollectionItems = true;
        initializePropertiesFromFirstItem();
    }

    /**
     * See {@link #CollectionContainer(Collection, boolean, int)}
     *
     * @param items           an array of {@link java.lang.Object} objects.
     * @param createBeanItems a boolean.
     * @param itemIdMode      a int.
     */
    public CollectionContainer(final Object[] items, final boolean createBeanItems,
                               final int itemIdMode) {
        this(Arrays.asList(items), createBeanItems, itemIdMode);
    }

    /**
     * Create new CollectionContainer of JavaBeans and use property as id. This
     * implies ITEM_ID_MODE_PROPERTY and creteBeanitems = true.
     *
     * @param collectionOfBeans The collection to be wrapped.
     * @param idPropertyId      The property name of the bean property used as id.
     */
    private CollectionContainer(final Collection<?> collectionOfBeans,
                                final Object idPropertyId) {
        this.itemIdMode = ITEM_ID_MODE_PROPERTY;
        this.createBeanItems = true;
        this.collection = collectionOfBeans;
        this.itemIds = new ArrayList<>(this.collection.size());
        this.idPropertyId = idPropertyId;
        initializePropertiesFromFirstItem();
    }

    /**
     * See {@link #CollectionContainer(Collection, Object)}.
     *
     * @param arrayOfBeans an array of {@link java.lang.Object} objects.
     * @param idPropertyId a {@link java.lang.Object} object.
     */
    public CollectionContainer(final Object[] arrayOfBeans, final Object idPropertyId) {
        this(Arrays.asList(arrayOfBeans), idPropertyId);
    }

    /**
     * Create new CollectionContainer of simple types. This is useful for
     * collections of String, Integer, Float, etc. Basically when the toString
     * returns something meaningful to user. The itemIdMode is set to
     * ITEM_ID_MODE_INDEX.
     *
     * @param primitives The collection to be wrapped.
     * @return a {@link org.vaadin.data.collectioncontainer.CollectionContainer} object.
     */
    public static CollectionContainer fromPrimitives(final Object[] primitives) {
        return fromPrimitives(Arrays.asList(primitives), false);
    }

    /**
     * Create an indexed collection container from two dimensional data.
     *
     * @param primitives an array of {@link java.lang.Object} objects.
     * @param idIndex    a int.
     * @return a {@link org.vaadin.data.collectioncontainer.CollectionContainer} object.
     */
    public static CollectionContainer fromPrimitives(final Object[][] primitives,
                                                     final int idIndex) {
        return fromPrimitives(Arrays.asList(primitives), idIndex);
    }

    /**
     * <p>fromPrimitives.</p>
     *
     * @param primitives an array of {@link java.lang.Object} objects.
     * @param indexed    a boolean.
     * @return a {@link org.vaadin.data.collectioncontainer.CollectionContainer} object.
     */
    public static CollectionContainer fromPrimitives(final Object[] primitives,
                                                     final boolean indexed) {
        return fromPrimitives(Arrays.asList(primitives), indexed);
    }

    private static CollectionContainer fromPrimitives(final Collection<?> primitives,
                                                      final boolean indexed) {
        return new CollectionContainer(primitives, false,
                indexed ? ITEM_ID_MODE_INDEX : ITEM_ID_MODE_OBJECT);
    }

    private static CollectionContainer fromPrimitives(final List<Object[]> primitives,
                                                      final int idIndex) {
        return new CollectionContainer(primitives, idIndex);
    }

    /**
     * <p>fromBeans.</p>
     *
     * @param beans an array of {@link java.lang.Object} objects.
     * @return a {@link org.vaadin.data.collectioncontainer.CollectionContainer} object.
     */
    public static CollectionContainer fromBeans(final Object[] beans) {
        return fromBeans(Arrays.asList(beans), false);
    }

    /**
     * <p>fromBeans.</p>
     *
     * @param beans   an array of {@link java.lang.Object} objects.
     * @param indexed a boolean.
     * @return a {@link org.vaadin.data.collectioncontainer.CollectionContainer} object.
     */
    public static CollectionContainer fromBeans(final Object[] beans, final boolean indexed) {
        return fromBeans(Arrays.asList(beans), indexed);
    }

    /**
     * <p>fromBeans.</p>
     *
     * @param beans      an array of {@link java.lang.Object} objects.
     * @param idProperty a {@link java.lang.Object} object.
     * @return a {@link org.vaadin.data.collectioncontainer.CollectionContainer} object.
     */
    public static CollectionContainer fromBeans(final Object[] beans,
                                                final Object idProperty) {
        return fromBeans(Arrays.asList(beans), idProperty);
    }

    /**
     * <p>fromBeans.</p>
     *
     * @param beans a {@link java.util.Collection} object.
     * @return a {@link com.vaadin.data.Container} object.
     */
    public static Container fromBeans(final Collection<?> beans) {
        return fromBeans(beans, false);
    }

    private static CollectionContainer fromBeans(final Collection<?> beans,
                                                 final boolean indexed) {
        return new CollectionContainer(beans, true,
                indexed ? ITEM_ID_MODE_INDEX : ITEM_ID_MODE_OBJECT);
    }

    private static CollectionContainer fromBeans(final Collection<?> beans,
                                                 final Object idProperty) {
        return new CollectionContainer(beans, idProperty);
    }

    /**
     * Initializes the Container's properties from the first item in collection.
     */
    private void initializePropertiesFromFirstItem() {
        propertyIds.clear();
        types.clear();

        final Item item = getItem(firstItemId());
        if (item != null) {
            for (final Object propertyId : item.getItemPropertyIds()) {
                final Property property = item.getItemProperty(propertyId);
                propertyIds.add(propertyId);
                types.put(propertyId, property.getType());
            }
        }
    }

    /*
     * @see com.vaadin.data.Container#getItem(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public Item getItem(final Object id) {

        // Handle null
        if (id == null) {
            return null;
        }

        // Check Item cache
        Item i = (Item) this.items.get(id);
        if (i != null || this.itemIdMode == ITEM_ID_MODE_PROPERTY) {
            return i;
        }

        Object obj = null;
        if (this.itemIdMode == ITEM_ID_MODE_INDEX) {
            if (this.collection instanceof List<?>) {
                final int idx = (Integer) id;
                if (idx >= 0 && idx < this.collection.size())
                    obj = ((List<?>) this.collection).get(idx);
            } else {
                final int idx = 0;
                final int intId = (Integer) id;

                for (final Object current : this.collection) {
                    if (idx == intId) {
                        obj = current;
                    }
                }
            }

        } else if (this.itemIdMode == ITEM_ID_MODE_UNIQUE) {
            obj = this.objects.get(id);
        } else if (this.itemIdMode == ITEM_ID_MODE_OBJECT) {
            obj = id;
        }

        if (obj == null)
            return null;

        // Create item for data
        i = createItem(obj);
        this.items.put(id, i);
        return i;

    }

    /**
     * Creates new item to this container.
     *
     * @param obj The object to be used as base.
     */
    Item createItem(final Object obj) {
        if (this.createCollectionItems) {
            if (obj instanceof Item) {
                return (Item) obj;
            } else {
                return new ArrayItem((Object[]) obj);
            }
        } else if (this.createBeanItems) {
            if (obj instanceof Item) {
                return (Item) obj;
            } else {
                return new BeanItem<>(obj);
            }
        } else return new PrimitiveItem(obj);
    }

    /*
     *
     * @see com.vaadin.data.Container#getContainerPropertyIds()
     */

    /** {@inheritDoc} */
    @Override
    public Collection<?> getContainerPropertyIds() {
        return propertyIds;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Try to avoid calling this, as it instantiates all item ids.
     * @see com.vaadin.data.Container#getItemIds()
     */
    @Override
    public Collection<?> getItemIds() {

        // initialize IDs
        if (!this.itemIdsInitialized) {

            this.itemIds.clear();
            this.objects.clear();
            this.items.clear();

            int idx = 0;
            for (final Object obj : collection) {
                initializeItem(idx++, obj);
            }
            this.itemIdsInitialized = true;
        }
        return this.itemIds;
    }

    /**
     * Creates new id for given collection object. The result depends on current
     * itemIdMode.
     *
     * @param index The index of collection object.
     * @param obj   the collection object.
     * @return New item id.
     */
    private synchronized Object getIdForObject(final int index, final Object obj) {
        if (this.itemIdMode == ITEM_ID_MODE_INDEX) {
            return index;
        } else if (this.itemIdMode == ITEM_ID_MODE_OBJECT) {
            return obj;
        } else if (this.itemIdMode == ITEM_ID_MODE_UNIQUE) {
            return this.counter++;
        } else if (this.itemIdMode == ITEM_ID_MODE_PROPERTY
                && obj instanceof Item) {
            final Item item = (Item) obj;
            final Property p = item.getItemProperty(this.idPropertyId);
            if (p == null)
                throw new IllegalArgumentException("Id property '"
                        + this.idPropertyId + "' not found in object (" + index
                        + "): " + obj);
            final Object value = p.getValue();
            if (value == null)
                throw new IllegalArgumentException("Value of id property '"
                        + this.idPropertyId + "' is null in object (" + index
                        + "): " + obj);
            return value;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Get container property for itemId and propertyId. This is equal to:
     * <code>getItem(itemId).getItemPropertyId(propertyId);</code>
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object, java.lang.Object)
     */
    @Override
    public Property getContainerProperty(final Object itemId, final Object propertyId) {
        final Item item = getItem(itemId);
        if (item != null) {
            return item.getItemProperty(propertyId);
        }
        return null;
    }

    /*
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public Class<?> getType(final Object propertyId) {
        return this.types.get(propertyId);
    }

    /**
     * Override the default type. Use this with caution.
     *
     * @param propertyId a {@link java.lang.Object} object.
     * @param type       a {@link java.lang.Class} object.
     */
    public void setType(final Object propertyId, final Class<?> type) {
        this.types.put(propertyId, type);
    }

    /*
     * @see com.vaadin.data.Container#size()
     */

    /** {@inheritDoc} */
    @Override
    public int size() {
        if (!itemIdsInitialized) {
            getItemIds();
        }
        return this.itemIds.size();
    }

    /*
     * @see com.vaadin.data.Container#containsId(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public boolean containsId(final Object id) {
        if (this.itemIdMode == ITEM_ID_MODE_INDEX && id instanceof Integer) {
            final int i = (Integer) id;
            return (i >= 0 && i < this.collection.size());
        } else if (this.itemIdMode == ITEM_ID_MODE_OBJECT) {
            return this.collection.contains(id);
        } else if (this.itemIdsInitialized) {
            return this.itemIds.contains(id);
        } else return this.itemIds != null && this.itemIds.contains(id) || getItemIds().contains(id);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not supported.
     * @see com.vaadin.data.Container#addItem(java.lang.Object)
     */
    @Override
    public Item addItem(final Object object) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Cannot add items CollectionContainer. Update the underlying collection instead.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not supported.
     * @see com.vaadin.data.Container#addItem()
     */
    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Cannot instantiate items to collection.");
    }

    /*
     * @see com.vaadin.data.Container#removeItem(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public boolean removeItem(final Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Item removal not supported");
    }

    /**
     * <p>removeObject.</p>
     *
     * @param id a {@link java.lang.Object} object.
     * @return a boolean.
     * @throws java.lang.UnsupportedOperationException if any.
     */
    public boolean removeObject(final Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Object removal not supported");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not supported.
     * @see com.vaadin.data.Container#addContainerProperty(java.lang.Object, java.lang.Class, java.lang.Object)
     */
    @Override
    public boolean addContainerProperty(final Object arg0, final Class<?> arg1, final Object arg2)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Cannot add container properties to CollectionContainer.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not supported.
     * @see com.vaadin.data.Container#removeContainerProperty(java.lang.Object)
     */
    @Override
    public boolean removeContainerProperty(final Object arg0)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Cannot remove container properties from CollectionContainer.");
    }

    /*
     * @see com.vaadin.data.Container#removeAllItems()
     */

    /** {@inheritDoc} */
    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {

        this.itemIds.clear();
        this.objects.clear();
        this.items.clear();
        this.collection.clear();
        return true;
    }

    /*
     * @see com.vaadin.data.Container.Ordered#nextItemId(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public Object nextItemId(final Object id) {
        /*
         * Four cases: ITEM_ID_MODE_INDEX; ITEM_ID_MODE_OBJECT;
		 * ITEM_ID_MODE_PROPERTY; ITEM_ID_MODE_UNIQUE;
		 */
        if (this.itemIdMode == ITEM_ID_MODE_INDEX && id instanceof Integer) {
            final int idx = (this.itemIdsInitialized ? itemIds.indexOf(id)
                    : (Integer) id) + 1;
            if (idx >= 0 && idx < this.collection.size()) {
                if (this.itemIdsInitialized)
                    return this.itemIds.get(idx);
                return idx;
            }
        } else if (this.itemIdMode == ITEM_ID_MODE_UNIQUE
                || this.itemIdMode == ITEM_ID_MODE_PROPERTY
                || this.itemIdMode == ITEM_ID_MODE_OBJECT) {
            if (id != null && this.itemIdsInitialized) {
                final int index = this.itemIds.indexOf(id);
                if (index >= 0 && index < (this.itemIds.size() - 1))
                    return this.itemIds.get(index + 1);
                return null;
            } else if (id != null && this.collection != null
                    && !this.collection.isEmpty()) {

                // First check the already initialized item ids
                final int index = this.itemIds.indexOf(id);
                if (index >= 0 && index < (this.itemIds.size() - 1)) {
                    return this.itemIds.get(index + 1);
                }

                // Find the next and create ID for it.
                final Object obj = this.objects.get(id);
                int idx = 0;
                for (final Iterator<?> i = this.collection.iterator(); i.hasNext(); idx++) {
                    Object cur = i.next();
                    if (cur != null && cur.equals(obj) && i.hasNext()) {
                        cur = i.next();
                        return initializeItem(idx, cur);
                    }

                }
            }
        }
        return null;
    }

    /*
     * @see com.vaadin.data.Container.Ordered#prevItemId(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public Object prevItemId(final Object id) {
        if (this.itemIdMode == ITEM_ID_MODE_INDEX && id instanceof Integer) {
            final int idx = (Integer) id - 1;
            if (idx >= 0 && idx < this.collection.size()) {
                if (this.itemIdsInitialized)
                    return this.itemIds.get(idx);
                return idx;
            }
        } else if (this.itemIdMode == ITEM_ID_MODE_UNIQUE
                || this.itemIdMode == ITEM_ID_MODE_PROPERTY
                || this.itemIdMode == ITEM_ID_MODE_OBJECT) {
            if (id != null && this.itemIdsInitialized) {
                final int index = this.itemIds.indexOf(id);
                if (index > 0 && index < (this.itemIds.size()))
                    return this.itemIds.get(index - 1);
                return null;
            }
        }
        return null;
    }

    /*
     * @see com.vaadin.data.Container.Ordered#firstItemId()
     */

    /** {@inheritDoc} */
    @Override
    public Object firstItemId() {
        if (this.itemIdsInitialized) {
            return this.itemIds.get(0);
        } else if (this.collection != null && !this.collection.isEmpty()) {
            final Object obj = this.collection.iterator().next();
            return initializeItem(0, obj);
        }
        return null;
    }

    /**
     * Initialize item and store it into mappings.
     *
     * @param obj the collection object
     * @return id of item.
     */
    private Object initializeItem(final int index, final Object obj) {

        if (this.itemIdMode == ITEM_ID_MODE_PROPERTY) {
            final Item item = createItem(obj);
            final Object id = getIdForObject(index, item);
            if (!this.itemIds.contains(id))
                this.itemIds.add(id);
            this.objects.put(id, obj);
            this.items.put(id, item);
            return id;
        } else {
            final Object id = getIdForObject(index, obj);
            if (!this.itemIds.contains(id))
                this.itemIds.add(id);
            this.objects.put(id, obj);
            return id;
        }
    }

    /*
     * @see com.vaadin.data.Container.Ordered#lastItemId()
     */

    /** {@inheritDoc} */
    @Override
    public Object lastItemId() {
        if (this.itemIdsInitialized) {
            return this.itemIds.get(itemIds.size() - 1);
        } else {

            if (this.collection instanceof List<?>) {
                final List<?> list = (List<?>) this.collection;
                final int lastIndex = list.size() - 1;
                return initializeItem(lastIndex, list.get(lastIndex));
            } else if (this.collection != null && !this.collection.isEmpty()) {
                Object last = null;
                int ind = 0;
                for (final Object obj : this.collection) {
                    last = obj;
                    ind++;
                }
                return initializeItem(ind, last);
            }
        }
        return null;
    }

    /*
     * @see com.vaadin.data.Container.Ordered#isFirstId(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public boolean isFirstId(final Object id) {
        final Object first = firstItemId();
        return (id != null && id.equals(first));
    }

    /*
     * @see com.vaadin.data.Container.Ordered#isLastId(java.lang.Object)
     */

    /** {@inheritDoc} */
    @Override
    public boolean isLastId(final Object id) {
        final Object last = lastItemId();
        return (id != null && id.equals(last));
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not supported.
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object)
     */
    @Override
    public Object addItemAfter(final Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Operation not supported by underlying collection.");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not supported.
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object, java.lang.Object)
     */
    @Override
    public Item addItemAfter(final Object afterThis, final Object id)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "addItemAfter: Operation not supported.");
    }

    /**
     * Simple Item wrapper for Java beans / pojo objects.
     */
    public class BeanItem<BT> extends com.vaadin.data.util.BeanItem<BT> {

        private static final long serialVersionUID = 2735024676610840048L;

        private BeanItem(final BT bean) {
            super(bean);
        }
    }

    /**
     * Simple Item wrapper for Java primitive objects.
     */
    public class PrimitiveItem implements Item {

        private static final long serialVersionUID = 2655977009734367009L;

        private Property<Object> property = null;

        /**
         * Create new item from a primitive object.
         *
         * @param object
         */
        private PrimitiveItem(final Object object) {
            this.property = new ObjectProperty<>(object);
        }

        /*
         *
         * @see com.vaadin.data.Item#getItemProperty(java.lang.Object)
         */
        @Override
        public Property getItemProperty(final Object propertyId) {
            if (propertyId != null
                    && propertyId.equals(ITEM_PROPERTY_ID_OBJECT))
                return this.property;
            return null;
        }

        /*
         * @see com.vaadin.data.Item#getItemPropertyIds()
         */
        @Override
        public Collection<?> getItemPropertyIds() {
            return Arrays.asList(ITEM_PROPERTY_ID_OBJECT);
        }

        /**
         * This is not supported.
         *
         * @see com.vaadin.data.Item#addItemProperty(java.lang.Object, com.vaadin.data.Property)
         */
        @Override
        public boolean addItemProperty(final Object id, final Property property)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Operation not supported.");
        }

        /**
         * This is not supported.
         *
         * @see com.vaadin.data.Item#removeItemProperty(java.lang.Object)
         */
        @Override
        public boolean removeItemProperty(final Object id)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Operation not supported.");
        }
    }

    /**
     * Simple Item wrapper for Java primitive objects.
     */
    public class ArrayItem implements Item {

        private static final long serialVersionUID = 6894488972928864826L;

        private final Object[] properties;

        private ArrayList<Integer> propIds;

        /**
         * Create new item from a primitive object array.
         *
         * @param properties
         */
        private ArrayItem(final Object[] properties) {
            this.properties = properties;
        }

        @Override
        public boolean addItemProperty(final Object id, final Property property)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Operation not supported.");
        }

        @Override
        public Property getItemProperty(final Object id) {
            if (id instanceof Integer) {
                final int i = (Integer) id;
                if (i >= 0 && i < properties.length) {
                    return new ObjectProperty<>(properties[i]);
                }
            }
            return null;
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            if (propIds == null) {
                propIds = new ArrayList<>();
                for (int i = 0; i < properties.length; i++) {
                    propIds.add(i);
                }
            }
            return propIds;
        }

        @Override
        public boolean removeItemProperty(final Object id)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Operation not supported.");
        }

    }

    /** {@inheritDoc} */
    @Override
    public void sort(final Object[] propertyId, final boolean[] ascending) {
        if (isNotSortable())
            return;

        // Initialize the itemIds array
        getItemIds();

        // Sort
        Collections.sort(this.itemIds,
                new ItemComparator(propertyId, ascending));
    }

    /** {@inheritDoc} */
    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (isNotSortable()) return null;
        else return getContainerPropertyIds();
    }

    /**
     * Is this container sortable.
     *
     * @return true
     */
    private boolean isNotSortable() {
        return false;
    }

    public class ItemComparator implements Comparator<Object> {

        private final Object[] propertyIds;

        private final boolean[] ascending;

        public ItemComparator(final Object[] propertyIds, final boolean[] ascending) {
            this.propertyIds = propertyIds;
            this.ascending = ascending;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compare(final Object o1, final Object o2) {
            if (propertyIds != null && ascending != null) {
                for (int i = 0; i < propertyIds.length; i++) {
                    final Object pid = propertyIds[i];
                    final int sign = ascending[i] ? 1 : -1;
                    final Item i1 = getItem(o1);
                    final Item i2 = getItem(o2);
                    if (i1 != null && i2 != null) {
                        final Property p1 = i1.getItemProperty(pid);
                        final Property p2 = i2.getItemProperty(pid);
                        if (p1 != null && p2 != null) {
                            final Object v1 = p1.getValue();
                            final Object v2 = p2.getValue();
                            if (v1 != null && v2 != null) {
                                if (v1 instanceof Comparable<?>) {
                                    final int diff = sign
                                            * ((Comparable<Object>) v1)
                                            .compareTo(v2);
                                    if (diff != 0)
                                        return diff;
                                }
                            } else if (v1 != null) {
                                // The first value is not null, so its "bigger"
                                return sign;
                            } else if (v2 != null) {
                                // The second value is not null, so its "bigger"
                                return -sign;
                            }
                        } else if (p1 != null) {
                            // The first property is not null, so its "bigger"
                            return sign;
                        } else if (p2 != null) {
                            // The second property is not null, so its "bigger"
                            return -sign;
                        }
                    } else if (i1 != null) {
                        // The first item is not null, so its "bigger"
                        return sign;
                    } else if (i2 != null) {
                        // The second item is not null, so its "bigger"
                        return -sign;
                    }
                }
            }
            return 0;
        }

    }

    /**
     * Get object from the underlying container by id. If createBeanItems is
     * true, this is shortcut for ((BeanItem)getitem(id)).getBean()
     *
     * @param id
     * @return The POJO associated to the given id.
     */
    Object getObject(final Object id) {
        final Item i = getItem(id);
        if (i instanceof BeanItem<?>) {
            return ((BeanItem<?>) i).getBean();
        } else if (i instanceof PrimitiveItem) {
            return ((PrimitiveItem) i).property.getValue();
        } else if (this.createBeanItems) {
            return i;
        }
        return null;
    }

    /**
     * See {@link #getObject(Object)}.
     *
     * @param id a {@link java.lang.Object} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object getBean(final Object id) {
        return getObject(id);
    }

    /**
     * Returns the underlying collection.
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<?> getCollection() {
        return collection;
    }

}
