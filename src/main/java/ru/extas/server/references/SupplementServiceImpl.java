/**
 *
 */
package ru.extas.server.references;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Заполнение простых справочников
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class SupplementServiceImpl implements SupplementService {
    // TODO Перенести данные в базу или в кэш

    static private class DocumentTypesFactory {
        static final List<String> INSTANCE;

        static {
            INSTANCE = newArrayList("Паспорт", "ПТС", "СТС", "Загранпаспорт", "Полис ДМС", "Справка 2НДФЛ",
                    "Водительское удостоверение", "СНИЛС", "Трудовая книжка", "Справка о доходах (по форме банка)");
            Collections.sort(INSTANCE);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> loadDocumentTypes() {
        return DocumentTypesFactory.INSTANCE;
    }

}
