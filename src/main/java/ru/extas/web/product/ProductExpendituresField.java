package ru.extas.web.product;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.MTextArea;
import ru.extas.model.sale.ProductExpenditure;
import ru.extas.model.sale.ProductInSale;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.container.ExtaBeanContainer;
import ru.extas.web.util.ComponentUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Поле редактирования Дополнительных расходов по продукту
 *
 * @author Valery Orlov
 *         Date: 07.02.14
 *         Time: 15:30
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductExpendituresField extends ExtaCustomField<List> {

    private final ProductInSale productInst;
    private Table costTable;
    private ExtaBeanContainer<ProductExpenditure> container;

    /**
     * <p>Constructor for .</p>
     *
     * @param caption     a {@link String} object.
     * @param description a {@link String} object.
     */
    public ProductExpendituresField(final String caption, final String description, final ProductInSale productInst) {
        super(caption, description);
        this.productInst = productInst;
        setWidth(100, Unit.PERCENTAGE);
        setHeight(300, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final GridLayout panel = new GridLayout(1, 2);
        panel.setSizeFull();

        panel.setRowExpandRatio(1, 1);
        panel.setMargin(true);

        if (!isReadOnly()) {
            panel.setSpacing(true);
            final MenuBar commandBar = new MenuBar();
            commandBar.setAutoOpen(true);
            commandBar.addStyleName(ExtaTheme.GRID_TOOLBAR);
            commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);

            final MenuBar.MenuItem addProdBtn = commandBar.addItem("Добавить", event -> {
                final BeanItem<ProductExpenditure> newObj = container.addBean(new ProductExpenditure(productInst));
                costTable.select(newObj.getBean());
            });
            addProdBtn.setDescription("Добавить статью расхода");
            addProdBtn.setIcon(Fontello.DOC_NEW);


            final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
                if (costTable.getValue() != null) {
                    costTable.removeItem(costTable.getValue());
                }
            });
            delProdBtn.setDescription("Удалить статью расхода");
            delProdBtn.setIcon(Fontello.TRASH);

            panel.addComponent(commandBar);
        }

        costTable = new Table();
        costTable.setSizeFull();
        costTable.addStyleName(ExtaTheme.TABLE_SMALL);
        costTable.addStyleName(ExtaTheme.TABLE_COMPACT);
        costTable.setSelectable(true);
        final Property dataSource = getPropertyDataSource();
        final List<ProductExpenditure> docList = dataSource != null ? (List<ProductExpenditure>) dataSource.getValue() : new ArrayList<>();
        container = new ExtaBeanContainer<>(ProductExpenditure.class);
        if (docList != null) {
            for (final ProductExpenditure exp : docList) {
                container.addBean(exp);
            }
        }
        costTable.setContainerDataSource(container);
        costTable.addItemSetChangeListener(event -> setValue(newArrayList(costTable.getItemIds())));
        // Колонки таблицы
        costTable.setVisibleColumns("type", "cost", "comment");
        costTable.setColumnHeader("type", "Статья");
        costTable.setColumnWidth("type", 200);
        costTable.setColumnHeader("cost", "Стоимость");
        costTable.setColumnWidth("cost", 95);
        costTable.setColumnHeader("comment", "Коментарий");
        costTable.setColumnWidth("comment", 200);
        costTable.setEditable(true);
        costTable.setTableFieldFactory((container1, itemId, propertyId, uiContext) -> {
            if ("type".equals(propertyId)) {
                final EnumSelect<ProductExpenditure.Type> field = new EnumSelect<>("Статья расхода");
                field.withSelectType(ComboBox.class)
                        .withStyleName(ExtaTheme.COMBOBOX_TINY, ExtaTheme.COMBOBOX_BORDERLESS)
                        .withWidth(100, Unit.PERCENTAGE);
                field.setOptions(ProductExpenditure.Type.values());
                field.setCaptionGenerator(ComponentUtil.getEnumCaptionGenerator(ProductExpenditure.Type.class));
                field.setPropertyDataSource(container1.getItem(itemId).getItemProperty(propertyId));
                return field;
            } else if ("cost".equals(propertyId)) {
                final EditField field = new EditField("Стоимость");
                field.withStyleName(ExtaTheme.TEXTFIELD_BORDERLESS, ExtaTheme.TEXTFIELD_TINY)
                        .withWidth(100, Unit.PERCENTAGE);
                field.setPropertyDataSource(container1.getItem(itemId).getItemProperty(propertyId));
                return field;
            } else if ("comment".equals(propertyId)) {
                final MTextArea field =
                        new MTextArea("Коментарий")
                                .withRows(1)
                                .withStyleName(ExtaTheme.TEXTAREA_BORDERLESS, ExtaTheme.TEXTAREA_TINY);
                field.setPropertyDataSource(container1.getItem(itemId).getItemProperty(propertyId));
                return field;
            }
            return null;
        });
        costTable.setWidth(500, Unit.PIXELS);
        panel.addComponent(costTable);

        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws SourceException, Validator.InvalidValueException {
        super.commit();
        final Property dataSource = getPropertyDataSource();
        if (dataSource != null)
            dataSource.setValue(container.getItemIds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends List> getType() {
        return List.class;
    }
}
