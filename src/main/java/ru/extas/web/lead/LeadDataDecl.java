package ru.extas.web.lead;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.GridItem;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.commons.converters.StringToJodaDTConverter;
import ru.extas.web.contacts.SalePointEditForm;

import java.text.MessageFormat;
import java.util.EnumSet;

import static ru.extas.web.commons.GridItem.extractBean;

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
        addMapping("num", "№", new LeadNumGenerator(), null);
        addMapping("contactName", "Клиент");
        addMapping("motor_all", "Техника", new MotorGenerator(), null);
        addMapping("motorType", "Тип техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorBrand", "Марка техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorModel", "Модель техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorPrice", "Стоимость техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("contactPhone", "Телефон", PhoneConverter.class);
        addMapping("pointOfSale", "Регион | Мотосалон", new SalePointGenerator(), null);
        addMapping("to_work", "", new ToWorkGenerator(), null);
        addMapping("region", "Регион", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("status", "Статус", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("result", "Результат завершения", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        super.addDefaultMappings();
    }

    private class LeadNumGenerator extends ComponentColumnGenerator {

        private StringToJodaDTConverter dtConverter = new StringToJodaDTConverter("EEE, dd MMM, HH:mm");

        @Override
        public Object generateCell(Object columnId, final Item item) {
            Lead lead = extractBean(item);
            VerticalLayout cell = new VerticalLayout();
            Button link = new Button(lead.getNum().toString(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    final BeanItem<Lead> curObj = new GridItem<>(item);

                    final LeadEditForm editWin = new LeadEditForm("Редактирование лида", curObj, false);
                    editWin.showModal();

                }
            });
            link.addStyleName("link");
            cell.addComponent(link);
            //cell.setComponentAlignment(link, Alignment.TOP_RIGHT);
            final Label createdAt = new Label(item.getItemProperty("createdAt"));
            createdAt.setConverter(dtConverter);
            cell.addComponent(createdAt);
            //cell.setComponentAlignment(createdAt, Alignment.BOTTOM_RIGHT);
            return cell;
        }
    }

    private class MotorGenerator extends ComponentColumnGenerator {
        @Override
        public Object generateCell(Object columnId, Item item) {
            Lead lead = extractBean(item);
            return new Label(
                    MessageFormat.format("<strong>{0}</strong><br/>{1} | {2}",
                            lead.getMotorType(),
                            lead.getMotorBrand(),
                            lead.getMotorModel()),
                    ContentMode.HTML);
        }

    }

    private class SalePointGenerator extends ComponentColumnGenerator {
        @Override
        public Object generateCell(Object columnId, Item item) {
            final Lead lead = extractBean(item);
            String region;
            Button link = new Button();
            link.addStyleName("link");
            if (lead.getVendor() != null) {
                link.setCaption(lead.getVendor().getName());
                link.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        final BeanItem<SalePoint> curObj = new BeanItem<>(lead.getVendor());

                        final SalePointEditForm editWin = new SalePointEditForm("Просмотр торговой точки", curObj);
                        editWin.showModal();

                    }
                });
                region = lead.getVendor().getActualAddress().getRegion();
            } else {
                region = lead.getRegion();
                link.setCaption(lead.getPointOfSale());
                link.setEnabled(false);
            }
            VerticalLayout cell = new VerticalLayout();
            cell.addComponent(new Label(region));
            cell.addComponent(link);
            return cell;
        }
    }

    private class ToWorkGenerator extends ComponentColumnGenerator {
        @Override
        public Object generateCell(Object columnId, Item item) {
            return new Button("В работу");
        }

    }
}
