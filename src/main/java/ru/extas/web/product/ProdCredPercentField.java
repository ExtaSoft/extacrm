package ru.extas.web.product;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProdCreditPercent;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.converters.StringToPercentConverter;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле редактирования процентных ставок в кредитном продукте
 *
 * @author Valery Orlov
 *         Date: 07.02.14
 *         Time: 15:28
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdCredPercentField extends CustomField<List> {

    private final ProdCredit product;
    private Table procentTable;
    private BeanItemContainer<ProdCreditPercent> container;

    /**
     * <p>Constructor for ProdCredPercentField.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param product     a {@link ru.extas.model.sale.ProdCredit} object.
     */
    public ProdCredPercentField(final String caption, final String description, final ProdCredit product) {
        this.product = product;
        setWidth(400, Unit.PIXELS);
        setHeight(200, Unit.PIXELS);
        setCaption(caption);
        setDescription(description);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSizeFull();
        fieldLayout.setSpacing(true);
        fieldLayout.setMargin(new MarginInfo(true, false, true, false));

        if (!isReadOnly()) {
            final MenuBar commandBar = new MenuBar();
            commandBar.setAutoOpen(true);
            commandBar.addStyleName(ExtaTheme.GRID_TOOLBAR);
            commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);

            final MenuBar.MenuItem addProdBtn = commandBar.addItem("Добавить", event -> {
                final BeanItem<ProdCreditPercent> newObj = new BeanItem<>(new ProdCreditPercent(product));

                final ProdCreditPercentForm editWin = new ProdCreditPercentForm("Новая процентная ставка", newObj);
                editWin.addCloseFormListener(event1 -> {
                    if (editWin.isSaved()) {
                        container.addBean(newObj.getBean());
                    }
                });
                FormUtils.showModalWin(editWin);
            });
            addProdBtn.setDescription("Добавить процентную стаквку в продукт");
            addProdBtn.setIcon(Fontello.DOC_NEW);

            final MenuBar.MenuItem edtProdBtn = commandBar.addItem("Изменить", event -> {
                if (procentTable.getValue() != null) {
                    final BeanItem<ProdCreditPercent> percentItem = (BeanItem<ProdCreditPercent>) procentTable.getItem(procentTable.getValue());
                    final ProdCreditPercentForm editWin = new ProdCreditPercentForm("Редактирование процентной ставки", percentItem);
                    FormUtils.showModalWin(editWin);
                }
            });
            edtProdBtn.setDescription("Изменить выделенную в списке процентную ставку");
            edtProdBtn.setIcon(Fontello.EDIT_3);

            final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
                if (procentTable.getValue() != null) {
                    procentTable.removeItem(procentTable.getValue());
                }
            });
            delProdBtn.setDescription("Удалить процентную ставку из продукта");
            delProdBtn.setIcon(Fontello.TRASH);

            fieldLayout.addComponent(commandBar);
        }

        procentTable = new Table();
        procentTable.setSizeFull();
        procentTable.addStyleName(ExtaTheme.TABLE_SMALL);
        procentTable.addStyleName(ExtaTheme.TABLE_COMPACT);
        procentTable.setRequired(true);
        procentTable.setSelectable(true);
        final Property dataSource = getPropertyDataSource();
        final List<ProdCreditPercent> percentList = dataSource != null ? (List<ProdCreditPercent>) dataSource.getValue() : new ArrayList<ProdCreditPercent>();
        container = new BeanItemContainer<>(ProdCreditPercent.class);
        if (percentList != null) {
            for (final ProdCreditPercent percent : percentList) {
                container.addBean(percent);
            }
        }
        procentTable.setContainerDataSource(container);
        procentTable.addItemSetChangeListener(event -> setValue(newArrayList(procentTable.getItemIds())));
        // Колонки таблицы
        procentTable.setVisibleColumns("percent", "period", "downpayment");
        procentTable.setColumnHeader("percent", "Процент");
        procentTable.setConverter("percent", lookup(StringToPercentConverter.class));
        procentTable.setColumnHeader("period", "Срок");
        procentTable.setColumnHeader("downpayment", "Первоначальный взнос");
        procentTable.setConverter("downpayment", lookup(StringToPercentConverter.class));
        fieldLayout.addComponent(procentTable);
        fieldLayout.setExpandRatio(procentTable, 1);

        return fieldLayout;
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
