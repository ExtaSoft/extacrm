/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import ru.extas.model.insurance.Policy;
import ru.extas.server.insurance.PolicyRepository;
import ru.extas.web.commons.ExtaBeanContainer;

import java.util.Collection;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>PolicySelect class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class PolicySelect extends ComboBox {

    private static final long serialVersionUID = 6004206917183679455L;

    /** {@inheritDoc} */
    @Override
    public Class<Policy> getType() {
        return Policy.class;
    }

    /**
     * <p>Constructor for PolicySelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param forceNum    a {@link java.lang.String} object.
     */
    public PolicySelect(final String caption, final String description, final String forceNum) {
        super(caption);

        // Преконфигурация
        setDescription(description);
        setInputPrompt("Выберите...");
        setWidth(20, Unit.EM);
        setImmediate(true);
        setNullSelectionAllowed(false);
        setScrollToSelectedItem(true);

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
        final PolicyRepository policyRepository = lookup(PolicyRepository.class);
        final PolicyRepository policyService = lookup(PolicyRepository.class);
        final Collection<Policy> policies = policyService.loadAvailable();
        final ExtaBeanContainer<Policy> clientsCont = new ExtaBeanContainer<>(Policy.class);
        clientsCont.addAll(policies);
        if (forceNum != null) {
            final Policy forcePolicy = policyRepository.findByRegNum(forceNum);
            clientsCont.addBean(forcePolicy);
        }

        // Устанавливаем контент выбора
        setFilteringMode(FilteringMode.CONTAINS);
        setContainerDataSource(clientsCont);
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setItemCaptionPropertyId("regNum");
    }

}
