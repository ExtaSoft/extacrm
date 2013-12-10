/**
 *
 */
package ru.extas.web.contacts;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отображения контактов в списке
 *
 * @author Valery Orlov
 */
public class CompanyDataDecl extends GridDataDecl {

    public CompanyDataDecl() {
        super();
        addMapping("name", "Имя");
        addMapping("fullName", "Полное Наименование");
        addMapping("cellPhone", "Телефон");
        addMapping("email", "E-Mail");
        addMapping("actualAddress.region", "Регион");
        super.addCreateModifyMarkers();
    }

}
