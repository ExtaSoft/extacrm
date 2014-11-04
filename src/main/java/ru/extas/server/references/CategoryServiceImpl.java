package ru.extas.server.references;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализация поставщика категорий объектов
 *
 * @author Valery Orlov
 *         Date: 04.11.2014
 *         Time: 13:49
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class CategoryServiceImpl implements CategoryService {

    static private class CompanyCategoriesFactory {
        static final List<String> INSTANCE;

        static {
            INSTANCE = newArrayList(
                    COMPANY_CAT_DEALER,
                    COMPANY_CAT_DISTRIBUTOR,
                    COMPANY_CAT_BANK,
                    COMPANY_CAT_ASSURANCE);
            Collections.sort(INSTANCE);
        }
    }


    @Override
    public Collection<String> loadCompanyCategories() {
        return CompanyCategoriesFactory.INSTANCE;
    }

}
