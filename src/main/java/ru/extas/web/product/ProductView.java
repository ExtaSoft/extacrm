/**
 *
 */
package ru.extas.web.product;

import com.vaadin.ui.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.component.AbstractSubdomainInfo;
import ru.extas.web.commons.component.SubdomainInfo;

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
public class ProductView extends SubdomainView {

	private static final long serialVersionUID = -1272779672761523416L;

	/**
	 * <p>Constructor for ProductView.</p>
	 */
	public ProductView() {
		super("Продукты");
	}


	/** {@inheritDoc} */
	@Override
	protected List<SubdomainInfo> getSubdomainInfo() {
		final ArrayList<SubdomainInfo> ret = newArrayList();
		ret.add(new AbstractSubdomainInfo("Кредитные продукты", ExtaDomain.PROD_CREDIT) {

			@Override
			public ExtaGrid createGrid() {
				return new ProdCreditGrid();
			}
		});
		ret.add(new AbstractSubdomainInfo("Страховые продукты", ExtaDomain.PROD_INSURANCE) {

			@Override
			public ExtaGrid createGrid() {
				return new ProdInsuranceGrid();
			}
		});
		ret.add(new AbstractSubdomainInfo("Рассрочка", ExtaDomain.PROD_INSTALL) {

			@Override
			public ExtaGrid createGrid() {
				return new ProdInstallmentsGrid();
			}
		});
		return ret;
	}
}
