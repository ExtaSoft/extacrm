package ru.extas.web.product;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.product.ProductExpenditure;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Строковое представление статусов продукта в продаже
 *
 * @author Valery Orlov
 *         Date: 17.03.2015
 *         Time: 14:36
 */
@Component
public class String2ProductExpenditureType extends String2EnumConverter {

    protected String2ProductExpenditureType() {
        super(ProductExpenditure.Type.class);
    }

    /**
     * <p>createEnum2StringMap.</p>
     *
     * @return a {@link com.google.common.collect.BiMap} object.
     */
    @Override
    protected HashBiMap createEnum2StringMap() {
        final HashBiMap<ProductExpenditure.Type, String> map = HashBiMap.create();
        map.put(ProductExpenditure.Type.INS_KASKO, "Страхование КАСКО");
        map.put(ProductExpenditure.Type.INS_TOTAL_AND_STEALING, "Страхование ТОТАЛ+УГОН");
        map.put(ProductExpenditure.Type.INS_STEALING, "Страхование Кража");
        map.put(ProductExpenditure.Type.INS_OSAGO, "Страхование ОСАГО");
        map.put(ProductExpenditure.Type.TAX_PAY, "Транспортный налог");
        return map;
    }

}
