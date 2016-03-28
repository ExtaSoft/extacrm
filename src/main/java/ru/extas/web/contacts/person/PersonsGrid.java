/**
 *
 */
package ru.extas.web.contacts.person;

import com.vaadin.data.Container;
import ru.extas.model.contacts.Person;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.SecuredDataContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (физ. лица)
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class PersonsGrid extends ExtaGrid<Person> {

    /**
     * <p>Constructor for PersonsGrid.</p>
     */
    public PersonsGrid() {
        super(Person.class);
    }

    @Override
    public ExtaEditForm<Person> createEditForm(final Person person, final boolean isInsert) {
        return new PersonEditForm(person);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new PersonDataDecl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        final ExtaDbContainer<Person> container = SecuredDataContainer.create(Person.class, ExtaDomain.PERSON);
        container.addNestedContainerProperty("registerAddress.regionWithType");
        container.addNestedContainerProperty("registerAddress.city");
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод нового Контакта в систему", Fontello.USER_ADD_1));
        actions.add(new EditObjectAction("Изменить", "Редактирование контактных данных", Fontello.USER_2));

        return actions;
    }

}
