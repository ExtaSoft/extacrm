/**
 *
 */
package ru.extas.web.motor;

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
 * Реализует экран "Справочник техники"
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.5
 */
public class MotorView extends SubdomainView {

	private static final long serialVersionUID = -1272779672761523416L;

	/**
	 * <p>Constructor for MotorView.</p>
	 */
	public MotorView() {
		super("Справочник техники");
	}


	/** {@inheritDoc} */
	@Override
	protected List<SubdomainInfo> getSubdomainInfo() {
		final ArrayList<SubdomainInfo> ret = newArrayList();
		ret.add(new AbstractSubdomainInfo("Модель техники", ExtaDomain.MOTOR_MODEL) {

			@Override
			public ExtaGrid createGrid() {
				return new MotorModelGrid();
			}
		});
		ret.add(new AbstractSubdomainInfo("Бренд", ExtaDomain.MOTOR_BRAND) {

			@Override
			public ExtaGrid createGrid() {
				return new MotorBrandGrid();
			}
		});
		ret.add(new AbstractSubdomainInfo("Тип техники", ExtaDomain.MOTOR_TYPE) {

			@Override
			public ExtaGrid createGrid() {
				return new MotorTypeGrid();
			}
		});
		return ret;
	}
}
