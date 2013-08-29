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
class CompanyDataDecl extends GridDataDecl {

    public CompanyDataDecl() {
        super();
        addMapping("name", "Имя");
        addMapping("companyInfo.fullName", "Полное Наименование");
        addMapping("cellPhone", "Телефон");
        addMapping("email", "E-Mail");
        addMapping("actualAddress.region", "Регион");
        super.addCreateModifyMarkers();
    }

}
