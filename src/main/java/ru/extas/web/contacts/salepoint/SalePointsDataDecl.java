/**
 *
 */
package ru.extas.web.contacts.salepoint;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.EmailLinkColumnGen;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.UrlLinkColumnGen;
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
        addMapping("phone", "Телефон", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), PhoneConverter.class);
		addMapping("email", "E-Mail", new EmailLinkColumnGen(), EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("www", "WWW", new UrlLinkColumnGen(), EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("regAddress.region", "Регион");
		addMapping("regAddress.city", "Город", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("regAddress.streetBld", "Адрес");
		super.addDefaultMappings();
	}

}
