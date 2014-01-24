package ru.extas.web.product;

import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.StringToPercentConverter;

/**
 * Декларация данных продукта "Кредит"
 *
 * @author Valery Orlov
 *         Date: 19.01.14
 *         Time: 12:52
 */
class ProdCreditDataDecl extends GridDataDecl {
	public ProdCreditDataDecl() {
		addMapping("name", "Название продукта");
		addMapping("vendor.name", "Банк");
		addMapping("percent", "Процент по кредиту", StringToPercentConverter.class);
		addMapping("maxPeroid", "Срок кредитования");
		addMapping("minDownpayment", "Первоначальный взнос", StringToPercentConverter.class);
		addMapping("maxSum", "Сумма кредита");
		addMapping("active", "Активный продукт");
		super.addCreateModifyMarkers();
	}
}
