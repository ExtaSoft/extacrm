package ru.extas.web.product;

import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.StringToPercentConverter;

/**
 * Декларация данных продукта "Страховка"
 *
 * @author Valery Orlov
 *         Date: 19.01.14
 *         Time: 12:52
 */
class ProdInsuranceDataDecl extends GridDataDecl {
	/**
	 * <p>Constructor for ProdInsuranceDataDecl.</p>
	 */
	public ProdInsuranceDataDecl() {
		addMapping("name", "Название продукта");
		addMapping("vendor.name", "Страховщик");
		addMapping("percent", "Страх. премия", StringToPercentConverter.class);
		addMapping("active", "Активный продукт");
		super.addDefaultMappings();
	}
}
