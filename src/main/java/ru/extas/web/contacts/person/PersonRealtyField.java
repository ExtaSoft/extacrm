package ru.extas.web.contacts.person;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.PersonRealty;
import ru.extas.web.commons.ExtaBeanContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.component.CardPanel;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.converters.StringToPercentConverter;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 12:19
 */
public class PersonRealtyField extends CustomField<List> {

    private final Person person;
    private ExtaBeanContainer<PersonRealty> itemContainer;
    private VerticalLayout root;

    public PersonRealtyField(final Person person) {
        this.person = person;
        addStyleName(ExtaTheme.NO_CAPTION_COMPLEX_FIELD);
    }

    @Override
    protected Component initContent() {
        final Property dataSource = checkNotNull(this.getPropertyDataSource(), "No Dsta source!!!");
        final List<PersonRealty> list = dataSource != null ? (List<PersonRealty>) dataSource.getValue() : new ArrayList<>();
        itemContainer = new ExtaBeanContainer<>(PersonRealty.class);

        root = new VerticalLayout();
        root.setMargin(new MarginInfo(true, false, true, false));
        root.setSpacing(true);

        final Button addBtn = new Button("Добавить", Fontello.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        addBtn.addClickListener(click -> {
            addRealty(new PersonRealty(person));
            updateValue();
        });
        root.addComponent(addBtn);

        for (final PersonRealty realty : list)
            addRealty(realty);

        return root;
    }

    private void addRealty(final PersonRealty realty) {
        final BeanItem<PersonRealty> realtyItem = itemContainer.addBean(realty);

        final ComponentContainer dataLine = new FormLayout();
        dataLine.addStyleName(ExtaTheme.FORMLAYOUT_LIGHT);

        final Button delItemBtn = new Button("Удалить");
        delItemBtn.setDescription("Удалить сведения о недвижимости");
        delItemBtn.setIcon(Fontello.TRASH_4);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_QUIET);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        //delItemBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        final CardPanel panel = new CardPanel("Недвижимость", delItemBtn, dataLine);
        delItemBtn.addClickListener(event -> {
            root.removeComponent(panel);
            itemContainer.removeItem(realty);
            updateValue();
        });

        // Тип недвижимости (Индивидуальный дом, Квартира, Дача, Земельный участок, Гараж, Другое имущество)
        final ComboBox typeField = new ComboBox("Тип недвижимости",
                newArrayList("Индивидуальный дом", "Квартира", "Дача", "Земельный участок", "Гараж"));
        typeField.setNullSelectionAllowed(false);
        typeField.setNewItemsAllowed(true);
        typeField.addStyleName(ExtaTheme.COMBOBOX_SMALL);
        typeField.setPropertyDataSource(realtyItem.getItemProperty("type"));
        typeField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(typeField);
        // Время владения (лет)
        final EditField owningPeriodField = new EditField("Время владения (лет)");
        owningPeriodField.setColumns(5);
        owningPeriodField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        owningPeriodField.setPropertyDataSource(realtyItem.getItemProperty("owningPeriod"));
        owningPeriodField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(owningPeriodField);
        // Доля владения %
        final EditField partField = new EditField("Доля владения %");
        partField.setColumns(5);
        partField.setConverter(lookup(StringToPercentConverter.class));
        partField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        partField.setPropertyDataSource(realtyItem.getItemProperty("part"));
        partField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(partField);
        // Общая площадь
        //  - строение (кв.м.)
        final EditField areaOfHouseField = new EditField("Площадь помещения (кв.м.)");
        areaOfHouseField.setColumns(5);
        areaOfHouseField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        areaOfHouseField.setPropertyDataSource(realtyItem.getItemProperty("areaOfHouse"));
        areaOfHouseField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(areaOfHouseField);
        //  - участка (соток)
        final EditField areaOfLandField = new EditField("Площадь участка (соток)");
        areaOfLandField.setColumns(5);
        areaOfLandField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        areaOfLandField.setPropertyDataSource(realtyItem.getItemProperty("areaOfLand"));
        areaOfLandField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(areaOfLandField);
        // Адрес объекта недвижимости
        final TextArea adressField = new TextArea("Адрес объекта");
        adressField.setRows(2);
        adressField.setInputPrompt("Город, Улица, Дом ...");
        adressField.setNullRepresentation("");
        adressField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        adressField.setPropertyDataSource(realtyItem.getItemProperty("adress"));
        adressField.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(adressField);

        // Способ приобретения (покупка, наследство/дар, другое)
        final ComboBox way2purchaseField = new ComboBox("Способ приобретения", newArrayList("Покупка", "Наследство/дар"));
        way2purchaseField.setNullSelectionAllowed(false);
        way2purchaseField.setNewItemsAllowed(true);
        way2purchaseField.addStyleName(ExtaTheme.COMBOBOX_SMALL);
        way2purchaseField.setPropertyDataSource(realtyItem.getItemProperty("way2purchase"));
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
