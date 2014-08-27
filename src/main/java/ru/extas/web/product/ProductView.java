/**
 *
 */
package ru.extas.web.product;

import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainInfoImpl;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.SubdomainInfo;

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
		ret.add(new SubdomainInfoImpl("Кредитные продукты", ExtaDomain.PROD_CREDIT) {

			@Override
			public ExtaGrid createGrid() {
				return new ProdCreditGrid();
			}
		});
		ret.add(new SubdomainInfoImpl("Страховые продукты", ExtaDomain.PROD_INSURANCE) {

			@Override
			public ExtaGrid createGrid() {
				return new ProdInsuranceGrid();
			}
		});
		ret.add(new SubdomainInfoImpl("Рассрочка", ExtaDomain.PROD_INSTALL) {

			@Override
			public ExtaGrid createGrid() {
				return new ProdInstallmentsGrid();
			}
		});
		return ret;
	}
}
