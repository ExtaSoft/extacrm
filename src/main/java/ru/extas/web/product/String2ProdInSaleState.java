package ru.extas.web.product;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.sale.ProductInSale;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Строковое представление статусов продукта в продаже
 *
 * @author Valery Orlov
 *         Date: 17.03.2015
 *         Time: 14:36
 */
@Component
public class String2ProdInSaleState extends String2EnumConverter {

    protected String2ProdInSaleState() {
        super(ProductInSale.State.class);
    }

    /**
     * <p>createEnum2StringMap.</p>
     *
     * @return a {@link com.google.common.collect.BiMap} object.
     */
    @Override
    protected HashBiMap createEnum2StringMap() {
        final HashBiMap<ProductInSale.State, String> map = HashBiMap.create();
        map.put(ProductInSale.State.IN_PROGRESS, "На рассмотрении");
        map.put(ProductInSale.State.AGREED, "Одобрен");
        map.put(ProductInSale.State.REJECTED, "Отклонен");
        map.put(ProductInSale.State.SOLD_OUT, "Сделка оформлена");
        map.put(ProductInSale.State.NEW, "Новая заявка");
        return map;
    }

}
