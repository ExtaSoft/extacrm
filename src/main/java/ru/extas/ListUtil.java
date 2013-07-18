package ru.extas;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

/**
 * 
 * @author Valery Orlov
 * 
 */
public class ListUtil {

	/**
	 * Checks if {@link List} is null or empty.
	 * 
	 * @param <E>
	 *            the generic type
	 * @param list
	 *            the list
	 * @return true, if is null or empty
	 */
	public static <E> boolean isNullOrEmpty(List<E> list) {
		return list == null || list.size() == 0;
	}

	/**
	 * Checks if {@link List} is not null and empty.
	 * 
	 * @param <E>
	 *            the generic type
	 * @param list
	 *            the list
	 * @return true, if is not null and empty
	 */
	public static <E> boolean isNotNullAndEmpty(List<E> list) {
		return list != null && list.size() != 0;
	}

	/**
	 * Helper method to return an empty list if provided one is null.
	 * 
	 * @param list
	 *            the list
	 * @return the provided list or an empty one if it was null
	 */
	public static <T> List<T> emptyIfNull(List<T> list) {
		if (list == null) {
			return newArrayList();
		}
		return list;
	}
}