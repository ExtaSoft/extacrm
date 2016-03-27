package ru.extas.web.contacts.salepoint;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.contacts.SalePoint_;
import ru.extas.security.SalePointSecurityFilter;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.SecuredDataContainer;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Objects;
import java.util.Optional;

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
public class SalePointField extends ExtaCustomField<SalePoint> {

    private final boolean secured;
    private SupplierSer<Company> companySupplier;
    private Container.Filter filter;

    private PopupView popupView;
    private PopupSalePointContent salePointContent;

    public SalePointField(final String caption, final String description) {
        this(caption, description, false);
    }

    /**
     * <p>Constructor for SalePointSelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public SalePointField(final String caption, final String description, final boolean secured) {
        super(caption, description);
        this.secured = secured;
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setBuffered(true);
        addValueChangeListener(e -> {
            if (popupView != null)
                popupView.markAsDirty();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        salePointContent = new PopupSalePointContent();
        popupView = new PopupView(salePointContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends SalePoint> getType() {
        return SalePoint.class;
    }

    public void changeCompany() {
        if (companySupplier != null) {
            final SalePoint salePoint = getValue();
            if (salePoint != null) {
                if (!Objects.equals(this.companySupplier.get(), salePoint.getCompany())) {
                    salePointContent.refreshFields(null);
                    markAsDirtyRecursive();
                }
            }
        }
    }

    public Container.Filter getFilter() {
        return filter;
    }

    public void setFilter(final Container.Filter filter) {
        this.filter = filter;
    }

    private class SalePointComboBox extends ComboBox {
        private static final long serialVersionUID = -8005905898383483037L;
        protected final ExtaDbContainer<SalePoint> container;

        public SalePointComboBox() {
            this("Название");
        }

        public SalePointComboBox(final String caption) {
            this(caption, "Введите или выберите название торговой точки");
        }

        protected SalePointComboBox(final String caption, final String description) {
            super(caption);

            // Преконфигурация
            setDescription(description);
            setInputPrompt("Торговая точка...");
            setWidth(15, Unit.EM);
            setImmediate(true);
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            if (secured)
                container = new SecuredDataContainer<SalePoint>(new SalePointSecurityFilter());
            else
                container = new ExtaDbContainer<>(SalePoint.class);
            container.sort(new Object[]{SalePoint_.name.getName()}, new boolean[]{true});
            setContainerFilter();

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId(SalePoint_.name.getName());
            container.setSingleSelectConverter(this);

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
            final SalePoint salePoint = (SalePoint) getConvertedValue();
            if (salePoint != null)
                if (companySupplier != null && !Objects.equals(salePoint.getCompany(), companySupplier.get()))
                    setConvertedValue(null);
        }

        protected void setContainerFilter() {
            container.removeAllContainerFilters();
            Filter fltr = null;
            if (companySupplier != null && companySupplier.get() != null)
                fltr = new Compare.Equal("company", companySupplier.get());
            if (filter != null)
                fltr = fltr != null ? new And(fltr, filter) : filter;
            if (fltr != null)
                container.addContainerFilter(fltr);
        }

    }

    private class PopupSalePointContent implements PopupView.Content {

        private Button viewBtn;
        private Label companyField;
        private Label phoneField;
        private Label addressField;
        private SalePointComboBox contactSelect;
        private Label cityField;

        @Override
        public String getMinimizedValueAsHTML() {
            final SalePoint salePoint = getValue();
            if (salePoint != null)
                return salePoint.getName();
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {
            final ExtaFormLayout container = new ExtaFormLayout();
            container.setSizeUndefined();
            container.setSpacing(true);

            container.addComponent(new FormGroupHeader("Торговая точка"));
            if (!isReadOnly()) {
                contactSelect = new SalePointComboBox();
                contactSelect.setPropertyDataSource(getPropertyDataSource());
                contactSelect.setNewItemsAllowed(true);
                contactSelect.setNewItemHandler(newItemCaption -> {
                    final SalePoint defNewObj = new SalePoint();
                    if (defNewObj.getName() == null) {
                        defNewObj.setName(newItemCaption);
                    }

                    final SalePointEditForm editWin = new SalePointEditForm(defNewObj);
                    editWin.setCompanySupplier(companySupplier);
                    editWin.setModified(true);

                    editWin.addCloseFormListener(event -> {
                        if (editWin.isSaved()) {
                            contactSelect.refreshContainer();
                            contactSelect.setConvertedValue(editWin.getEntity());
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    FormUtils.showModalWin(editWin);
                });
                contactSelect.addValueChangeListener(event -> refreshFields((SalePoint) contactSelect.getConvertedValue()));
                container.addComponent(contactSelect);
            } else {
                final Label name = new Label();
                name.setCaption("Название");
                final SalePoint salePoint = getValue();
                if (salePoint != null)
                    name.setValue(salePoint.getName());
                container.addComponent(name);
            }

            // Компания
            companyField = new Label();
            companyField.setCaption("Компания");
            container.addComponent(companyField);
            // Телефон
            phoneField = new Label();
            phoneField.setCaption("Телефон");
            phoneField.setConverter(lookup(PhoneConverter.class));
            container.addComponent(phoneField);
            // Адрес
            cityField = new Label();
            cityField.setCaption("Город");
            container.addComponent(cityField);

            addressField = new Label();
            addressField.setCaption("Адрес");
            container.addComponent(addressField);
            refreshFields(getValue());

            final HorizontalLayout toolbar = new HorizontalLayout();
            viewBtn = new Button("Просмотр", event -> {
                final SalePoint salePoint = (SalePoint) getPropertyDataSource().getValue();
                final SalePointEditForm editWin = new SalePointEditForm(salePoint);
                editWin.setReadOnly(isReadOnly());
                editWin.addCloseFormListener(e -> {
                    if (editWin.isSaved()) {
                        refreshFields(salePoint);
                    }
                    popupView.setPopupVisible(true);
                });
                popupView.setPopupVisible(false);
                FormUtils.showModalWin(editWin);
            });
            viewBtn.setDescription("Открыть форму ввода/редактирования торговой точки");
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            viewBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(viewBtn);

            if (!isReadOnly()) {
                final Button searchBtn = new Button("Поиск", event -> {
                    final SalePointSelectWindow selectWindow = new SalePointSelectWindow("Выберите торговую точку или введите новую", companySupplier);
                    selectWindow.addCloseListener(e -> {
                        if (selectWindow.isSelectPressed()) {
                            contactSelect.setConvertedValue(selectWindow.getSelected().stream().findFirst().orElse(null));
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    selectWindow.showModal();

                });
                searchBtn.setDescription("Открыть форму для поиска и выбора торговой точки");
                searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
                searchBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
                searchBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
                toolbar.addComponent(searchBtn);
            }

            container.addComponent(toolbar);
            return container;
        }

        public void refreshFields(final SalePoint salePoint) {
            setValue(salePoint);

            final BeanItem<SalePoint> beanItem = new BeanItem<>(Optional.ofNullable(salePoint).orElse(new SalePoint()));
            beanItem.addNestedProperty("company.name");
            beanItem.addNestedProperty("posAddress.cityWithType");
            beanItem.addNestedProperty("posAddress.value");
            if (viewBtn != null)
                viewBtn.setEnabled(salePoint != null);
            // Компания
            if (companyField != null) companyField.setPropertyDataSource(beanItem.getItemProperty("company.name"));
            // Телефон
            if (phoneField != null) phoneField.setPropertyDataSource(beanItem.getItemProperty("phone"));
            // Адрес
            if (cityField != null) cityField.setPropertyDataSource(beanItem.getItemProperty("posAddress.cityWithType"));
            if (addressField != null)
                addressField.setPropertyDataSource(beanItem.getItemProperty("posAddress.value"));
        }
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
