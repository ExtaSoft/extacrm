package ru.extas.web.lead;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.*;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.motor.MotorColumnGenerator;

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
        addMapping("num", "№", new NumColumnGenerator() {
            @Override
            public void fireClick(Item item) {
                final BeanItem<Lead> curObj = new GridItem<>(item);

                final LeadEditForm editWin = new LeadEditForm("Редактирование лида", curObj, false);
                editWin.showModal();
            }
        }, null);
        addMapping("contactName", "Клиент");
        addMapping("motor_all", "Техника", new MotorColumnGenerator(), null);
        addMapping("motorType", "Тип техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorBrand", "Марка техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorModel", "Модель техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorPrice", "Стоимость техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("contactPhone", "Телефон", PhoneConverter.class);
        addMapping("pointOfSale", "Регион | Мотосалон", new SalePointColumnGenerator("vendor", "pointOfSale", "region"), null);
        addMapping("to_work", "", new ToWorkGenerator(), null);
        addMapping("region", "Регион", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("status", "Статус", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("result", "Результат завершения", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        super.addDefaultMappings();
    }

    private class ToWorkGenerator extends ComponentColumnGenerator {
        @Override
        public Object generateCell(Object columnId, Item item) {
            return new Button("В работу");
        }

    }
}
