/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.A7Form;
import ru.extas.server.A7FormRepository;
import ru.extas.server.A7FormService;

import java.util.Collection;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 */
public class A7Select extends ComboBox {

private static final long serialVersionUID = 6004206917183679455L;

@Override
public Class<A7Form> getType() {
	return A7Form.class;
}

/**
 * @param caption
 * @param description
 * @param forceNum
 */
public A7Select(final String caption, final String description, final String forceNum) {
	super(caption);

	// Преконфигурация
	setDescription(description);
	setInputPrompt("Выберите из реестра БСО");
	setWidth(20, Unit.EM);
	setImmediate(true);
	setNullSelectionAllowed(false);

	final A7FormRepository a7FormRepository = lookup(A7FormRepository.class);
	final A7FormService a7FormService = lookup(A7FormService.class);
	final Collection<A7Form> forms = a7FormService.loadAvailable();
	final BeanItemContainer<A7Form> clientsCont = new BeanItemContainer<>(A7Form.class);
	clientsCont.addAll(forms);
	if (forceNum != null) {
		final A7Form forceForm = a7FormRepository.findByRegNum(forceNum);
		clientsCont.addBean(forceForm);
	}

	// Устанавливаем контент выбора
	setFilteringMode(FilteringMode.CONTAINS);
	setContainerDataSource(clientsCont);
	setItemCaptionMode(ItemCaptionMode.PROPERTY);
	setItemCaptionPropertyId("regNum");
}

}
