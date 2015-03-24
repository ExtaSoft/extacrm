package ru.extas.web.contacts.legalentity;

import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaJpaContainer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Objects;
import java.util.Optional;

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
public class LegalEntityField extends CustomField<LegalEntity> {

    private SupplierSer<Company> companySupplier;

    private PopupView popupView;
    private PopupLegalEntityContent entityContent;

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public LegalEntityField(final String caption) {
        this(caption, "");
    }

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public LegalEntityField(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        entityContent = new PopupLegalEntityContent();
        popupView = new PopupView(entityContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    public void changeCompany() {
        if (companySupplier != null) {
            final LegalEntity legalEntity = getValue();
            if (legalEntity != null) {
                if (!Objects.equals(this.companySupplier.get(), legalEntity.getCompany())) {
                    entityContent.refreshFields();
                    markAsDirtyRecursive();
                }
            }
        }
    }

    private static class LESelectField extends ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected final ExtaJpaContainer<LegalEntity> container;
        private final SupplierSer<Company> companySupplier;

        protected LESelectField(final String caption, final SupplierSer<Company> companySupplier) {
            this(caption, "Выберите существующее юр. лицо или введите новое", companySupplier);
        }

        protected LESelectField(final String caption, final String description, final SupplierSer<Company> companySupplier) {
            super(caption);
            this.companySupplier = companySupplier;

            // Преконфигурация
            setWidth(15, Unit.EM);
            setDescription(description);
            setInputPrompt("ООО \"Рога и Копыта\"");
            setImmediate(true);
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            container = new ExtaJpaContainer<>(LegalEntity.class);
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
            final LegalEntity legalEntity = (LegalEntity) getConvertedValue();
            if (legalEntity != null)
                if (companySupplier != null && !Objects.equals(legalEntity.getCompany(), companySupplier.get()))
                    setConvertedValue(null);
        }

        protected void setContainerFilter() {
            container.removeAllContainerFilters();
            if (companySupplier != null)
                container.addContainerFilter(new Compare.Equal("company", companySupplier.get()));
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends LegalEntity> getType() {
        return LegalEntity.class;
    }

    private class PopupLegalEntityContent implements PopupView.Content {

        private PopupForm form;

        @Override
        public String getMinimizedValueAsHTML() {
            final LegalEntity legalEntity = getValue();
            if (legalEntity != null)
                return legalEntity.getName();
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {

            return this.form = new PopupForm(LegalEntityField.this, popupView, companySupplier);
        }

        public void refreshFields() {
            form.refreshFields(null);
        }
    }

    public static class PopupForm extends ExtaFormLayout {
        private final SupplierSer<Company> companySupplier;
        private final Field field;
        private LESelectField selectField;
        private final Label emailField;
        private final Label innField;
        private final Label phoneField;
        private final Button viewBtn;

        public PopupForm(final Field field, final PopupView popupView, final SupplierSer<Company> companySupplier) {

            this.field = field;
            this.companySupplier = companySupplier;

            setSizeUndefined();
            setSpacing(true);

            addComponent(new FormGroupHeader("Юридическое лицо"));

            if (!field.isReadOnly()) {
                selectField = new LESelectField("Название", "Введите или выберите название юридического лица", companySupplier);
                selectField.setValue(field.getValue());
                selectField.setNewItemsAllowed(true);
                selectField.setNewItemHandler(newItemCaption -> {
                    final LegalEntity newObj = new LegalEntity();
                    newObj.setName(newItemCaption);

                    final LegalEntityEditForm editWin = new LegalEntityEditForm(newObj);
                    editWin.setModified(true);

                    editWin.addCloseFormListener(event -> {
                        if (editWin.isSaved()) {
                            selectField.refreshContainer();
                            selectField.setValue(editWin.getEntityId());
                        }
                        popupView.setPopupVisible(true);
                    });
                    popupView.setPopupVisible(false);
                    FormUtils.showModalWin(editWin);
                });
                selectField.addValueChangeListener(event -> refreshFields((LegalEntity) selectField.getConvertedValue()));
                addComponent(selectField);
            } else {
                final Label name = new Label();
                name.setCaption("Название");
                final LegalEntity legalEntity = (LegalEntity) field.getValue();
                if (legalEntity != null)
                    name.setValue(legalEntity.getName());
                addComponent(name);
            }

            // Телефон
            phoneField = new Label();
            phoneField.setCaption("Телефон");
            phoneField.setConverter(lookup(PhoneConverter.class));
            addComponent(phoneField);
            // Мыло
            emailField = new Label();
            emailField.setCaption("E-Mail");
            addComponent(emailField);

            // ИНН
            innField = new Label();
            innField.setCaption("ИНН");
            addComponent(innField);

            final HorizontalLayout toolbar = new HorizontalLayout();
            viewBtn = new Button("Просмотр", event -> {
                final LegalEntity bean = (LegalEntity) selectField.getConvertedValue();

                final LegalEntityEditForm editWin = new LegalEntityEditForm(bean);
                editWin.setModified(true);
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
            viewBtn.setDescription("Открыть форму ввода/редактирования юр. лица");
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            viewBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(viewBtn);

            if (!field.isReadOnly()) {
                final Button searchBtn = new Button("Поиск", event -> {
                    final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберете юр. лицо или введите новое");
                    selectWindow.addCloseListener(e -> {
                        if (selectWindow.isSelectPressed()) {
                            final LegalEntity selected = selectWindow.getSelected().stream().findFirst().orElse(null);
                            selectField.setConvertedValue(selected);
                        }
                        popupView.setPopupVisible(true);
                    });
                    selectWindow.setCompanySupplier(this.companySupplier);
                    popupView.setPopupVisible(false);
                    selectWindow.showModal();

                });
                searchBtn.setDescription("Открыть форму для поиска и выбора юр. лица");
                searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
                searchBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
                searchBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
                toolbar.addComponent(searchBtn);
            }

            addComponent(toolbar);

            refreshFields((LegalEntity) field.getValue());
        }

        public void refreshFields(final LegalEntity legalEntity) {
            field.setValue(legalEntity);

            final BeanItem<LegalEntity> beanItem = new BeanItem<>(Optional.ofNullable(legalEntity).orElse(new LegalEntity()));
            if (viewBtn != null)
                viewBtn.setEnabled(legalEntity != null);
            // Телефон
            if (phoneField != null) phoneField.setPropertyDataSource(beanItem.getItemProperty("phone"));
            // Мыло
            if (emailField != null) emailField.setPropertyDataSource(beanItem.getItemProperty("email"));
            // ИНН
            if (innField != null) innField.setPropertyDataSource(beanItem.getItemProperty("inn"));
        }
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
