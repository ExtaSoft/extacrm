/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.EnumSet;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointsDataDecl extends GridDataDecl {

	/**
	 * <p>Constructor for SalePointsDataDecl.</p>
	 */
	public SalePointsDataDecl() {
		super();
		addMapping("name", "Имя");
        addMapping("company.name", "Компания", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("phone", "Телефон", PhoneConverter.class);
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("regAddress.region", "Регион");
		super.addDefaultMappings();
	}

}
