package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Objects;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Выбор контакта - юр. лица
 * с возможностью добавления нового
 *
 * @author Valery Orlov
 *         Date: 27.03.2014
 *         Time: 0:45
 * @version $Id: $Id
 * @since 0.3.0
 */
public class LegalEntitySelect extends CustomField<LegalEntity> {

    private Company company;
    private PopupView popupView;
    private PopupLegalEntityContent entityContent;

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public LegalEntitySelect(final String caption) {
        this(caption, "", null);
    }

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public LegalEntitySelect(final String caption, final String description, final Company company) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
        this.company = company;
    }

    /** {@inheritDoc} */
    @Override
    protected Component initContent() {
        entityContent = new PopupLegalEntityContent();
        popupView = new PopupView(entityContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    public void setCompany(Company company) {
        if (!Objects.equals(this.company, company)) {
            this.company = company;
            final LegalEntity legalEntity = getValue();
            if (legalEntity != null && company != null && !legalEntity.getCompany().equals(company)) {
                entityContent.refreshFields(null);
                markAsDirtyRecursive();
            }
        }
    }

    private class LESelectField extends ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected ExtaDataContainer<LegalEntity> container;

        protected LESelectField(final String caption) {
            this(caption, "Выберите существующее юр. лицо или введите новое");
        }

        protected LESelectField(final String caption, final String description) {
            super(caption);

            // Преконфигурация
            setWidth(15, Unit.EM);
            setDescription(description);
            setInputPrompt("ООО \"Рога и Копыта\"");
            setImmediate(true);

            // Инициализация контейнера
            container = new ExtaDataContainer<>(LegalEntity.class);
            container.sort(new Object[]{"name"}, new boolean[]{true});
            setContainerFilter();

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");
            setConverter(new SingleSelectConverter<LegalEntity>(this));

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
            if(company != null && !Objects.equals(getConvertedValue(), company))
                setConvertedValue(null);
        }

        protected void setContainerFilter() {
            container.removeAllContainerFilters();
            if (company != null)
                container.addContainerFilter(new Compare.Equal("company", company));
        }

    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends LegalEntity> getType() {
        return LegalEntity.class;
    }

    private class PopupLegalEntityContent implements PopupView.Content {
        private LESelectField selectField;
        private Label emailField;
        private Label innField;
        private Label phoneField;
        private Button viewBtn;

        @Override
        public String getMinimizedValueAsHTML() {
            final LegalEntity legalEntity = getValue();
            if (legalEntity != null)
                return legalEntity.getName();
            else
                return "Нажмите для выбора или ввода юр. лица...";
        }

        @Override
        public Component getPopupComponent() {

            final ExtaFormLayout formLayout = new ExtaFormLayout();
            formLayout.setSpacing(true);

            formLayout.addComponent(new FormGroupHeader("Юридическое лицо"));

            selectField = new LESelectField("Название", "Введите или выберите название юридического лица");
            selectField.setPropertyDataSource(getPropertyDataSource());
            selectField.setNewItemsAllowed(true);
            selectField.setNewItemHandler(new AbstractSelect.NewItemHandler() {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings({"unchecked"})
                @Override
                public void addNewItem(final String newItemCaption) {
                    final LegalEntity newObj = new LegalEntity();
                    newObj.setName(newItemCaption);

                    final LegalEntityEditForm editWin = new LegalEntityEditForm(newObj);
                    editWin.setModified(true);

                    editWin.addCloseFormListener(event -> {
                        if (editWin.isSaved()) {
                            selectField.refreshContainer();
                            selectField.setValue(editWin.getObjectId());
                        }
                    });
                    FormUtils.showModalWin(editWin);
                }
            });
            selectField.addValueChangeListener(event -> refreshFields((LegalEntity) selectField.getConvertedValue()));
            formLayout.addComponent(selectField);

            // Телефон
            phoneField = new Label();
            phoneField.setCaption("Телефон");
            phoneField.setConverter(lookup(PhoneConverter.class));
            formLayout.addComponent(phoneField);
            // Мыло
            emailField = new Label();
            emailField.setCaption("E-Mail");
            formLayout.addComponent(emailField);

            // ИНН
            innField = new Label();
            innField.setCaption("ИНН");
            formLayout.addComponent(innField);

            HorizontalLayout toolbar = new HorizontalLayout();
            viewBtn = new Button("Просмотр", event -> {
                final LegalEntity bean = (LegalEntity) selectField.getConvertedValue();

                final LegalEntityEditForm editWin = new LegalEntityEditForm(bean);
                editWin.setModified(true);

                editWin.addCloseFormListener(event1 -> {
                    if (editWin.isSaved()) {
                        refreshFields(bean);
                    }
                });
                FormUtils.showModalWin(editWin);
            });
            viewBtn.setDescription("Открыть форму ввода/редактирования юр. лица");
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            viewBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(viewBtn);

            final Button searchBtn = new Button("Поиск", event -> {
                final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберите юр. лицо или введите новое", null);
                selectWindow.addCloseListener(e -> {
                    if (selectWindow.isSelectPressed()) {
                        final LegalEntity selected = selectWindow.getSelected();
                        selectField.setConvertedValue(selected);
                    }
                });
                selectWindow.showModal();

            });
            searchBtn.setDescription("Открыть форму для поиска и выбора юр. лица");
            searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
            searchBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            searchBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(searchBtn);

            formLayout.addComponent(toolbar);

            refreshFields((LegalEntity) getPropertyDataSource().getValue());
            return formLayout;
        }

        public void refreshFields(LegalEntity legalEntity) {
            setValue(legalEntity);

            if (legalEntity == null) {
                viewBtn.setEnabled(false);
                legalEntity = new LegalEntity();
            } else
                viewBtn.setEnabled(true);

            final BeanItem<LegalEntity> beanItem = new BeanItem<>(legalEntity);
            // Телефон
            if (phoneField != null) phoneField.setPropertyDataSource(beanItem.getItemProperty("phone"));
            // Мыло
            if (emailField != null) emailField.setPropertyDataSource(beanItem.getItemProperty("email"));
            // ИНН
            if (innField != null) innField.setPropertyDataSource(beanItem.getItemProperty("inn"));
        }
    }
}
