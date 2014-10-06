package ru.extas.web.contacts;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.List;

/**
 * Вспомогательные методы работы с именем
 *
 * @author Valery Orlov
 *         Date: 06.10.2014
 *         Time: 17:08
 */
public class NameUtils {

    /**
     * Возврашает только фамилию и имя.
     *
      * @param name полное имя (ФИО)
     * @return фамилия и имя
     */
    public static String getShortName(String name) {
        String shortName;
        final List<String> nameParts = Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .splitToList(name);
        if (nameParts.size() > 1)
            shortName = Joiner.on(' ').join(nameParts.get(0), nameParts.get(1));
        else
            shortName = name;
        return shortName;
    }
}
