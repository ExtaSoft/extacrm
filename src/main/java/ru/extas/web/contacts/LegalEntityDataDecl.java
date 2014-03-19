/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;

import java.util.EnumSet;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 */
public class LegalEntityDataDecl extends GridDataDecl {

	public LegalEntityDataDecl() {
		super();
		addMapping("name", "Имя");
		addMapping("company.name", "Компания", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("phone", "Телефон");
		addMapping("email", "E-Mail");
		addMapping("www", "WWW");
		addMapping("actualAddress.region", "Регион");
		super.addCreateModifyMarkers();
	}

}
