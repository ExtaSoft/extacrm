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
	/**
	 * <p>Constructor for ProdCreditDataDecl.</p>
	 */
	public ProdCreditDataDecl() {
		addMapping("name", "Название продукта");
		addMapping("vendor.name", "Банк");
		addMapping("programType", "Тип программы");
		addMapping("minSum", "Мин.сумма");
		addMapping("maxSum", "Макс.сумма");
		addMapping("minDownpayment", "Мин.взнос", StringToPercentConverter.class);
		addMapping("maxDownpayment", "Макс.взнос", StringToPercentConverter.class);
		addMapping("minPeriod", "Мин.срок");
		addMapping("maxPeriod", "Макс.срок");
		addMapping("step", "Шаг кредита");
		addMapping("dealerSubsidy", "Субсидия", StringToPercentConverter.class);
		addMapping("active", "Активный продукт");
		super.addDefaultMappings();
	}
}
