package ru.extas.web.product;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProdCreditDoc;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.vaadin.ui.Button.*;

/**
 * Поле редактирования комплекта документов для кредитного продукта
 *
 * @author Valery Orlov
 *         Date: 07.02.14
 *         Time: 15:30
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdCredDocsField extends CustomField<List> {

	private final ProdCredit product;
	private Table docTable;
	private BeanItemContainer<ProdCreditDoc> container;

	/**
	 * <p>Constructor for ProdCredDocsField.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @param product a {@link ru.extas.model.sale.ProdCredit} object.
	 */
	public ProdCredDocsField(String caption, final String description, ProdCredit product) {
		this.product = product;
        setWidth(400, Unit.PIXELS);
        setHeight(300, Unit.PIXELS);
        setCaption(caption);
		setDescription(description);
	}

	/** {@inheritDoc} */
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
                final BeanItem<ProdCreditDoc> newObj = container.addBean(new ProdCreditDoc(product));
                docTable.select(newObj.getBean());
            });
			addProdBtn.setDescription("Добавить процентную стаквку в продукт");
			addProdBtn.setIcon(Fontello.DOC_NEW);


			final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
                if (docTable.getValue() != null) {
                    docTable.removeItem(docTable.getValue());
                }
            });
			delProdBtn.setDescription("Удалить процентную ставку из продукта");
			delProdBtn.setIcon(Fontello.TRASH);

			fieldLayout.addComponent(commandBar);
		}

		docTable = new Table();
        docTable.setSizeFull();
        docTable.addStyleName(ExtaTheme.TABLE_SMALL);
        docTable.addStyleName(ExtaTheme.TABLE_COMPACT);
        docTable.setRequired(true);
        docTable.setSelectable(true);
		final Property dataSource = getPropertyDataSource();
		final List<ProdCreditDoc> docList = dataSource != null ? (List<ProdCreditDoc>) dataSource.getValue() : new ArrayList<ProdCreditDoc>();
		container = new BeanItemContainer<>(ProdCreditDoc.class);
		if (docList != null) {
			for (final ProdCreditDoc doc : docList) {
				container.addBean(doc);
			}
		}
		docTable.setContainerDataSource(container);
		docTable.addItemSetChangeListener(event -> setValue(newArrayList(docTable.getItemIds())));
		// Колонки таблицы
		docTable.setVisibleColumns("name", "required");
		docTable.setColumnHeader("name", "Документ");
		docTable.setColumnHeader("required", "Обязательный");
		docTable.setEditable(true);
		docTable.setTableFieldFactory((container1, itemId, propertyId, uiContext) -> {
            if ("name".equals(propertyId)) {
                final DocumentSelect field = new DocumentSelect("Документ");
                field.addStyleName(ExtaTheme.COMBOBOX_SMALL);
                field.addStyleName(ExtaTheme.COMBOBOX_BORDERLESS);
                field.setWidth(100, Unit.PERCENTAGE);
                field.setPropertyDataSource(container1.getItem(itemId).getItemProperty(propertyId));
                return field;
            } else if ("required".equals(propertyId)) {
                final CheckBox checkBox = new CheckBox();
                checkBox.setPropertyDataSource(container1.getItem(itemId).getItemProperty(propertyId));
                checkBox.addStyleName(ExtaTheme.CHECKBOX_SMALL);
                return checkBox;
            }
            return null;
        });
		fieldLayout.addComponent(docTable);
        fieldLayout.setExpandRatio(docTable, 1);

        return fieldLayout;
	}

	/** {@inheritDoc} */
	@Override
	public void commit() throws SourceException, Validator.InvalidValueException {
		super.commit();
		final Property dataSource = getPropertyDataSource();
		if (dataSource != null)
			dataSource.setValue(container.getItemIds());
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends List> getType() {
		return List.class;
	}
}
