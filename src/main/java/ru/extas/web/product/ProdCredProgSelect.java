package ru.extas.web.product;

import com.vaadin.ui.ComboBox;
import ru.extas.model.ProdCredit;
import ru.extas.web.util.ComponentUtil;

/**
 * Поле выбора кредитной программы
 *
 * @author Valery Orlov
 *         Date: 07.02.14
 *         Time: 15:26
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdCredProgSelect extends ComboBox {

	/**
	 * <p>Constructor for ProdCredProgSelect.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public ProdCredProgSelect(final String caption, final String description) {
		super(caption);
		setDescription(description);
		setWidth(30, Unit.EM);

		setNullSelectionAllowed(false);
		setNewItemsAllowed(false);
		ComponentUtil.fillSelectByEnum(this, ProdCredit.ProgramType.class);
	}

}
