package ru.extas.web.contacts.company;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Company_;
import ru.extas.security.CompanySecurityFilter;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.WebSiteLinkField;

import java.util.Objects;
import java.util.Optional;

/**
 * Выбор компании с возможностью добавления нового
 * <p>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompanyField extends CustomField<Company> {

    private final boolean secured;
    private PopupView popupView;
    private PopupCompanyContent companyContent;
    private SupplierSer<String> regionSupplier;
    private Container.Filter filter;

    public CompanyField(final String caption) {
        this(caption, "Введите или выберите компанию", false);
    }

    public CompanyField(final String caption, final String description) {
        this(caption, "Введите или выберите компанию", false);
    }

    public CompanyField(final String caption, boolean secured) {
        this(caption, "Введите или выберите компанию", secured);
    }

    public CompanyField(final String caption, final String description, boolean secured) {
        this.secured = secured;
        setCaption(caption);
        setDescription(description);
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setBuffered(true);
        addValueChangeListener(e -> {
            if (popupView != null)
                popupView.markAsDirty();
        });
    }


    @Override
    protected Component initContent() {
        companyContent = new PopupCompanyContent();
        popupView = new PopupView(companyContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    @Override
    public Class<? extends Company> getType() {
        return Company.class;
    }

    public void changeRegion() {
        if (regionSupplier != null) {
            final Company company = getValue();
            if (company != null) {
                if (!Objects.equals(this.regionSupplier.get(), company.getRegion())) {
                    companyContent.refreshFields(null);
                    markAsDirtyRecursive();
                }
            }
        }
    }

    public SupplierSer<String> getRegionSupplier() {
        return regionSupplier;
    }

    public void setRegionSupplier(SupplierSer<String> regionSupplier) {
        this.regionSupplier = regionSupplier;
    }

    public Container.Filter getFilter() {
        return filter;
    }

    public void setFilter(Container.Filter filter) {
        this.filter = filter;
    }

    private class CompanyComboBox extends ComboBox {

        protected final ExtaJpaContainer<Company> container;

        /**
         * <p>Constructor for CompanySelect.</p>
         *
         * @param caption a {@link java.lang.String} object.
         */
        public CompanyComboBox(final String caption) {
            this(caption, caption);
        }

        /**
         * <p>Constructor for CompanySelect.</p>
         *
         * @param caption     a {@link java.lang.String} object.
         * @param description a {@link java.lang.String} object.
         */
        public CompanyComboBox(final String caption, final String description) {
            super(caption);

            // Преконфигурация
            setDescription(description);
            setInputPrompt("Название компании...");
            setWidth(25, Unit.EM);
            setImmediate(true);
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            if (secured)
                container = new SecuredDataContainer<Company>(new CompanySecurityFilter());
            else
                container = new ExtaJpaContainer<>(Company.class);
            container.sort(new Object[]{"name"}, new boolean[]{true});
            setContainerFilter();

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");
            setConverter(new SingleSelectConverter<Company>(this));

            // Функционал добавления нового контакта
            setNullSelectionAllowed(false);
        }

        public void refreshContainer() {
            setContainerFilter();
            container.refresh();
            final Company company = (Company) getConvertedValue();
            if (company != null)
                if (regionSupplier != null && !Objects.equals(company.getRegion(), regionSupplier.get()))
                    setConvertedValue(null);
        }

        protected void setContainerFilter() {
            container.removeAllContainerFilters();
            Container.Filter fltr = null;
            if (regionSupplier != null && regionSupplier.get() != null)
                fltr = new Compare.Equal(Company_.region.getName(), regionSupplier.get());
            if (filter != null)
                fltr = fltr != null ? new And(fltr, filter) : filter;
            if (fltr != null)
                container.addContainerFilter(fltr);
        }
    }

    private class PopupCompanyContent implements PopupView.Content {

        private CompanyComboBox select;
        private WebSiteLinkField wwwField;
        private Label regionField;
        private Label cityField;
        private Button viewBtn;

        @Override
        public String getMinimizedValueAsHTML() {
            final Company company = getValue();
            if (company != null)
                return company.getName();
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {
            final ExtaFormLayout container = new ExtaFormLayout();
            container.setSizeUndefined();
            container.setSpacing(true);

            container.addComponent(new FormGroupHeader("Компания"));
            if (!isReadOnly()) {
                select = new CompanyComboBox("Название");
                select.setPropertyDataSource(getPropertyDataSource());
                select.setNewItemsAllowed(true);
                select.setNewItemHandler(newItemCaption -> {
                    final Company newObj = new Company();
                    newObj.setName(newItemCaption);

                    final CompanyEditForm editWin = new CompanyEditForm(newObj);
                    editWin.setModified(true);

                    editWin.addCloseFormListener(event -> {
                        if (editWin.isSaved()) {
                            select.refreshContainer();
                            select.setValue(editWin.getEntityId());
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    FormUtils.showModalWin(editWin);
                });
                select.addValueChangeListener(event -> refreshFields((Company) select.getConvertedValue()));
                container.addComponent(select);
            } else {
                final Label name = new Label();
                name.setCaption("Название");
                final Company company = getValue();
                if (company != null)
                    name.setValue(company.getName());
                container.addComponent(name);
            }

            // Сайт
            wwwField = new WebSiteLinkField("Сайт");
            wwwField.setReadOnly(true);
            container.addComponent(wwwField);
            // Регион
            regionField = new Label();
            regionField.setCaption("Регион");
            container.addComponent(regionField);
            // Город
            cityField = new Label();
            cityField.setCaption("Город");
            container.addComponent(cityField);

            refreshFields(getValue());

            final HorizontalLayout toolbar = new HorizontalLayout();
            viewBtn = new Button("Просмотр", event -> {
                final Company company = getValue();
                final CompanyEditForm editWin = new CompanyEditForm(company);
                editWin.setReadOnly(isReadOnly());
                editWin.addCloseFormListener(e -> {
                    if (editWin.isSaved()) {
                        refreshFields(company);
                    }
                    popupView.setPopupVisible(true);
                });
                popupView.setPopupVisible(false);
                FormUtils.showModalWin(editWin);
            });
            viewBtn.setDescription("Открыть форму ввода/редактирования компании");
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            viewBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(viewBtn);

//            final Button searchBtn = new Button("Поиск", event -> {
//                final SalePointSelectWindow selectWindow = new SalePointSelectWindow("Выберите торговую точку или введите новую", company);
//                selectWindow.addCloseListener(e -> {
//                    if (selectWindow.isSelectPressed()) {
//                        select.setConvertedValue(selectWindow.getSelected());
//                    }
//                });
//                selectWindow.showModal();
//
//            });
//            searchBtn.setDescription("Открыть форму для поиска и выбора торговой точки");
//            searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
//            searchBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
//            searchBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
//            toolbar.addComponent(searchBtn);

            container.addComponent(toolbar);
            return container;
        }

        public void refreshFields(final Company company) {
            setValue(company);

            final BeanItem<Company> beanItem = new BeanItem<>(Optional.ofNullable(company).orElse(new Company()));
            if (viewBtn != null) viewBtn.setEnabled(company != null);
            if (wwwField != null) wwwField.setPropertyDataSource(beanItem.getItemProperty("www"));
            if (regionField != null) regionField.setPropertyDataSource(beanItem.getItemProperty("region"));
            if (cityField != null) cityField.setPropertyDataSource(beanItem.getItemProperty("city"));
        }
    }

}