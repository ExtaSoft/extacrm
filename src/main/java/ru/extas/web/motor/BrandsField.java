package ru.extas.web.motor;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TwinColSelect;
import ru.extas.server.motor.MotorBrandRepository;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Реализует редактирование брендов внутри юридического лица
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 16:44
 * @version $Id: $Id
 * @since 0.3
 */
public class BrandsField extends CustomField<Set> {

	/**
	 * <p>Constructor for BrandsField.</p>
	 */
	public BrandsField() {
		setRequiredError("Необходимо указать хотябы один бренд!");
		setBuffered(true);
	}

	/** {@inheritDoc} */
	@Override
	protected Component initContent() {

		final TwinColSelect twin = new TwinColSelect();
		twin.setRows(10);
		twin.setNullSelectionAllowed(true);
		twin.setMultiSelect(true);
		twin.setImmediate(true);
		twin.setLeftColumnCaption("Доступные бренды");
		twin.setRightColumnCaption("Бренды юр.лица");
		twin.addValueChangeListener(event -> {
            Set selected = (Set) twin.getValue();
            setValue(selected);
        });

		final Property dataSource = getPropertyDataSource();
		final Set<String> set = (Set<String>) dataSource.getValue();
		if (set != null) {
			twin.setValue(newHashSet(set));
		}
		for (final String item : lookup(MotorBrandRepository.class).loadAllNames()) {
			twin.addItem(item);
		}

		return twin;
	}

	/** {@inheritDoc} */
	@Override
	public Class<? extends Set> getType() {
		return Set.class;
	}
}
