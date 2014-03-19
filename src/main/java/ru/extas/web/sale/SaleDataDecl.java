package ru.extas.web.sale;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;

import java.util.EnumSet;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:52
 */
class SaleDataDecl extends GridDataDecl {
	/**
	 * <p>Constructor for SaleDataDecl.</p>
	 */
	public SaleDataDecl() {
		addMapping("client.name", "Клиент");
		addMapping("motorType", "Тип техники");
		addMapping("motorBrand", "Марка техники");
		addMapping("motorModel", "Модель техники");
		addMapping("motorPrice", "Стоимость техники");
		addMapping("dealer.name", "Мотосалон");
		addMapping("region", "Регион");
		addMapping("result", "Результат завершения", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		super.addCreateModifyMarkers();
	}
}
