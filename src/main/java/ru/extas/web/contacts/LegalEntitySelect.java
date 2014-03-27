package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.contacts.LegalEntity;

/**
 * Выбор контакта - юр. лица
 * с возможностью добавления нового
 *
 * @author Valery Orlov
 *         Date: 27.03.2014
 *         Time: 0:45
 */
public class LegalEntitySelect extends CustomField<LegalEntity> {

    private LESelectField selectField;
    private Label emailField;
    private Label innField;
    private Label phoneField;
    private Button viewBtn;

    public LegalEntitySelect(final String caption) {
        this(caption, "");
    }

    public LegalEntitySelect(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
        addStyleName("bordered-component");
    }

    @Override
    protected Component initContent() {
        VerticalLayout container = new VerticalLayout();
        container.setSpacing(true);

        HorizontalLayout nameLay = new HorizontalLayout();

        selectField = new LESelectField("Название", "Введите или выберите название юридического лица");
        selectField.setInputPrompt("ООО \"Рога и Копыта\"");
        selectField.setPropertyDataSource(getPropertyDataSource());
        selectField.setNewItemsAllowed(true);
        selectField.setNewItemHandler(new AbstractSelect.NewItemHandler() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings({"unchecked"})
            @Override
            public void addNewItem(final String newItemCaption) {
                final BeanItem<LegalEntity> newObj;
                newObj = new BeanItem<>(new LegalEntity());
                newObj.getBean().setName(newItemCaption);
                newObj.expandProperty("actualAddress");

                final String edFormCaption = "Ввод нового юр.лица в систему";
                final LegalEntityEditForm editWin = new LegalEntityEditForm(edFormCaption, newObj);
                editWin.setModified(true);

                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            selectField.refreshContainer();
                            selectField.setValue(newObj.getBean().getId());
                        }
                    }
                });
                editWin.showModal();
            }
        });
        selectField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                refreshFields((LegalEntity) selectField.getConvertedValue());
            }
        });
        nameLay.addComponent(selectField);

        Button searchBtn = new Button("Поиск", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {

                final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберите клиента или введите нового", null);
                selectWindow.addCloseListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (selectWindow.isSelectPressed()) {
                            final LegalEntity selected = selectWindow.getSelected();
                            selectField.setConvertedValue(selected);
                        }
                    }
                });
                selectWindow.showModal();

            }
        });
        searchBtn.addStyleName("icon-search-outline");
        searchBtn.addStyleName("icon-only");
        nameLay.addComponent(searchBtn);
        nameLay.setComponentAlignment(searchBtn, Alignment.BOTTOM_LEFT);

        viewBtn = new Button("Просмотр", new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final BeanItem<LegalEntity> beanItem;
                beanItem = new BeanItem<>((LegalEntity) selectField.getConvertedValue());
                beanItem.expandProperty("actualAddress");

                final String edFormCaption = "Просмотр/Редактирование юр. лица";
                final LegalEntityEditForm editWin = new LegalEntityEditForm(edFormCaption, beanItem);
                editWin.setModified(true);

                editWin.addCloseListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            refreshFields(beanItem.getBean());
                        }
                    }
                });
                editWin.showModal();
            }
        });
        viewBtn.addStyleName("icon-edit-3");
        viewBtn.addStyleName("icon-only");
        nameLay.addComponent(viewBtn);
        nameLay.setComponentAlignment(viewBtn, Alignment.BOTTOM_LEFT);
        container.addComponent(nameLay);

        HorizontalLayout fieldsContainer = new HorizontalLayout();
        fieldsContainer.setSpacing(true);
        // Телефон
        phoneField = new Label();
        phoneField.setCaption("Телефон");
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

    @Override
    public Class<? extends LegalEntity> getType() {
        return LegalEntity.class;
    }
}
