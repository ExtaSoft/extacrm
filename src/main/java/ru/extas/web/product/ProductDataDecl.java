package ru.extas.web.product;

import ru.extas.web.commons.GridDataDecl;

/**
 * Метаинформация для показа полного (общего) списка продуктов
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 17:05
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for ProductDataDecl.</p>
	 */
	public ProductDataDecl() {
		addMapping("name", "Название продукта");
		addMapping("vendor.name", "Банк/СК");
		addMapping("active", "Активный продукт");
		super.addCreateModifyMarkers();
	}
}
