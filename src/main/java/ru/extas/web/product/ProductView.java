/**
 *
 */
package ru.extas.web.product;

import com.vaadin.ui.Component;
import ru.extas.security.ExtaDomain;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует экран "Продукты"
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ProductView extends AbstractTabView {

	private static final long serialVersionUID = -1272779672761523416L;

	/**
	 * <p>Constructor for ProductView.</p>
	 */
	public ProductView() {
		super("Продукты");
	}


	/** {@inheritDoc} */
	@Override
	protected List<TabInfo> getTabComponentsInfo() {
		final ArrayList<TabInfo> ret = newArrayList();
		ret.add(new AbstractTabInfo("Кредитные продукты", ExtaDomain.PROD_CREDIT) {

			@Override
			public Component createComponent() {
				return new ProdCreditGrid();
			}
		});
		ret.add(new AbstractTabInfo("Страховые продукты", ExtaDomain.PROD_INSURANCE) {

			@Override
			public Component createComponent() {
				return new ProdInsuranceGrid();
			}
		});
		ret.add(new AbstractTabInfo("Рассрочка", ExtaDomain.PROD_INSTALL) {

			@Override
			public Component createComponent() {
				return new ProdInstallmentsGrid();
			}
		});
		return ret;
	}
}
