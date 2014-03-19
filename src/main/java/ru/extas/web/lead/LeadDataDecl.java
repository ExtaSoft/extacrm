package ru.extas.web.lead;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;

import java.util.EnumSet;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:52
 */
class LeadDataDecl extends GridDataDecl {
    /**
     * <p>Constructor for LeadDataDecl.</p>
     */
    public LeadDataDecl() {
        addMapping("contactName", "Имя контакта");
        addMapping("motorType", "Тип техники");
        addMapping("motorBrand", "Марка техники");
        addMapping("motorModel", "Модель техники");
        addMapping("motorPrice", "Стоимость техники");
        addMapping("pointOfSale", "Мотосалон");
        addMapping("region", "Регион");
        addMapping("status", "Статус");
        addMapping("result", "Результат завершения", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        super.addCreateModifyMarkers();
    }
}
