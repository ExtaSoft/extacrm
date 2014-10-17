package ru.extas.web.insurance;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Label;
import org.joda.time.LocalDate;
import ru.extas.model.contacts.Person;
import ru.extas.model.insurance.Insurance;
import ru.extas.web.commons.DataDeclMapping.PresentFlag;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.GridItem;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.EnumSet;

/**
 * Опции отрбражения страховок
 *
 * @author Valery Orlov
 */
class InsuranceDataDecl extends GridDataDecl {

    /** Constant <code>CLIENT_BIRTHDAY="client_birthday"</code> */
    public static final String CLIENT_BIRTHDAY = "client_birthday";

    /**
     * <p>Constructor for InsuranceDataDecl.</p>
     */
    public InsuranceDataDecl() {
        super();
        addMapping("regNum", "Номер полиса");
        addMapping("a7Num", "Квитанция А-7", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("date", "Дата договора");
        addMapping("clientName", "Клиент");
        addMapping("clientPhone", "Телефон", EnumSet.of(PresentFlag.COLLAPSED), PhoneConverter.class);
        addMapping(CLIENT_BIRTHDAY, "Дата рождения", new ClientBirthdayGenerator(), EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("beneficiary", "Выгодопреобретатель", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("usedMotor", "Б/у", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("motorType", "Тип техники");
        addMapping("motorBrand", "Марка техники");
        addMapping("motorModel", "Модель техники");
        addMapping("motorVin", "VIN", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("saleNum", "№ дог.купли/продажи", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("saleDate", "Дата дог.купли/продажи", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("riskSum", "Страховая сумма");
        addMapping("coverTime", "Период страхования", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("premium", "Страховая премия");
        addMapping("docComplete", "Комплект документов", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("paymentDate", "Дата оплаты страховой премии", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("startDate", "Дата начала срока действия договора", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("endDate", "Дата окончания срока действия договора", EnumSet.of(PresentFlag.COLLAPSED));
        addMapping("dealer.name", "Торгова точка");
        super.addDefaultMappings();
    }


    private class ClientBirthdayGenerator implements GridColumnGenerator {

        @Override
        public Object generateCell(final Object columnId, final Item item, final Object itemId) {
            final Property itemProperty = getCellProperty(columnId, item);
            if (itemProperty != null) {
                final Label value = new Label(itemProperty);
                return value;
            }
            return null;
        }

        @Override
        public Property getCellProperty(final Object columnId, final Item item) {
            Property itemProperty = null;
            if (CLIENT_BIRTHDAY.equals(columnId)) {
                final Insurance ins = GridItem.extractBean(item);
                if (ins.getClientPP() != null) {
                    final BeanItem<Person> personItem = new BeanItem<>(ins.getClientPP());
                    itemProperty = personItem.getItemProperty("birthday");
                }
            }
            return itemProperty;
        }

        @Override
        public Class<?> getType() {
            return LocalDate.class;
        }
    }
}
