package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Objects;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Компонент выбора торговой точки
 *
 * @author Valery Orlov
 *         Date: 21.02.14
 *         Time: 12:53
 * @version $Id: $Id
 * @since 0.3
 */
public class SalePointSelect extends CustomField<SalePoint> {

    private Button viewBtn;
    private Label companyField;
    private Label phoneField;
    private Label adressField;
    private Company company;
    private SalePointComboBox contactSelect;

    /**
     * <p>Constructor for SalePointSelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param company     a {@link ru.extas.model.contacts.Company} object.
     */
    public SalePointSelect(final String caption, final String description, final Company company) {
        this.company = company;
        setCaption(caption);
        setDescription(description);
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setBuffered(true);
        addStyleName(ExtaTheme.BORDERED_COMPONENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {

        final VerticalLayout container = new VerticalLayout();
        container.setSpacing(true);

        final CssLayout nameLay = new CssLayout();
        nameLay.addStyleName(ExtaTheme.LAYOUT_COMPONENT_GROUP);

        if (!isReadOnly()) {
            contactSelect = new SalePointComboBox();
            contactSelect.setPropertyDataSource(getPropertyDataSource());
            contactSelect.setNewItemsAllowed(true);
            contactSelect.setNewItemHandler(new AbstractSelect.NewItemHandler() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings({"unchecked"})
                @Override
                public void addNewItem(final String newItemCaption) {
                    final SalePoint defNewObj = new SalePoint();
                    if (defNewObj.getName() == null) {
                        defNewObj.setName(newItemCaption);
                    }
                    defNewObj.setCompany(company);

                    final SalePointEditForm editWin = new SalePointEditForm(defNewObj);
                    editWin.setModified(true);

                    editWin.addCloseFormListener(event -> {
                        if (editWin.isSaved()) {
                            contactSelect.refreshContainer();
                            contactSelect.setValue(editWin.getObjectId());
                        }
                    });
                    FormUtils.showModalWin(editWin);
                }
            });
            contactSelect.addValueChangeListener(event -> refreshFields((SalePoint) contactSelect.getConvertedValue()));
            nameLay.addComponent(contactSelect);

            final Button searchBtn = new Button("Поиск", event -> {

                final SalePointSelectWindow selectWindow = new SalePointSelectWindow("Выберите клиента или введите нового", company);
                selectWindow.addCloseListener(e -> {
                    if (selectWindow.isSelectPressed()) {
                        contactSelect.setConvertedValue(selectWindow.getSelected());
                    }
                });
                selectWindow.showModal();

            });
            searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
            searchBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            nameLay.addComponent(searchBtn);

            viewBtn = new Button("Просмотр", event -> {
                final SalePoint salePoint = (SalePoint) getPropertyDataSource().getValue();

                final SalePointEditForm editWin = new SalePointEditForm(salePoint);

                editWin.addCloseFormListener(e -> {
                    if (editWin.isSaved()) {
                        refreshFields(salePoint);
                    }
                });
                FormUtils.showModalWin(editWin);
            });
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            nameLay.addComponent(viewBtn);

        } else {
            final Label name = new Label();
            final SalePoint salePoint = (SalePoint) getPropertyDataSource().getValue();
            if (salePoint != null)
                name.setValue(salePoint.getName());
            nameLay.addComponent(name);
        }

        container.addComponent(nameLay);

        final HorizontalLayout fieldsContainer = new HorizontalLayout();
        fieldsContainer.setSpacing(true);
        // Компания
        companyField = new Label();
        companyField.setCaption("Компания");
        fieldsContainer.addComponent(companyField);
        // Телефон
        phoneField = new Label();
        phoneField.setCaption("Телефон");
        phoneField.setConverter(lookup(PhoneConverter.class));
        fieldsContainer.addComponent(phoneField);
        // Адрес
        adressField = new Label();
        adressField.setCaption("Адрес");
        fieldsContainer.addComponent(adressField);
        refreshFields((SalePoint) getPropertyDataSource().getValue());

        container.addComponent(fieldsContainer);

        return container;
    }

    private void refreshFields(SalePoint salePoint) {
        setValue(salePoint);
        if (viewBtn != null) {
            viewBtn.setEnabled(salePoint != null);
        }
        if (salePoint == null) {
            salePoint = new SalePoint();
        }

        final BeanItem<SalePoint> personItem = new BeanItem<>(salePoint);
        personItem.addNestedProperty("company.name");
        personItem.addNestedProperty("regAddress.streetBld");
        // Компания
        companyField.setPropertyDataSource(personItem.getItemProperty("company.name"));
        // Телефон
        phoneField.setPropertyDataSource(personItem.getItemProperty("phone"));
        // Адрес
        adressField.setPropertyDataSource(personItem.getItemProperty("regAddress.streetBld"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends SalePoint> getType() {
        return SalePoint.class;
    }

    public void setCompany(Company company) {
        if (!Objects.equals(this.company, company)) {
            this.company = company;
            contactSelect.refreshContainer();
        }
    }

    private class SalePointComboBox extends ComboBox {
        private static final long serialVersionUID = -8005905898383483037L;
        protected ExtaDataContainer<SalePoint> container;

        public SalePointComboBox() {
            this("", "Введите или выберите название торговой точки");
        }

        protected SalePointComboBox(final String caption, final String description) {
            super(caption);

            // Преконфигурация
            setDescription(description);
            setInputPrompt("Торговая точка...");
            setWidth(25, Unit.EM);
            setImmediate(true);

            // Инициализация контейнера
            container = new ExtaDataContainer<>(SalePoint.class);
            container.sort(new Object[]{"name"}, new boolean[]{true});
            setContainerFilter();

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");
            setConverter(new SingleSelectConverter<SalePoint>(this));

            // Функционал добавления нового контакта
            setNullSelectionAllowed(false);
            setNewItemsAllowed(false);
        }

        /**
         * <p>refreshContainer.</p>
         */
        public void refreshContainer() {
            setContainerFilter();
            container.refresh();
            if (company != null && !Objects.equals(getConvertedValue(), company))
                setConvertedValue(null);
        }

        protected void setContainerFilter() {
            container.removeAllContainerFilters();
            if (company != null)
                container.addContainerFilter(new Compare.Equal("company", company));
        }

    }
}
