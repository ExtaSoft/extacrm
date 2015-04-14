package ru.extas.web.contacts.person;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.PersonExpense;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.container.ExtaBeanContainer;
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
 *         Date: 12.09.2014
 *         Time: 0:15
 */
public class PersonExpensesField extends CustomField<List> {

    private final Person person;
    private ExtaBeanContainer<PersonExpense> itemContainer;
    private VerticalLayout root;
    private Table table;

    public PersonExpensesField(final Person person) {
        this.person = person;
        addStyleName(ExtaTheme.NO_CAPTION_COMPLEX_FIELD);
    }

    @Override
    protected Component initContent() {
        final Property dataSource = checkNotNull(this.getPropertyDataSource(), "No Dsta source!!!");
        final List<PersonExpense> list = dataSource != null ? (List<PersonExpense>) dataSource.getValue() : new ArrayList<>();
        itemContainer = new ExtaBeanContainer<>(PersonExpense.class);
        itemContainer.addAll(list);
        itemContainer.addItemSetChangeListener(event -> updateValue(true));

        root = new VerticalLayout();
        root.setMargin(new MarginInfo(true, false, true, false));
        root.setSpacing(true);

        final Button addBtn = new Button("Добавить", Fontello.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        addBtn.addClickListener(click -> itemContainer.addBean(new PersonExpense(person)));
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
        table.addGeneratedColumn("deleteAction", (source, itemId, columnId) -> {
                    final Button delItemBtn = new Button("Удалить");
                    delItemBtn.setDescription("Удалить сведения о расходах");
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
        table.setVisibleColumns("type", "expense", "deleteAction");
        table.setColumnHeaders("Статья расхода", "Расход", "Удалить");
        table.setColumnWidth("type", 180);
        table.setTableFieldFactory((container, itemId, propertyId, uiContext) -> {
            if ("type".equals(propertyId)) {
                final HashSet<String> options = newHashSet("Текущие расходы (питание, одежда)", "Оплата кредитов");
                final Object value = container.getContainerProperty(itemId, propertyId).getValue();
                if (value != null)
                    options.add((String) value);
                final ComboBox typeField = new ComboBox("Статья расхода", options);
                typeField.setWidth(100, Unit.PERCENTAGE);
                typeField.setNullSelectionAllowed(false);
                typeField.setNewItemsAllowed(true);
                typeField.addStyleName(ExtaTheme.COMBOBOX_SMALL);
                typeField.addStyleName(ExtaTheme.COMBOBOX_BORDERLESS);
                typeField.addValueChangeListener(e -> updateValue(false));
                return typeField;
            } else if ("expense".equals(propertyId)) {
                final EditField incomeField = new EditField("Расход");
                incomeField.setWidth(100, Unit.PERCENTAGE);
                incomeField.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
                incomeField.addStyleName(ExtaTheme.TEXTFIELD_BORDERLESS);
                incomeField.addValueChangeListener(e -> updateValue(false));
                return incomeField;
            }
            return DefaultFieldFactory.get().createField(container, itemId, propertyId, uiContext);
        });

        table.setFooterVisible(true);
        table.setColumnFooter("type", "Итого");

        updateTableValue(list, true);

        root.addComponent(table);
        return root;
    }

    private void updateTableValue(final List<PersonExpense> list, final boolean isItemSetChanged) {
        final BigDecimal expenseSum = getExpenseSum(list, PersonExpense::getExpense);
        table.setColumnFooter("expense", lookup(StringToMoneyConverter.class).convertToPresentation(expenseSum, null));

        // Adjust the table height a bit
        table.setPageLength(table.size());
    }

    private BigDecimal getExpenseSum(final List<PersonExpense> list, final Function<PersonExpense, BigDecimal> func) {
        BigDecimal expenseSum = BigDecimal.ZERO;
        for (final PersonExpense expense : list) {
            final BigDecimal augend = func.apply(expense);
            if (augend != null)
                expenseSum = expenseSum.add(augend);
        }
        return expenseSum;
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private void updateValue(final boolean isItemSetChanged) {
        final List<PersonExpense> list = newArrayList(itemContainer.getItemIds());
        setValue(list);
        updateTableValue(list, isItemSetChanged);
    }
}
