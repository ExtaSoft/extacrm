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
class ContactDataDecl extends GridDataDecl {

    public ContactDataDecl() {
        super();
        addMapping("name", "Имя");
        addMapping("birthday", "Дата рождения"/* , StringToJodaLDConverter.class */);
        addMapping("sex", "Пол");
        addMapping("cellPhone", "Мобильный телефон");
        addMapping("email", "E-Mail");
        addMapping("region", "Регион");
        super.addCreateModifyMarkers();
    }

}
