package ru.extas.web.commons;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.model.common.Comment;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.users.LoginToUserNameConverter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле формы для управления комментариями
 *
 * @author Valery Orlov
 *         Date: 25.09.2014
 *         Time: 18:31
 */
public class CommentsField<TComment extends Comment> extends CustomField<List> {

    private final Class<TComment> commentClass;
    private BeanItemContainer<TComment> container;
    private ItemGrid commentsContainer;

    public CommentsField(Class<TComment> commentClass) {
        this.commentClass = commentClass;
        setBuffered(true);
    }

    @Override
    protected Component initContent() {
        final Property dataSource = getPropertyDataSource();
        final List<TComment> list = dataSource != null ? (List<TComment>) dataSource.getValue() : new ArrayList<>();
        container = new BeanItemContainer<>(commentClass);
        if (list != null) {
            container.addAll(list);
        }
        VerticalLayout root = new VerticalLayout();

        commentsContainer = new ItemGrid();
        commentsContainer.setColumns(1);
        commentsContainer.setSelectable(false);
        commentsContainer.setContainerDataSource(container);
        commentsContainer.setItemGenerator((pSource, pItemId) -> new ItemComponent(pItemId));
        root.addComponent(commentsContainer);

        Button addBtn = new Button("Оставить комментарий", FontAwesome.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addClickListener(e -> {
            try {
                TComment newComment = commentClass.newInstance();
                container.addBean(newComment);
            } catch (Throwable ex) {
            }
        });
        root.addComponent(addBtn);

        return root;
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private class ItemComponent extends VerticalLayout {

        private boolean isNew;
        private final HorizontalLayout toolbar;
        private final RichTextArea textArea;
        private final Label text;

        public ItemComponent(Object itemId) {
            BeanItem<TComment> item = container.getItem(itemId);
            TComment comment = item.getBean();

            if (isNew = comment.getCreatedAt() == null) {
                comment.setCreatedBy(lookup(UserManagementService.class).getCurrentUserLogin());
                comment.setCreatedAt(DateTime.now());
            }

            Label icon = new Label(FontAwesome.COMMENT_O.getHtml(), ContentMode.HTML);
            icon.addStyleName(ExtaTheme.LABEL_COLORED);
            Label user = new Label(item.getItemProperty("createdBy"), ContentMode.HTML);
            user.addStyleName(ExtaTheme.LABEL_COLORED);
            user.addStyleName(ExtaTheme.LABEL_BOLD);
            user.setConverter(lookup(LoginToUserNameConverter.class));
            Label crTime = new Label(item.getItemProperty("createdAt"));
            crTime.addStyleName(ExtaTheme.LABEL_COLORED);

            String userLogin = lookup(UserManagementService.class).getCurrentUserLogin();
            final boolean ownComment = userLogin.equals(item.getBean().getCreatedBy());

            Button editBtn = new Button("Редактировать", FontAwesome.PENCIL);
            editBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            editBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            editBtn.setVisible(ownComment);
            editBtn.addClickListener(e -> switchEditMode(true));

            Button delBtn = new Button("Удалить", FontAwesome.TRASH_O);
            delBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
            delBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
            delBtn.setVisible(ownComment);
            delBtn.addClickListener(e ->
                    ConfirmDialog.show(UI.getCurrent(), "Удаление комментария...", "Вы уверены что необходимо удлить комментарий?",
                            "Удалить", "Оставить", dialog -> {
                                if (dialog.isConfirmed()) {
                                    container.removeItem(itemId);
                                    setValue(container.getItemIds());
                                }
                            }));

            HorizontalLayout header = new HorizontalLayout();
            header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            header.addComponents(icon, crTime, user, editBtn, delBtn);
            header.setSpacing(true);
            addComponent(header);

            final Property textProp = item.getItemProperty("text");
            text = new Label(textProp, ContentMode.HTML);
            addComponent(text);

            textArea = new RichTextArea(textProp);
            textArea.setNullRepresentation("");
            textArea.setBuffered(true);
            addComponent(textArea);

            Button saveBtn = new Button("Сохранить", Fontello.OK);
            saveBtn.addStyleName(ExtaTheme.BUTTON_PRIMARY);
            saveBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            saveBtn.addClickListener(e -> {
                textArea.commit();
                setValue(container.getItemIds());
                switchEditMode(false);
                isNew = false;
            });
            Button cancelBtn = new Button("Отменить", Fontello.CANCEL);
            cancelBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            cancelBtn.addClickListener(e -> {
                textArea.discard();
                if (isNew)
                    container.removeItem(itemId);
                switchEditMode(false);
                isNew = false;
            });
            toolbar = new HorizontalLayout(saveBtn, cancelBtn);
            addComponent(toolbar);

            switchEditMode(isNew);
        }

        private void switchEditMode(boolean isEdit) {
            text.setVisible(!isEdit);
            textArea.setVisible(isEdit);
            toolbar.setVisible(isEdit);
        }

    }
}
