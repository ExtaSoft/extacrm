/**
 * 
 */
package ru.extas.web.insurance;

import static ru.extas.server.ServiceLocator.lookup;

import java.util.Collection;

import ru.extas.model.Policy;
import ru.extas.server.PolicyRegistry;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;

/**
 * @author Valery Orlov
 * 
 */
public class PolicySelect extends ComboBox {

	private static final long serialVersionUID = 6004206917183679455L;

	@Override
	public Class<Policy> getType() {
		return Policy.class;
	}

	/**
	 * @param caption
	 * @param description
	 * @param forceNum
	 */
	public PolicySelect(final String caption, final String description, final String forceNum) {
		super(caption);

		// Преконфигурация
		setDescription(description);
		setInputPrompt("Выберите из реестра БСО");
		setWidth(20, Unit.EM);
		setImmediate(true);
		setNullSelectionAllowed(false);

		// Инициализация контейнера
// final LazyJdoContainer<Policy> container = new LazyJdoContainer<Policy>(Policy.class, 50, "key");
// Filter filter = new And(new IsNull("issueDate"),
// new Compare.Less("bookTime", DateTime.now().minusHours(1)));
// if (forceNum != null) {
// filter = new Or(new Compare.Equal("regNum", forceNum), filter);
// }
// container.addContainerFilter(filter);
//
// // Устанавливаем контент выбора
// setFilteringMode(FilteringMode.STARTSWITH);
// setContainerDataSource(container);
// setItemCaptionMode(ItemCaptionMode.PROPERTY);
// setItemCaptionPropertyId("regNum");
//
// setConverter(new SingleSelectConverter<Policy>(this, container));
		final PolicyRegistry policyRepository = lookup(PolicyRegistry.class);
		final Collection<Policy> policies = policyRepository.loadAvailable();
		final BeanItemContainer<Policy> clientsCont = new BeanItemContainer<Policy>(Policy.class);
		clientsCont.addAll(policies);
		if (forceNum != null) {
			final Policy forcePolicy = policyRepository.findByNum(forceNum);
			clientsCont.addBean(forcePolicy);
		}

		// Устанавливаем контент выбора
		setFilteringMode(FilteringMode.CONTAINS);
		setContainerDataSource(clientsCont);
		setItemCaptionMode(ItemCaptionMode.PROPERTY);
		setItemCaptionPropertyId("regNum");
	}

}
