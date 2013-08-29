/**
 *
 */
package ru.extas.vaadin.addon.jdocontainer;

import com.google.common.base.Joiner;

import javax.jdo.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.collect.Lists.newArrayListWithCapacity;

/**
 * Упрощает работу с запросами
 *
 * @author Valery Orlov
 */
public class QueryUtils {

    /**
     * Создает строку сортировки для запроса
     *
     * @param q               запрос
     * @param sortPropertyIds поля сортировки
     * @param sortStates      направление сортировки
     */
    public static void setOrdering(final Query q, final Object[] sortPropertyIds, final boolean[] sortStates) {

        String order = "";
        if (sortPropertyIds != null && sortPropertyIds.length != 0 && sortStates != null && sortStates.length != 0) {
            final List<String> sortProps = newArrayListWithCapacity(sortPropertyIds.length);
            int i = 0;
            for (final Object prop : sortPropertyIds) {
                sortProps.add(prop + (sortStates[i] ? " ascending" : " descending"));
                i++;
            }
            order = Joiner.on(",").join(sortProps);
            q.setOrdering(order);
        }
    }

    /**
     * Устанавливает диапазон выборки
     *
     * @param q          запрос
     * @param startIndex начало диапазона
     * @param count      размер выборки
     */
    public static void setRange(final Query q, final int startIndex, final int count) {
        if (startIndex != -1 && count != 0)
            q.setRange(startIndex, startIndex + count);
    }

    /**
     * Преобразует выражение SQL LIKE в регулярное выражение
     *
     * @param like выражение SQL LIKE
     * @return регулярное выражение
     */
    public static String likeToRegex(final String like, final boolean isIgnoreCase) {

        // Quote pattern

        final int len = like.length();
        if (len == 0)
            return "";

        final StringBuilder sb = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            // Экранирование спец. символов
            final char c = like.charAt(i);
            if ("[](){}.*+?$^|#\\".indexOf(c) != -1)
                sb.append("\\");
            sb.append(c);
        }
        if (isIgnoreCase) {
            sb.insert(0, "(?i)");
        }
        String regex = sb.toString();
        regex = regex.replace("_", ".").replace("%", ".*");
        return regex;

    }

    /**
     * Декларирует параметры запроса
     *
     * @param query      запрос
     * @param parameters карта патаметров
     */
    public static void declareParameters(final javax.jdo.Query query, final Map<String, Object> parameters) {
        if (!parameters.isEmpty()) {
            final StringBuilder sb = new StringBuilder(parameters.size() * 20);
            final Iterator<Entry<String, Object>> entries = parameters.entrySet().iterator();
            while (entries.hasNext()) {
                final Entry<String, Object> prm = entries.next();
                sb.append(prm.getValue().getClass().getName());
                sb.append(" ");
                sb.append(prm.getKey());
                if (entries.hasNext())
                    sb.append(", ");
            }
            query.declareParameters(sb.toString());
        }
    }
}
