package ru.extas.web.lead;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import ru.extas.model.lead.Lead;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.contacts.Name2ShortNameConverter;
import ru.extas.web.motor.MotorColumnGenerator;

import java.util.EnumSet;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:52
 */
class LeadDataDecl extends GridDataDecl {

    /**
     * <p>Constructor for LeadDataDecl.</p>
     */
    public LeadDataDecl(final LeadsGrid grid) {
        addMapping("num", "№", new NumColumnGenerator() {
            @Override
            public void fireClick(final Item item) {
                final Lead curObj = GridItem.extractBean(item);
                grid.doEditObject(curObj);
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
        addMapping("region", "Регион", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("responsible.name", "Ответственный", getPresentFlags(true), Name2ShortNameConverter.class);
        addMapping("responsibleAssist.name", "Заместитель", getPresentFlags(true), Name2ShortNameConverter.class);
        addMapping("status", "Статус", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        super.addDefaultMappings();
        if (grid.getStatus() == Lead.Status.NEW && lookup(UserManagementService.class).isItOurUser()) {
            addMapping("to_work", "В работу", new ComponentColumnGenerator() {
                @Override
                public Object generateCell(final Object columnId, final Item item, final Object itemId) {
                    final Button button = new Button("В работу", Fontello.CHECK_2);
                    button.addStyleName(ExtaTheme.BUTTON_SMALL);
                    button.addClickListener(e -> grid.doQualifyLead(itemId));
                    return button;
                }
            }, null);
        }
    }

}
