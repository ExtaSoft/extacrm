package ru.extas.web.contacts.person;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Optional;

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
public class PersonSelect extends CustomField<Person> {

    private PopupView popupView;
    private PopupPersonContent entityContent;

    /**
     * <p>Constructor for PersonSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public PersonSelect(final String caption) {
        this(caption, "");
    }

    /**
     * <p>Constructor for PersonSelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public PersonSelect(final String caption, final String description) {
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

    private class PersonSelectField extends ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected final ExtaDataContainer<Person> container;

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

            // Инициализация контейнера
            container = new ExtaDataContainer<>(Person.class);

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");
            setConverter(new SingleSelectConverter<Person>(this));

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

    private class PopupPersonContent implements PopupView.Content {

        private Label emailField;
        private Label birthdayField;
        private Label phoneField;
        private Button viewBtn;
        private PersonSelectField personSelectField;


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
            final ExtaFormLayout formLayout = new ExtaFormLayout();
            formLayout.setSizeUndefined();
            formLayout.setSpacing(true);

            formLayout.addComponent(new FormGroupHeader("Клиент"));

            if (!isReadOnly()) {
                personSelectField = new PersonSelectField("", "Введите или выберите имя контакта");
                personSelectField.setInputPrompt("Фамилия Имя Отчество");
                personSelectField.setPropertyDataSource(getPropertyDataSource());
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
                            personSelectField.setValue(editWin.getObjectId());
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    FormUtils.showModalWin(editWin);
                });
                personSelectField.addValueChangeListener(event -> refreshFields((Person) personSelectField.getConvertedValue()));
                formLayout.addComponent(personSelectField);

            } else {
                final Label name = new Label();
                name.setCaption("Имя");
                final Person person = (Person) getPropertyDataSource().getValue();
                if (person != null)
                    name.setValue(person.getName());
                formLayout.addComponent(name);
            }

            // Дата рождения
            birthdayField = new Label();
            birthdayField.setCaption("Дата рождения");
            formLayout.addComponent(birthdayField);
            // Телефон
            phoneField = new Label();
            phoneField.setCaption("Телефон");
            phoneField.setConverter(lookup(PhoneConverter.class));
            formLayout.addComponent(phoneField);
            // Мыло
            emailField = new Label();
            emailField.setCaption("E-Mail");
            formLayout.addComponent(emailField);

            final HorizontalLayout toolbar = new HorizontalLayout();
            final Button searchBtn = new Button("Поиск", event -> {
                final PersonSelectWindow selectWindow = new PersonSelectWindow("Выберите клиента или введите нового");
                selectWindow.addCloseListener(e -> {
                    if (selectWindow.isSelectPressed()) {
                        final Person selected = selectWindow.getSelected();
                        personSelectField.setConvertedValue(selected);
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

            viewBtn = new Button("Просмотр", event -> {
                final Person bean = (Person) getPropertyDataSource().getValue();

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
            formLayout.addComponent(toolbar);

            refreshFields(getValue());
            return formLayout;
        }

        private void refreshFields(final Person person) {
            setValue(person);

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
}
