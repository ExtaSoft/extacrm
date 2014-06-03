/**
 *
 */
package ru.extas.web.motor;

import com.vaadin.ui.Component;
import ru.extas.security.ExtaDomain;
import ru.extas.web.commons.AbstractTabView;
import ru.extas.web.commons.component.AbstractTabInfo;
import ru.extas.web.commons.component.TabInfo;

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
public class MotorView extends AbstractTabView {

	private static final long serialVersionUID = -1272779672761523416L;

	/**
	 * <p>Constructor for MotorView.</p>
	 */
	public MotorView() {
		super("Справочник техники");
	}


	/** {@inheritDoc} */
	@Override
	protected List<TabInfo> getTabComponentsInfo() {
		final ArrayList<TabInfo> ret = newArrayList();
		ret.add(new AbstractTabInfo("Модель техники", ExtaDomain.MOTOR_MODEL) {

			@Override
			public Component createComponent() {
				return new MotorModelGrid();
			}
		});
		ret.add(new AbstractTabInfo("Бренд", ExtaDomain.MOTOR_BRAND) {

			@Override
			public Component createComponent() {
				return new MotorBrandGrid();
			}
		});
		ret.add(new AbstractTabInfo("Тип техники", ExtaDomain.MOTOR_TYPE) {

			@Override
			public Component createComponent() {
				return new MotorTypeGrid();
			}
		});
		return ret;
	}
}
