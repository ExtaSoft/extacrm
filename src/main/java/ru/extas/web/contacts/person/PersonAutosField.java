package ru.extas.web.contacts.person;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.PersonAuto;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.component.CardPanel;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.component.YearField;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 15:41
 */
public class PersonAutosField extends CustomField<List> {

    private final Person person;
    private ExtaBeanContainer<PersonAuto> itemContainer;
    private VerticalLayout root;

    public PersonAutosField(final Person person) {
        setRequiredError("Необходимо заполнить информацию об автотранспорте!");

        this.person = person;
        addStyleName(ExtaTheme.NO_CAPTION_COMPLEX_FIELD);
    }

    @Override
    protected Component initContent() {
        final Property dataSource = checkNotNull(this.getPropertyDataSource(), "No Data source!!!");
        final List<PersonAuto> list = dataSource != null ? (List<PersonAuto>) dataSource.getValue() : new ArrayList<>();
        itemContainer = new ExtaBeanContainer<>(PersonAuto.class);

        root = new VerticalLayout();
        root.setMargin(new MarginInfo(true, false, true, false));
        root.setSpacing(true);

        final Button addBtn = new Button("Добавить", Fontello.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        addBtn.addClickListener(click -> {
            addAuto(new PersonAuto(person));
            updateValue();
        });
        root.addComponent(addBtn);

        for (final PersonAuto auto : list)
            addAuto(auto);

        return root;
    }

    private void addAuto(final PersonAuto auto) {
        final BeanItem<PersonAuto> autoItem = itemContainer.addBean(auto);

        final ComponentContainer dataLine = new FormLayout();
        dataLine.addStyleName(ExtaTheme.FORMLAYOUT_LIGHT);

        final Button delItemBtn = new Button("Удалить");
        delItemBtn.setDescription("Удалить сведения об автотранспорте");
        delItemBtn.setIcon(Fontello.TRASH_4);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_QUIET);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        //delItemBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        final CardPanel panel = new CardPanel("Автотранспорт", delItemBtn, dataLine);
        delItemBtn.addClickListener(event -> {
            root.removeComponent(panel);
            itemContainer.removeItem(auto);
            updateValue();
        });

        // Марка, модель
        final EditField brandModelField = new EditField("Марка, модель");
        brandModelField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        brandModelField.setPropertyDataSource(autoItem.getItemProperty("brandModel"));
        brandModelField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(brandModelField);
        // год выпуска
        final YearField yearOfManufactureField = new YearField("Год выпуска");
        yearOfManufactureField.addStyleName(ExtaTheme.DATEFIELD_SMALL);
        yearOfManufactureField.setPropertyDataSource(autoItem.getItemProperty("yearOfManufacture"));
        yearOfManufactureField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(yearOfManufactureField);
        // Гос. рег. №
        final EditField regNumField = new EditField("Гос. рег. №");
        regNumField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        regNumField.setPropertyDataSource(autoItem.getItemProperty("regNum"));
        regNumField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(regNumField);
        // Покупная стоимость
        final EditField priceField = new EditField("Покупная стоимость");
        priceField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        priceField.setPropertyDataSource(autoItem.getItemProperty("price"));
        priceField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(priceField);
        // Дата приобретения
        final LocalDateField purchaseDateField = new LocalDateField("Дата приобретения");
        purchaseDateField.addStyleName(ExtaTheme.DATEFIELD_SMALL);
        purchaseDateField.setPropertyDataSource(autoItem.getItemProperty("purchaseDate"));
        purchaseDateField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(purchaseDateField);
        // Способ приобретения (покупка, покупка с пробегом, автокредит, покупка по ген. довер.)
        final ComboBox way2purchaseField = new ComboBox("Способ приобретения",
                newArrayList("Покупка", "Ппокупка с пробегом", "Автокредит", "Покупка по ген. довер."));
        way2purchaseField.setWidth(15, Unit.EM);
        way2purchaseField.setNullSelectionAllowed(false);
        way2purchaseField.setNewItemsAllowed(false);
        way2purchaseField.addStyleName(ExtaTheme.COMBOBOX_SMALL);
        way2purchaseField.setPropertyDataSource(autoItem.getItemProperty("way2purchase"));
        way2purchaseField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(way2purchaseField);

        root.addComponent(panel);
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private void updateValue() {
        setValue(newArrayList(itemContainer.getItemIds()));
    }
}
