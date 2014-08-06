package ru.extas.web.sale;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.GridItem;
import ru.extas.web.commons.NumColumnGenerator;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.lead.SalePointColumnGenerator;
import ru.extas.web.motor.MotorColumnGenerator;

import java.util.EnumSet;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:52
 */
class SaleDataDecl extends GridDataDecl {
	/**
	 * <p>Constructor for SaleDataDecl.</p>
	 */
	public SaleDataDecl() {
        addMapping("num", "№", new NumColumnGenerator() {
            @Override
            public void fireClick(Item item) {
                final BeanItem<Sale> curObj = new GridItem<>(item);

                final SaleEditForm editWin = new SaleEditForm("Редактирование лида", curObj);
                editWin.showModal();
            }
        }, null);
		addMapping("client.name", "Клиент");
        addMapping("motor_all", "Техника", new MotorColumnGenerator(), null);
        addMapping("client.phone", "Телефон", PhoneConverter.class);
        addMapping("motorType", "Тип техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("motorBrand", "Марка техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("motorModel", "Модель техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("motorPrice", "Стоимость техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("dealer.name", "Мотосалон", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("pointOfSale", "Регион | Мотосалон", new SalePointColumnGenerator("dealer", null, "region"), null);
		addMapping("region", "Регион", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		addMapping("result", "Результат завершения", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
		super.addDefaultMappings();
	}
}
