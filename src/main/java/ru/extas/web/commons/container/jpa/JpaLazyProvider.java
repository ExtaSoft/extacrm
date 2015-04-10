package ru.extas.web.commons.container.jpa;

import ru.extas.model.common.IdentifiedObject;

import java.io.Serializable;
import java.util.*;

/**
 * @author Valery Orlov
 *         Date: 10.04.2015
 *         Time: 17:28
 */
public class JpaLazyProvider<TEntityType extends IdentifiedObject> extends AbstractList<TEntityType> implements Serializable {

    // Vaadin table by default has 15 rows, 2x that to cache up an down
    // With this setting it is maximum of 2 requests that happens. With
    // normal scrolling just 0-1 per user interaction
    public static final int DEFAULT_PAGE_SIZE = 15 + 15 * 2;

    private List<TEntityType> currentPage;
    private List<TEntityType> prevPage;
    private List<TEntityType> nextPage;

    private int pageIndex = -10;
    private final int pageSize = DEFAULT_PAGE_SIZE;



    @Override
    public TEntityType get(final int index) {
        final int pageIndexForReqest = index / pageSize;
        final int indexOnPage = index % pageSize;

        // Find page from cache
        List<TEntityType> page = null;
        if (pageIndex == pageIndexForReqest) {
            page = currentPage;
        } else if (pageIndex - 1 == pageIndexForReqest && prevPage != null) {
            page = prevPage;
        } else if (pageIndex + 1 == pageIndexForReqest && nextPage != null) {
            page = nextPage;
        }

        if (page == null) {
            // Page not in cache, change page, move next/prev is feasible
            if (pageIndexForReqest - 1 == pageIndex) {
                // going to next page
                prevPage = currentPage;
                currentPage = nextPage;
                nextPage = null;
            } else if (pageIndexForReqest + 1 == pageIndex) {
                // going to previous page
                nextPage = currentPage;
                currentPage = prevPage;
                prevPage = null;
            } else {
                currentPage = null;
                prevPage = null;
                nextPage = null;
            }
            pageIndex = pageIndexForReqest;
            if (currentPage == null) {
                currentPage = findEntities(pageIndex * pageSize);
            }
            if (currentPage == null) {
                return null;
            } else {
                page = currentPage;
            }
        }
        final TEntityType get = page.get(indexOnPage);
        return get;
    }

    protected List findEntities(int i) {
        return pageProvider.findEntities(i);
    }

    private Integer cachedSize;

    @Override
    public int size() {
        if (cachedSize == null) {
            cachedSize = countProvider.size();
        }
        return cachedSize;
    }

    private transient WeakHashMap<TEntityType, Integer> indexCache;

    private Map<TEntityType, Integer> getIndexCache() {
        if (indexCache == null) {
            indexCache = new WeakHashMap<TEntityType, Integer>();
        }
        return indexCache;
    }

    @Override
    public int indexOf(Object o) {
        // optimize: check the buffers first
        Integer indexViaCache = getIndexCache().get(o);
        if (indexViaCache != null) {
            return indexViaCache;
        } else if (currentPage != null) {
            int idx = currentPage.indexOf(o);
            if (idx != -1) {
                indexViaCache = pageIndex * pageSize + idx;
            }
        }
        if (indexViaCache == null && prevPage != null) {
            int idx = prevPage.indexOf(o);
            if (idx != -1) {
                indexViaCache = (pageIndex - 1) * pageSize + idx;
            }
        }
        if (indexViaCache == null && nextPage != null) {
            int idx = nextPage.indexOf(o);
            if (idx != -1) {
                indexViaCache = (pageIndex + 1) * pageSize + idx;
            }
        }
        if (indexViaCache != null) {
            /*
             * In some cases (selected value) components like Vaadin combobox calls this, then stuff from elsewhere with indexes and
             * finally again this method with the same object (possibly on other page). Thus, to avoid heavy iterating,
             * cache the location.
             */
            getIndexCache().put((TEntityType) o, indexViaCache);
            return indexViaCache;
        }
        // fall back to iterating, this will most likely be sloooooow....
        // If your app gets here, consider overwriting this method, and to
        // some optimization at service/db level
        return super.indexOf(o);
    }

    @Override
    public boolean contains(Object o) {
        // Although there would be the indexed version, vaadin sometimes calls this
        // First check caches, then fall back to sluggish iterator :-(
        if (getIndexCache().containsKey(o)) {
            return true;
        } else if (currentPage != null && currentPage.contains(o)) {
            return true;
        } else if (prevPage != null && prevPage.contains(o)) {
            return true;
        } else if (nextPage != null && nextPage.contains(o)) {
            return true;
        }
        return super.contains(o);
    }

    @Override
    public Iterator<TEntityType> iterator() {
        return new Iterator<TEntityType>() {

            private int index = -1;
            private final int size = size();

            @Override
            public boolean hasNext() {
                return index + 1 < size;
            }

            @Override
            public TEntityType next() {
                index++;
                return get(index);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    /**
     * Resets buffers used by the LazyList.
     */
    public void reset() {
        currentPage = null;
        prevPage = null;
        nextPage = null;
        pageIndex = -10;
        cachedSize = null;
        if (indexCache != null) {
            indexCache.clear();
        }
    }

}
