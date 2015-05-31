package ru.extas.web.users;

import com.google.common.base.Joiner;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.container.ExtaDbContainer;

/**
 * Выбор группы кураторов с возможностью ввода/редактирования
 *
 * @author Valery Orlov
 *         Date: 22.10.2014
 *         Time: 16:25
 */
public class CuratorsGroupField extends CustomField<CuratorsGroup> {

    private PopupView popupView;
    private PopupGroupContent entityContent;

    public CuratorsGroupField(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
    }

    @Override
    protected Component initContent() {
        entityContent = new PopupGroupContent();
        popupView = new PopupView(entityContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    private class CuratorsGroupSelectField extends ComboBox {

        private static final long serialVersionUID = -8005905898383483037L;
        protected final ExtaDbContainer<CuratorsGroup> container;

        protected CuratorsGroupSelectField(final String caption) {
            this(caption, "Выберите группу курирующих сотрудников");
        }

        protected CuratorsGroupSelectField(final String caption, final String description) {
            super(caption);

            // Преконфигурация
            setWidth(15, Unit.EM);
            setDescription(description);
            setInputPrompt("Имя");
            setImmediate(true);
            setScrollToSelectedItem(true);

            // Инициализация контейнера
            container = new ExtaDbContainer<>(CuratorsGroup.class);
            container.sort(new Object[]{"name"}, new boolean[]{true});

            // Устанавливаем контент выбора
            setFilteringMode(FilteringMode.CONTAINS);
            setContainerDataSource(container);
            setItemCaptionMode(ItemCaptionMode.PROPERTY);
            setItemCaptionPropertyId("name");
            container.setSingleSelectConverter(this);

            // Функционал добавления нового контакта
            setNullSelectionAllowed(false);
            setNewItemsAllowed(false);
        }

    }

    private class PopupGroupContent implements PopupView.Content {
        private CuratorsGroupSelectField selectField;
        private Button viewBtn;
        private Label descField;
        private Label empsField;

        @Override
        public String getMinimizedValueAsHTML() {
            final CuratorsGroup group = getValue();
            if (group != null)
                return group.getName();
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {

            final ExtaFormLayout formLayout = new ExtaFormLayout();
            formLayout.setSizeUndefined();
            formLayout.setSpacing(true);

            formLayout.addComponent(new FormGroupHeader("Группа кураторов"));

            selectField = new CuratorsGroupSelectField("Имя", "Введите или выберите имя сотрудника");
            selectField.setBuffered(true);
            selectField.setPropertyDataSource(getPropertyDataSource());
            selectField.setNewItemsAllowed(false);
            selectField.addValueChangeListener(event -> refreshFields((CuratorsGroup) selectField.getConvertedValue()));
            formLayout.addComponent(selectField);

            if (isReadOnly()) {
                selectField.setReadOnly(true);
                selectField.setWidthUndefined();
            }

            // Описание
            descField = new Label();
            descField.setCaption("Описание");
            formLayout.addComponent(descField);
            // Список сотрудников входящих в группу
            empsField = new Label();
            empsField.setCaption("Состав группы");
            empsField.setContentMode(ContentMode.PREFORMATTED);
            formLayout.addComponent(empsField);

            final HorizontalLayout toolbar = new HorizontalLayout();
            viewBtn = new Button("Просмотр", event -> {
                final CuratorsGroup bean = (CuratorsGroup) selectField.getConvertedValue();

                final CuratorsGroupEditForm editWin = new CuratorsGroupEditForm(bean);
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
            viewBtn.setDescription("Открыть форму ввода/редактирования группы");
            viewBtn.setIcon(Fontello.EDIT_3);
            viewBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            viewBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            toolbar.addComponent(viewBtn);

            formLayout.addComponent(toolbar);

            refreshFields(getValue());
            return formLayout;
        }

        public void refreshFields(final CuratorsGroup group) {
            setValue(group);

            if (group != null) {
                if (viewBtn != null) viewBtn.setEnabled(group != null);
                // Описание
                if (descField != null) descField.setValue(group.getDescription());
                // Список сотрудников входящих в группу
                if (empsField != null) {
                    empsField.setValue(
                            Joiner.on(System.lineSeparator())
                                    .join(group.getCurators().stream().map(e -> e.getName()).iterator()));
                }
            }
        }
    }

    @Override
    public Class<? extends CuratorsGroup> getType() {
        return CuratorsGroup.class;
    }

}
