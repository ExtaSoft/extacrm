package ru.extas.web.lead;

import com.vaadin.ui.ComboBox;

import java.util.Arrays;

/**
 * Выбор источника лида
 *
 * Created by valery on 28.04.16.
 */
public class LeadSourceSelect extends ComboBox {

    public static final String DEALER_MANAGER = "Сотрудник Дилера";
    public static final String DEALER_SITE = "Сайт Дилера";
    public static final String CLIENT_CALL = "Звонок клиента";
    public static final String USER_ACCOUNT = "ЛК Клиента";
    public static final String OTHER = "Прочее...";
    public static final String EA_MANAGER = "Сотрудник ЭА";

    public LeadSourceSelect() {
        super("Источник лида", Arrays.asList(
                EA_MANAGER,
                DEALER_MANAGER,
                DEALER_SITE,
                CLIENT_CALL,
                USER_ACCOUNT,
                OTHER));

        setDescription("Укажите источник из которого пришел лид");
        setWidth(15, Unit.EM);
        setNullSelectionAllowed(false);
        setNewItemsAllowed(true);

    }

}
