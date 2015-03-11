package ru.extas.server.references;

import com.google.common.collect.Ordering;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
            INSTANCE = Ordering.natural().immutableSortedCopy(
                    Arrays.asList(
                            COMPANY_CAT_DEALER,
                            COMPANY_CAT_DISTRIBUTOR,
                            COMPANY_CAT_BANK,
                            COMPANY_CAT_ASSURANCE,
                            COMPANY_CAT_CALLCENTER));
        }
    }


    @Override
    public Collection<String> loadCompanyCategories() {
        return CompanyCategoriesFactory.INSTANCE;
    }

}
