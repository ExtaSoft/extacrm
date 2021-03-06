package ru.extas.web.product;

import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.StringToPercentConverter;

/**
 * Декларация данных продукта "Рассрочка"
 *
 * @author Valery Orlov
 *         Date: 19.01.14
 *         Time: 12:52
 */
class ProdHirePurchaseDataDecl extends GridDataDecl {
	/**
	 * <p>Constructor for ProdInstallmentsDataDecl.</p>
	 */
	public ProdHirePurchaseDataDecl() {
		addMapping("name", "Название продукта");
		addMapping("vendor.name", "Банк");
		addMapping("maxPeriod", "Период рассрочки");
		addMapping("minDownpayment", "Первоначальный взнос", StringToPercentConverter.class);
		addMapping("active", "Активный продукт");
		super.addDefaultMappings();
	}
}
