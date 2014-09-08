package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.converters.PhoneConverter;

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

    private LESelectField selectField;
    private Label emailField;
    private Label innField;
    private Label phoneField;
    private Button viewBtn;

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public LegalEntitySelect(final String caption) {
        this(caption, "");
    }

    /**
     * <p>Constructor for LegalEntitySelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public LegalEntitySelect(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
        addStyleName(ExtaTheme.BORDERED_COMPONENT);
    }

    /** {@inheritDoc} */
    @Override
    protected Component initContent() {
        VerticalLayout container = new VerticalLayout();
        container.setSpacing(true);

        CssLayout nameLay = new CssLayout();
        nameLay.addStyleName(ExtaTheme.LAYOUT_COMPONENT_GROUP);

        selectField = new LESelectField("Название", "Введите или выберите название юридического лица");
        selectField.setInputPrompt("ООО \"Рога и Копыта\"");
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
        nameLay.addComponent(selectField);

        Button searchBtn = new Button("Поиск", event -> {

            final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберите клиента или введите нового", null);
            selectWindow.addCloseListener(e -> {
                if (selectWindow.isSelectPressed()) {
                    final LegalEntity selected = selectWindow.getSelected();
                    selectField.setConvertedValue(selected);
                }
            });
            selectWindow.showModal();

        });
        searchBtn.setIcon(Fontello.SEARCH_OUTLINE);
        searchBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        nameLay.addComponent(searchBtn);

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
        viewBtn.setIcon(Fontello.EDIT_3);
        viewBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        nameLay.addComponent(viewBtn);
        container.addComponent(nameLay);

        HorizontalLayout fieldsContainer = new HorizontalLayout();
        fieldsContainer.setSpacing(true);
        // Телефон
        phoneField = new Label();
        phoneField.setCaption("Телефон");
        phoneField.setConverter(lookup(PhoneConverter.class));
        fieldsContainer.addComponent(phoneField);
        // Мыло
        emailField = new Label();
        emailField.setCaption("E-Mail");
        fieldsContainer.addComponent(emailField);

        // ИНН
        innField = new Label();
        innField.setCaption("ИНН");
        fieldsContainer.addComponent(innField);
        container.addComponent(fieldsContainer);

        refreshFields((LegalEntity) getPropertyDataSource().getValue());
        return container;
    }

    private void refreshFields(LegalEntity legalEntity) {
        setValue(legalEntity);

        if (legalEntity == null) {
            viewBtn.setEnabled(false);
            legalEntity = new LegalEntity();
        } else
            viewBtn.setEnabled(true);

        BeanItem<LegalEntity> beanItem = new BeanItem<>(legalEntity);
        // Телефон
        phoneField.setPropertyDataSource(beanItem.getItemProperty("phone"));
        // Мыло
        emailField.setPropertyDataSource(beanItem.getItemProperty("email"));
        // ИНН
        innField.setPropertyDataSource(beanItem.getItemProperty("inn"));
    }

    private class LESelectField extends AbstractContactSelect<LegalEntity> {

        protected LESelectField(final String caption) {
            super(caption, LegalEntity.class);
        }

        protected LESelectField(final String caption, final String description) {
            super(caption, description, LegalEntity.class);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends LegalEntity> getType() {
        return LegalEntity.class;
    }
}
