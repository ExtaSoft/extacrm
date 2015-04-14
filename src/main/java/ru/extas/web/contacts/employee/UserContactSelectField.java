package ru.extas.web.contacts.employee;

import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Not;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.PopupView;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.Employee_;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.contacts.NameUtils;

/**
 * @author Valery Orlov
 *         Date: 06.10.2014
 *         Time: 16:51
 */
public class UserContactSelectField extends CustomField<Employee> {
    public UserContactSelectField(final String caption) {
        setCaption(caption);
    }

    @Override
    protected Component initContent() {

        final PopupView popup = new PopupView(new PopupContent());
        return popup;
    }

    @Override
    public Class<? extends Employee> getType() {
        return Employee.class;
    }

    private class PopupContent implements PopupView.Content {
        @Override
        public String getMinimizedValueAsHTML() {
            final Person person = (Person) getPropertyDataSource().getValue();
            if (person != null)
                return NameUtils.getShortName(person.getName());
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {
            if (!isReadOnly()) {
                final UserContactSelect personSelectField = new UserContactSelect();
                personSelectField.setInputPrompt("Фамилия Имя Отчество");
                personSelectField.setPropertyDataSource(getPropertyDataSource());
                personSelectField.addValueChangeListener(e -> setValue((Employee) personSelectField.getConvertedValue()));
                return personSelectField;
            }
            return null;
        }
    }

    private class UserContactSelect extends com.vaadin.ui.ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected final ExtaDbContainer<Employee> container;

        protected UserContactSelect() {
            super("");
            final Filter filter = new Not(new IsNull("userProfile"));

            final Class<Employee> contactType1 = Employee.class;

            // Преконфигурация
            setDescription("Выберите пользователя системы");
            setInputPrompt("контакт...");
            setWidth(25, Unit.EM);
            setImmediate(true);
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            this.container = new ExtaDbContainer<>(Employee.class);
            if (filter != null) {
                this.container.addContainerFilter(filter);
            }
            container.sort(new Object[]{Employee_.name.getName()}, new boolean[]{true});

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(this.container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");
            container.setSingleSelectConverter(this);

            // Функционал добавления нового контакта
            setNullSelectionAllowed(false);
            setNewItemsAllowed(false);
        }

        /**
         * <p>refreshContainer.</p>
         */
        public void refreshContainer() {
            container.refresh();
        }
    }

}
