package ru.extas.web.product;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProdCreditDoc;
import ru.extas.web.commons.Fontello;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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
		setCaption(caption);
		setDescription(description);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {
		final VerticalLayout fieldLayout = new VerticalLayout();
		fieldLayout.setSpacing(true);

		if (!isReadOnly()) {
			final HorizontalLayout commandBar = new HorizontalLayout();
			commandBar.addStyleName("configure");
			commandBar.setSpacing(true);

			final Button addProdBtn = new Button("Добавить", new Button.ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					final BeanItem<ProdCreditDoc> newObj = container.addBean(new ProdCreditDoc(product));
					docTable.select(newObj.getBean());

//					final ProdCreditPercentForm editWin = new ProdCreditPercentForm("Новая процентная ставка", newObj);
//					editWin.addCloseListener(new Window.CloseListener() {
//
//						@Override
//						public void windowClose(final Window.CloseEvent e) {
//							if (editWin.isSaved()) {
//								container.addBean(newObj.getBean());
//								Notification.show("Процентная ставка добавлена", Notification.Type.TRAY_NOTIFICATION);
//							}
//						}
//					});
//					editWin.showModal();
				}
			});
			addProdBtn.setDescription("Добавить процентную стаквку в продукт");
			addProdBtn.setIcon(Fontello.DOC_NEW);
			commandBar.addComponent(addProdBtn);

//			final Button edtProdBtn = new Button("Изменить", new Button.ClickListener() {
//				@Override
//				public void buttonClick(final Button.ClickEvent event) {
//					if (docTable.getValue() != null) {
//						final BeanItem<ProdCreditDoc> docItem = (BeanItem<ProdCreditDoc>) docTable.getValue();
//						final ProdCreditPercentForm editWin = new ProdCreditPercentForm("Редактирование процентной ставки", docItem);
//						editWin.addCloseListener(new Window.CloseListener() {
//
//							@Override
//							public void windowClose(final Window.CloseEvent e) {
//								if (editWin.isSaved()) {
//									Notification.show("Продукт изменен", Notification.Type.TRAY_NOTIFICATION);
//								}
//							}
//						});
//						editWin.showModal();
//					}
//				}
//			});
//			edtProdBtn.setDescription("Изменить выделенную в списке процентную ставку");
//			edtProdBtn.addStyleName(Fontello.EDIT_3);
//			commandBar.addComponent(edtProdBtn);

			final Button delProdBtn = new Button("Удалить", new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					if (docTable.getValue() != null) {
						docTable.removeItem(docTable.getValue());
					}
				}
			});
			delProdBtn.setDescription("Удалить процентную ставку из продукта");
			delProdBtn.setIcon(Fontello.TRASH);
			commandBar.addComponent(delProdBtn);

			fieldLayout.addComponent(commandBar);
		}

		docTable = new Table();
		docTable.setRequired(true);
		docTable.setSelectable(true);
		docTable.setHeight(10, Unit.EM);
		docTable.setWidth(25, Unit.EM);
		docTable.addStyleName("components-inside");
		final Property dataSource = getPropertyDataSource();
		final List<ProdCreditDoc> docList = dataSource != null ? (List<ProdCreditDoc>) dataSource.getValue() : new ArrayList<ProdCreditDoc>();
		container = new BeanItemContainer<>(ProdCreditDoc.class);
		if (docList != null) {
			for (final ProdCreditDoc doc : docList) {
				container.addBean(doc);
			}
		}
		docTable.setContainerDataSource(container);
		docTable.addItemSetChangeListener(new Container.ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(final Container.ItemSetChangeEvent event) {
				setValue(newArrayList(docTable.getItemIds()));
			}
		});
		// Колонки таблицы
		docTable.setVisibleColumns("name", "required");
		docTable.setColumnHeader("name", "Документ");
		docTable.setColumnHeader("required", "Обязательный");
		docTable.setEditable(true);
		docTable.setTableFieldFactory(new TableFieldFactory() {
			@Override
			public Field<?> createField(final Container container, final Object itemId, final Object propertyId, final Component uiContext) {
				if ("name".equals(propertyId)) {
					final DocumentSelect field = new DocumentSelect("Документ");
					field.setPropertyDataSource(container.getItem(itemId).getItemProperty(propertyId));
					return field;
				} else if ("required".equals(propertyId)) {
					final CheckBox checkBox = new CheckBox();
					checkBox.setPropertyDataSource(container.getItem(itemId).getItemProperty(propertyId));
					return checkBox;
				}
				return null;
			}
		});
		fieldLayout.addComponent(docTable);

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
