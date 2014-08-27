package ru.extas;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 */
class ListUtil {

    /**
     * Checks if {@link java.util.List} is null or empty.
     *
     * @param <E>  the generic type
     * @param list the list
     * @return a boolean.
     */
    public static <E> boolean isNullOrEmpty(final List<E> list) {
        return list == null || list.size() == 0;
    }

    /**
     * Checks if {@link java.util.List} is not null and empty.
     *
     * @param <E>  the generic type
     * @param list the list
     * @return a boolean.
     */
    public static <E> boolean isNotNullAndEmpty(final List<E> list) {
        return list != null && list.size() != 0;
    }

    /**
     * Helper method to return an empty list if provided one is null.
     *
     * @param list the list
     * @return the provided list or an empty one if it was null
     * @param <T> a T object.
     */
    public static <T> List<T> emptyIfNull(final List<T> list) {
        if (list == null) {
            return newArrayList();
        }
        return list;
    }
}
