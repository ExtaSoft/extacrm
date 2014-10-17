package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.contacts.Company;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.FormUtils;

/**
 * Выбор контакта - юр. лица
 * с возможностью добавления нового
 * <p/>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompanySelect extends ComboBox {

    protected ExtaDataContainer<Company> container;

    /**
	 * <p>Constructor for CompanySelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public CompanySelect(final String caption) {
		this(caption, caption);
	}

	/**
	 * <p>Constructor for CompanySelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public CompanySelect(final String caption, final String description) {
        super(caption);

        // Преконфигурация
        setDescription(description);
        setInputPrompt("контакт...");
        setWidth(25, Unit.EM);
        setImmediate(true);

        // Инициализация контейнера
        container = new ExtaDataContainer<>(Company.class);
//        if (filter != null) {
//            container.addContainerFilter(filter);
//        }

        // Устанавливаем контент выбора
        setFilteringMode(FilteringMode.CONTAINS);
        setContainerDataSource(container);
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setItemCaptionPropertyId("name");
        setConverter(new SingleSelectConverter<Company>(this));

        // Функционал добавления нового контакта
        setNullSelectionAllowed(false);
        setNewItemsAllowed(false);
        addNewItemFeature();
	}

	private void addNewItemFeature() {
		setNewItemsAllowed(true);
		setNewItemHandler(new NewItemHandler() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({"unchecked"})
			@Override
			public void addNewItem(final String newItemCaption) {
				final Company newObj = new Company();
				newObj.setName(newItemCaption);

				final CompanyEditForm editWin = new CompanyEditForm(newObj);
				editWin.setModified(true);

                editWin.addCloseFormListener(event -> {
                    if (editWin.isSaved()) {
                        container.refresh();
                        setValue(editWin.getObjectId());
                    }
                });
                FormUtils.showModalWin(editWin);
			}
		});
	}

}
