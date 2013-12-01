package ru.extas.web.lead;

import ru.extas.web.commons.GridDataDecl;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:52
 */
class LeadDataDecl extends GridDataDecl {
    public LeadDataDecl() {
        addMapping("contactName", "Имя контакта");
        addMapping("motorType", "Тип техники");
        addMapping("motorBrand", "Марка техники");
        addMapping("motorModel", "Модель техники");
        addMapping("motorPrice", "Стоимость техники");
        addMapping("pointOfSale", "Мотосалон");
        addMapping("region", "Регион");
        addMapping("status", "Статус");
        super.addCreateModifyMarkers();
    }
}
