/**
 * Copyright 2010 Tommi S.E. Laukkanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.extas.vaadin.addon.jdocontainer;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lazy ID list implementation used in LazyQueryContainer.getItemIds
 * method. Wraps LazyQueryView and gets item ID's from the view on demand.
 *
 * @param <T> the id class
 * @author Tommi Laukkanen
 */
public final class LazyBeanList<T> extends AbstractList<T> implements Serializable {
    /**
     * Java serialization version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The composite LazyQueryView.
     */
    private final LazyQueryView lazyQueryView;
    /**
     * Map containing index to item ID mapping for IDs already loaded through this list.
     */
    private final Map<Object, Integer> idIndexMap = new HashMap<>();

    /**
     * Constructor which sets composite LazyQueryView and ID of the item ID property.
     *
     * @param lazyQueryView the LazyQueryView where IDs can be read from.
     */
    public LazyBeanList(final LazyQueryView lazyQueryView) {
        this.lazyQueryView = lazyQueryView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return lazyQueryView.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized T[] toArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("hiding")
    @Override
    public <T> T[] toArray(final T[] a) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T get(final int index) {
        if (index < 0 || index >= lazyQueryView.size()) {
            throw new IndexOutOfBoundsException();
        }
        final T itemId = ((BeanItem<T>) lazyQueryView.getItem(index)).getBean();
        // Do not put added item ids to id index map and make sure that
        // existing item indexes start from 0 i.e. ignore added items as they
        // are compensated for in indexOf method.
        final int addedItemSize = lazyQueryView.getAddedItems().size();
        if (index >= addedItemSize) {
            idIndexMap.put(itemId, index - addedItemSize);
        }
        return itemId;
    }

    /**
     * {@inheritDoc}
     */
    public Integer set(final int index, final Integer element) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public int indexOf(final Object o) {
        if (o == null) {
            return -1;
        }
        // Brute force added items first. There should only be a few.
        final List<Item> addedItems = lazyQueryView.getAddedItems();
        for (int i = 0; i < addedItems.size(); i++) {
            if (o.equals(((BeanItem<T>) addedItems.get(i)).getBean())) {
                return i;
            }
        }
        // Check from mapping cache.
        if (idIndexMap.containsKey(o)) {
            return addedItems.size() + idIndexMap.get(o);
        }
        // Switching to brute forcing.
        for (int i = addedItems.size(); i < lazyQueryView.size(); i++) {
            if (o.equals(((BeanItem<T>) lazyQueryView.getItem(i)).getBean())) {
                return i;
            }
        }
        // Not found.
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }
}
