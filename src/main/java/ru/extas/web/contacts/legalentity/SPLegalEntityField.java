package ru.extas.web.contacts.legalentity;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.ListContainer;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.converters.PhoneConverter;

import java.util.Optional;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 31.03.2015
 *         Time: 18:50
 */
public class SPLegalEntityField extends ExtaCustomField<LegalEntity> {

    private SupplierSer<SalePoint> salePointSupplier;

    private PopupView popupView;
    private PopupLegalEntityContent entityContent;

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public SPLegalEntityField(final String caption) {
        this(caption, "");
    }

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption     a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public SPLegalEntityField(final String caption, final String description) {
        super(caption, description);
        setBuffered(true);
    }

    public SupplierSer<SalePoint> getSalePointSupplier() {
        return salePointSupplier;
    }

    public void setSalePointSupplier(final SupplierSer<SalePoint> salePointSupplier) {
        this.salePointSupplier = salePointSupplier;
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

    public void changeSalePoint() {
        if (salePointSupplier != null) {
            final SalePoint salePoint = salePointSupplier.get();
            final LegalEntity legalEntity = getValue();
            if (legalEntity != null && salePoint != null) {
                entityContent.refreshFields();
                markAsDirtyRecursive();
            }
        }
    }

    private class LESelectField extends ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected final ListContainer<LegalEntity> container;

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
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            container = new ListContainer<>(LegalEntity.class);
            refreshContainer();

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");

            // Функционал добавления нового контакта
            setNullSelectionAllowed(false);
            setNewItemsAllowed(false);
        }

        /**
         * <p>refreshContainer.</p>
         */
        public void refreshContainer() {
            container.removeAllItems();
            if (salePointSupplier != null) {
                final SalePoint salePoint = salePointSupplier.get();
                if (salePoint != null) {
                    container.addAll(salePoint.getLegalEntities());
//                    LegalEntity legalEntity = (LegalEntity) getConvertedValue();
//                    if(!salePoint.getLegalEntities().contains(legalEntity))
//                        setConvertedValue(null);
                }
            }
            container.sort(new Object[]{"name"}, new boolean[]{true});
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

            return this.form = new PopupForm(SPLegalEntityField.this, popupView);
        }

        public void refreshFields() {
            form.refreshFields(null);
        }
    }

    public class PopupForm extends ExtaFormLayout {
        private final Field field;
        private LESelectField selectField;
        private final Label emailField;
        private final Label innField;
        private final Label phoneField;
        private final Button viewBtn;

        public PopupForm(final Field field, final PopupView popupView) {

            this.field = field;

            setSizeUndefined();
            setSpacing(true);

            addComponent(new FormGroupHeader("Юридическое лицо"));

            if (!field.isReadOnly()) {
                selectField = new LESelectField("Название", "Введите или выберите название юридического лица");
                selectField.setValue(field.getValue());
                selectField.setNewItemsAllowed(false);
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

            addComponent(toolbar);

            refreshFields((LegalEntity) field.getValue());
        }

        public void refreshFields(final LegalEntity legalEntity) {
            field.setValue(legalEntity);

            selectField.refreshContainer();

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

}
