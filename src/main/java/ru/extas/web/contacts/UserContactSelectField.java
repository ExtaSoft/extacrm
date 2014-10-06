package ru.extas.web.contacts;

import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Not;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.PopupView;
import ru.extas.model.contacts.Person;

/**
 * @author Valery Orlov
 *         Date: 06.10.2014
 *         Time: 16:51
 */
public class UserContactSelectField extends CustomField<Person> {
    public UserContactSelectField(String caption) {
        setCaption(caption);
    }

    @Override
    protected Component initContent() {

        PopupView popup = new PopupView(new PopupContent());
        return popup;
    }

    @Override
    public Class<? extends Person> getType() {
        return Person.class;
    }

    private class PopupContent implements PopupView.Content {
        @Override
        public String getMinimizedValueAsHTML() {
            Person person = (Person) getPropertyDataSource().getValue();
            if (person != null)
                return NameUtils.getShortName(person.getName());
            else
                return "Нажмите для выбора пользователя...";
        }

        @Override
        public Component getPopupComponent() {
            if (!isReadOnly()) {
                final UserContactSelect personSelectField = new UserContactSelect();
                personSelectField.setInputPrompt("Фамилия Имя Отчество");
                personSelectField.setPropertyDataSource(getPropertyDataSource());
                personSelectField.addValueChangeListener(e -> setValue((Person) personSelectField.getConvertedValue()));
                return personSelectField;
            }
            return null;
        }
    }

    private class UserContactSelect extends AbstractContactSelect<Person> {

        protected UserContactSelect() {
            super("", "Выберите пользователя системы", Person.class, new Not(new IsNull("userProfile")));
        }
    }

}
