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
class PersonDataDecl extends GridDataDecl {

    public PersonDataDecl() {
        super();
        addMapping("name", "Имя");
        addMapping("birthday", "Дата рождения"/* , StringToJodaLDConverter.class */);
        addMapping("sex", "Пол");
        addMapping("cellPhone", "Мобильный телефон");
        addMapping("email", "E-Mail");
        addMapping("actualAddress.region", "Регион");
        super.addCreateModifyMarkers();
    }

}
