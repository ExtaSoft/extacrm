package ru.extas.web.contacts.person;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.Person_;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Optional;
import java.util.Set;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Выбор контакта - физ. лица
 * с возможностью добавления нового
 * <p>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class PersonField extends CustomField<Person> {

    private PopupView popupView;
    private PopupPersonContent entityContent;

    /**
     * <p>Constructor for PersonSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public PersonField(final String caption) {
        this(caption, "");
    }

    /**
     * <p>Constructor for PersonSelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public PersonField(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        entityContent = new PopupPersonContent();
        popupView = new PopupView(entityContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Person> getType() {
        return Person.class;
    }

    private static class PersonSelectField extends ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected final ExtaDbContainer<Person> container;

        protected PersonSelectField(final String caption) {
            this(caption, "Выберите существующий контакт или введите новый");
        }

        protected PersonSelectField(final String caption, final String description) {
            super(caption);

            // Преконфигурация
            setDescription(description);
            setInputPrompt("контакт...");
            setWidth(25, Unit.EM);
            setImmediate(true);
            setBuffered(true);
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            container = new ExtaDbContainer<>(Person.class);
            container.sort(new Object[]{Person_.name.getName()}, new boolean[]{true});

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
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

    public static class PopupForm extends ExtaFormLayout {

        private final Label emailField;
        private final Label birthdayField;
        private final Label phoneField;
        private final Button viewBtn;
        private PersonSelectField personSelectField;
        private final Field field;

        public PopupForm(final Field field, final PopupView popupView) {
            this.field = field;

            setSizeUndefined();
            setSpacing(true);

            addComponent(new FormGroupHeader("Физическое лицо"));

            if (!isReadOnly()) {
                personSelectField = new PersonSelectField("Имя", "Введите или выберите имя контакта");
                personSelectField.setInputPrompt("Фамилия Имя Отчество");
                personSelectField.setPropertyDataSource(field.getPropertyDataSource());
                personSelectField.setNewItemsAllowed(true);
                personSelectField.setNewItemHandler(newItemCaption -> {
                    final Person newObj = new Person();
                    newObj.setName(newItemCaption);

                    final String edFormCaption = "Ввод нового контакта в систему";
                    final PersonEditForm editWin = new PersonEditForm(newObj);
                    editWin.setModified(true);

                    editWin.addCloseFormListener(event -> {
                        if (editWin.isSaved()) {
                            personSelectField.refreshContainer();
                            personSelectField.setConvertedValue(editWin.getEntity());
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    FormUtils.showModalWin(editWin);
                });
                personSelectField.addValueChangeListener(event -> refreshFields((Person) personSelectField.getConvertedValue()));
                addComponent(personSelectField);

            } else {
                final Label name = new Label();
                name.setCaption("Имя");
                final Person person = (Person) field.getValue();
                if (person != null)
                    name.setValue(person.getName());
                addComponent(name);
            }

            // Дата рождения
            birthdayField = new Label();
            birthdayField.setCaption("Дата рождения");
            addComponent(birthdayField);
            // Телефон
            phoneField = new Label();
            phoneField.setCaption("Телефон");
            phoneField.setConverter(lookup(PhoneConverter.class));
            addComponent(phoneField);
            // Мыло
            emailField = new Label();
            emailField.setCaption("E-Mail");
            addComponent(emailField);

            final HorizontalLayout toolbar = new HorizontalLayout();
            if (!isReadOnly()) {
                final Button searchBtn = new Button("Поиск", event -> {
                    final PersonSelectWindow selectWindow = new PersonSelectWindow("Выберите клиента или введите нового");
                    selectWindow.addCloseListener(e -> {
                        if (selectWindow.isSelectPressed()) {
                            final Set<Person> selected = selectWindow.getSelected();
                            personSelectField.setConvertedValue(selected.stream().findFirst().orElse(null));
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    selectWindow.showModal();

                });
                searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
                searchBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
                searchBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
                toolbar.addComponent(searchBtn);
            }

            viewBtn = new Button("Просмотр", event -> {
                final Person bean = (Person) field.getValue();

                final PersonEditForm editWin = new PersonEditForm(bean);
                editWin.setReadOnly(isReadOnly());

                editWin.addCloseFormListener(event1 -> {
                    if (editWin.isSaved()) {
                        refreshFields(bean);
                    }
                    popupView.setPopupVisible(true);
                });
                popupView.setPopupVisible(false);
                FormUtils.showModalWin(editWin);
            });
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            viewBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(viewBtn);
            addComponent(toolbar);

            refreshFields((Person) field.getValue());
        }

        private void refreshFields(final Person person) {
            field.setValue(person);

            final BeanItem<Person> beanItem = new BeanItem<>(Optional.ofNullable(person).orElse(new Person()));
            if (viewBtn != null) viewBtn.setEnabled(person != null);
            // Дата рождения
            if (birthdayField != null) birthdayField.setPropertyDataSource(beanItem.getItemProperty("birthday"));
            // Телефон
            if (phoneField != null) phoneField.setPropertyDataSource(beanItem.getItemProperty("phone"));
            // Мыло
            if (emailField != null) emailField.setPropertyDataSource(beanItem.getItemProperty("email"));
        }
    }
    private class PopupPersonContent implements PopupView.Content {

        @Override
        public String getMinimizedValueAsHTML() {
            final Person person = getValue();
            if (person != null)
                return person.getName();
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {
            return new PopupForm(PersonField.this, popupView);
        }

    }
}
