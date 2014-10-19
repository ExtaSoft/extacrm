package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.converters.PhoneConverter;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Выбор компании с возможностью добавления нового
 * <p/>
 * Date: 12.09.13
 * Time: 12:15
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CompanySelect extends CustomField<Company> {

    private PopupView popupView;
    private PopupCompanyContent companyContent;

    public CompanySelect(final String caption) {
        this(caption, "Введите или укажите компанию");
    }

    public CompanySelect(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setRequiredError(String.format("Поле '%s' не может быть пустым", caption));
        setBuffered(true);
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

    private class CompanyComboBox extends ComboBox {

        protected ExtaDataContainer<Company> container;

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

            // Инициализация контейнера
            container = new ExtaDataContainer<>(Company.class);
            container.sort(new Object[]{"name"}, new boolean[]{true});

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
            container.refresh();
        }
    }

    private class PopupCompanyContent implements PopupView.Content {

        private CompanyComboBox select;
        private Link wwwField;
        private Label regionField;
        private Label cityField;
        private Button viewBtn;

        @Override
        public String getMinimizedValueAsHTML() {
            final Company company = getValue();
            if (company != null)
                return company.getName();
            else
                return "Нажмите для выбора или ввода компании...";
        }

        @Override
        public Component getPopupComponent() {
            final ExtaFormLayout container = new ExtaFormLayout();
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
                            select.setValue(editWin.getObjectId());
                        }
                    });
                    FormUtils.showModalWin(editWin);
                });
                select.addValueChangeListener(event -> refreshFields((Company) select.getConvertedValue()));
                container.addComponent(select);
            } else {
                final Label name = new Label();
                final Company company = getValue();
                if (company != null)
                    name.setValue(company.getName());
                container.addComponent(name);
            }

            // Сайт
            wwwField = new Link();
            wwwField.setTargetName("_blank");
            wwwField.setCaption("www");
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

            HorizontalLayout toolbar = new HorizontalLayout();
            viewBtn = new Button("Просмотр", event -> {
                final Company company = getValue();
                final CompanyEditForm editWin = new CompanyEditForm(company);
                editWin.addCloseFormListener(e -> {
                    if (editWin.isSaved()) {
                        refreshFields(company);
                    }
                });
                FormUtils.showModalWin(editWin);
            });
            viewBtn.setDescription("Открыть форму ввода/редактирования торговой точки");
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

        public void refreshFields(Company company) {
            setValue(company);
            if (viewBtn != null) {
                viewBtn.setEnabled(company != null);
            }
            if (company == null) {
                company = new Company();
            }

            final BeanItem<Company> beanItem = new BeanItem<>(company);

            if (wwwField != null) {
                wwwField.setCaption(company.getWww());
                wwwField.setResource(new ExternalResource(company.getWww()));
            }
            if (regionField != null) regionField.setPropertyDataSource(beanItem.getItemProperty("region"));
            if (cityField != null) cityField.setPropertyDataSource(beanItem.getItemProperty("city"));
        }
    }

}