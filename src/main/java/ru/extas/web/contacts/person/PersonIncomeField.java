package ru.extas.web.contacts.person;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.PersonIncome;
import ru.extas.web.commons.ExtaBeanContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.converters.StringToMoneyConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 11.09.2014
 *         Time: 17:04
 */
public class PersonIncomeField extends CustomField<List> {

    private final Person person;
    private ExtaBeanContainer<PersonIncome> itemContainer;
    private VerticalLayout root;
    private Table table;

    public PersonIncomeField(final Person person) {
        this.person = person;
        addStyleName(ExtaTheme.NO_CAPTION_COMPLEX_FIELD);
    }

    @Override
    protected Component initContent() {
        final Property dataSource = checkNotNull(this.getPropertyDataSource(), "No Dsta source!!!");
        final List<PersonIncome> list = dataSource != null ? (List<PersonIncome>) dataSource.getValue() : new ArrayList<>();
        itemContainer = new ExtaBeanContainer<>(PersonIncome.class);
        itemContainer.addAll(list);
        itemContainer.addItemSetChangeListener(event -> updateValue(true));

        root = new VerticalLayout();
        root.setMargin(new MarginInfo(true, false, true, false));
        root.setSpacing(true);

        final Button addBtn = new Button("Добавить", Fontello.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        addBtn.addClickListener(click -> itemContainer.addBean(new PersonIncome(person)));
        root.addComponent(addBtn);

        table = new Table();
        table.setContainerDataSource(itemContainer);
        table.addStyleName(ExtaTheme.TABLE_BORDERLESS);
        table.addStyleName(ExtaTheme.TABLE_SMALL);
        table.addStyleName(ExtaTheme.TABLE_COMPACT);
        table.addStyleName(ExtaTheme.TABLE_NO_STRIPES);
        table.addStyleName(ExtaTheme.TABLE_NO_VERTICAL_LINES);
        table.addStyleName(ExtaTheme.TABLE_NO_HORIZONTAL_LINES);

        table.setEditable(true);
        table.addGeneratedColumn("overallIncome", (source, itemId, columnId) -> {
                    final PersonIncome income = ((BeanItem<PersonIncome>) source.getItem(itemId)).getBean();
                    BigDecimal incomeSum = BigDecimal.ZERO;
                    if (income.getIncome() != null)
                        incomeSum = incomeSum.add(income.getIncome());
                    if (income.getSpouseIncome() != null)
                        incomeSum = incomeSum.add(income.getSpouseIncome());
                    return new Label(new ObjectProperty(incomeSum));
                }
        );
        table.addGeneratedColumn("deleteAction", (source, itemId, columnId) -> {
                    final Button delItemBtn = new Button("Удалить");
                    delItemBtn.setDescription("Удалить сведения о доходе");
                    delItemBtn.setIcon(Fontello.TRASH_4);
                    delItemBtn.addStyleName(ExtaTheme.BUTTON_QUIET);
                    delItemBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
                    delItemBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
                    delItemBtn.addClickListener(event -> {
                        itemContainer.removeItem(itemId);
                        updateValue(true);
                    });
                    return delItemBtn;
                }
        );
        table.setVisibleColumns("type", "income", "spouseIncome", "overallIncome", "deleteAction");
        table.setColumnHeaders("Ежемесячный доход", "Доход", "Доход супруги(а)", "Всего", "Удалить");
        table.setColumnWidth("type", 190);
        table.setTableFieldFactory((container, itemId, propertyId, uiContext) -> {
            if ("type".equals(propertyId)) {
                final HashSet<String> options = newHashSet("Основная зарплата", "Зарплата по совместительству");
                final Object value = container.getContainerProperty(itemId, propertyId).getValue();
                if (value != null)
                    options.add((String) value);
                final ComboBox typeField = new ComboBox("Тип дохода", options);
                typeField.setWidth(100, Unit.PERCENTAGE);
                typeField.setNullSelectionAllowed(false);
                typeField.setNewItemsAllowed(true);
                typeField.addStyleName(ExtaTheme.COMBOBOX_SMALL);
                typeField.addStyleName(ExtaTheme.COMBOBOX_BORDERLESS);
                typeField.addValueChangeListener(e -> updateValue(false));
                return typeField;
            } else if ("income".equals(propertyId)) {
                final EditField incomeField = new EditField("Доход");
                incomeField.setWidth(100, Unit.PERCENTAGE);
                incomeField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
                incomeField.addStyleName(ExtaTheme.TEXTFIELD_BORDERLESS);
                incomeField.addValueChangeListener(e -> updateValue(false));
                return incomeField;
            } else if ("spouseIncome".equals(propertyId)) {
                final EditField spouseIncomeField = new EditField("Доход супруги(а)");
                spouseIncomeField.setWidth(100, Unit.PERCENTAGE);
                spouseIncomeField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
                spouseIncomeField.addStyleName(ExtaTheme.TEXTFIELD_BORDERLESS);
                spouseIncomeField.addValueChangeListener(e -> updateValue(false));
                return spouseIncomeField;
            }
            return DefaultFieldFactory.get().createField(container, itemId, propertyId, uiContext);
        });

        table.setFooterVisible(true);
        table.setColumnFooter("type", "Итого");

        updateTableValue(list, true);

        root.addComponent(table);
        return root;
    }

    private void updateTableValue(final List<PersonIncome> list, final boolean isItemSetChanged) {
        final BigDecimal incomeSum = getIncomSum(list, PersonIncome::getIncome);
        final BigDecimal spouseIncomeSum = getIncomSum(list, PersonIncome::getSpouseIncome);
        table.setColumnFooter("income", lookup(StringToMoneyConverter.class).convertToPresentation(incomeSum, null));
        table.setColumnFooter("spouseIncome", lookup(StringToMoneyConverter.class).convertToPresentation(spouseIncomeSum, null));
        table.setColumnFooter("overallIncome", lookup(StringToMoneyConverter.class).convertToPresentation(spouseIncomeSum.add(incomeSum), null));

        // Adjust the table height a bit
        table.setPageLength(table.size());
    }

    private BigDecimal getIncomSum(final List<PersonIncome> list, final Function<PersonIncome, BigDecimal> func) {
        BigDecimal incomeSum = BigDecimal.ZERO;
        for (final PersonIncome income : list) {
            final BigDecimal augend = func.apply(income);
            if (augend != null)
                incomeSum = incomeSum.add(augend);
        }
        return incomeSum;
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private void updateValue(final boolean isItemSetChanged) {
        final List<PersonIncome> list = newArrayList(itemContainer.getItemIds());
        setValue(list);
        updateTableValue(list, isItemSetChanged);
    }
}
