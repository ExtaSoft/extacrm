package ru.extas.web.product;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import ru.extas.model.ProdCredit;
import ru.extas.model.ProdCreditPercent;
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
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @param product a {@link ru.extas.model.ProdCredit} object.
	 */
	public ProdCredPercentField(String caption, final String description, ProdCredit product) {
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
					final BeanItem<ProdCreditPercent> newObj = new BeanItem<>(new ProdCreditPercent(product));

					final ProdCreditPercentForm editWin = new ProdCreditPercentForm("Новая процентная ставка", newObj);
					editWin.addCloseListener(new Window.CloseListener() {

						@Override
						public void windowClose(final Window.CloseEvent e) {
							if (editWin.isSaved()) {
								container.addBean(newObj.getBean());
							}
						}
					});
					editWin.showModal();
				}
			});
			addProdBtn.setDescription("Добавить процентную стаквку в продукт");
			addProdBtn.addStyleName("icon-doc-new");
			commandBar.addComponent(addProdBtn);

			final Button edtProdBtn = new Button("Изменить", new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					if (procentTable.getValue() != null) {
						final BeanItem<ProdCreditPercent> percentItem = (BeanItem<ProdCreditPercent>) procentTable.getItem(procentTable.getValue());
						final ProdCreditPercentForm editWin = new ProdCreditPercentForm("Редактирование процентной ставки", percentItem);
						editWin.showModal();
					}
				}
			});
			edtProdBtn.setDescription("Изменить выделенную в списке процентную ставку");
			edtProdBtn.addStyleName("icon-edit-3");
			commandBar.addComponent(edtProdBtn);

			final Button delProdBtn = new Button("Удалить", new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					if (procentTable.getValue() != null) {
						procentTable.removeItem(procentTable.getValue());
					}
				}
			});
			delProdBtn.setDescription("Удалить процентную ставку из продукта");
			delProdBtn.addStyleName("icon-trash");
			commandBar.addComponent(delProdBtn);

			fieldLayout.addComponent(commandBar);
		}

		procentTable = new Table();
		procentTable.setRequired(true);
		procentTable.setSelectable(true);
		procentTable.setHeight(7, Unit.EM);
		procentTable.setWidth(25, Unit.EM);
		final Property dataSource = getPropertyDataSource();
		final List<ProdCreditPercent> percentList = dataSource != null ? (List<ProdCreditPercent>) dataSource.getValue() : new ArrayList<ProdCreditPercent>();
		container = new BeanItemContainer<>(ProdCreditPercent.class);
		if (percentList != null) {
			for (final ProdCreditPercent percent : percentList) {
				container.addBean(percent);
			}
		}
		procentTable.setContainerDataSource(container);
		procentTable.addItemSetChangeListener(new Container.ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(final Container.ItemSetChangeEvent event) {
				setValue(newArrayList(procentTable.getItemIds()));
			}
		});
		// Колонки таблицы
		procentTable.setVisibleColumns("percent", "period", "downpayment");
		procentTable.setColumnHeader("percent", "Процент");
		procentTable.setConverter("percent", lookup(StringToPercentConverter.class));
		procentTable.setColumnHeader("period", "Срок");
		procentTable.setColumnHeader("downpayment", "Первоначальный взнос");
		procentTable.setConverter("downpayment", lookup(StringToPercentConverter.class));
		fieldLayout.addComponent(procentTable);

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
