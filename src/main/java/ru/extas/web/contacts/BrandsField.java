package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TwinColSelect;
import ru.extas.server.SupplementService;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Реализует редактирование брендов внутри юридического лица
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 16:44
 */
public class BrandsField extends CustomField<Set> {

	public BrandsField() {
		setBuffered(true);
	}

	@Override
	protected Component initContent() {

		final TwinColSelect twin = new TwinColSelect();
		twin.setRows(10);
		twin.setNullSelectionAllowed(true);
		twin.setMultiSelect(true);
		twin.setImmediate(true);
		twin.setLeftColumnCaption("Доступные бренды");
		twin.setRightColumnCaption("Бренды юр.лица");
		twin.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final Property.ValueChangeEvent event) {
				Set selected = (Set) twin.getValue();
				setValue(selected);
			}
		});

		final Property dataSource = getPropertyDataSource();
		final Set<String> set = (Set<String>) dataSource.getValue();
		if (set != null) {
			twin.setValue(newHashSet(set));
		}
		for (final String item : lookup(SupplementService.class).loadMotorBrands()) {
			twin.addItem(item);
		}

		return twin;
	}

	@Override
	public Class<? extends Set> getType() {
		return Set.class;
	}
}
