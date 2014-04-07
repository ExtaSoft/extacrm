package ru.extas.web.contacts;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.contacts.Company;

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
				final BeanItem<Company> newObj = new BeanItem<>(new Company());
				newObj.getBean().setName(newItemCaption);
				newObj.expandProperty("actualAddress");

				final String edFormCaption = "Ввод нового контакта в систему";
				final CompanyEditForm editWin = new CompanyEditForm(edFormCaption, newObj);
				editWin.setModified(true);

				editWin.addCloseListener(new Window.CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final Window.CloseEvent e) {
						if (editWin.isSaved()) {
							container.refresh();
							setValue(newObj.getBean().getId());
						}
					}
				});
				editWin.showModal();
			}
		});
	}

}
