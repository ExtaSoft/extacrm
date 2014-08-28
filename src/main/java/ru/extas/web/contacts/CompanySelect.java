package ru.extas.web.contacts;

import ru.extas.model.contacts.Company;
import ru.extas.web.commons.ExtaEditForm;
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
public class CompanySelect extends AbstractContactSelect<Company> {

	/**
	 * <p>Constructor for CompanySelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public CompanySelect(final String caption) {
		super(caption, Company.class);
		addNewItemFeature();
	}

	/**
	 * <p>Constructor for CompanySelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public CompanySelect(final String caption, final String description) {
		super(caption, description, Company.class);
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
