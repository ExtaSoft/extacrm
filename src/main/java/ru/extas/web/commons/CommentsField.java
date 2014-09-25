package ru.extas.web.commons;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.vaadin.addon.itemlayout.grid.ItemGrid;
import ru.extas.model.common.Comment;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.users.LoginToUserNameConverter;

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
    private PopupView addComment;
    private PopupView addCommentPopup;

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
        commentsContainer.setItemGenerator((pSource, pItemId) -> getItemComponent((TComment) pItemId));
        root.addComponent(commentsContainer);

        addCommentPopup = new PopupView(new PopupView.Content() {
            @Override
            public String getMinimizedValueAsHTML() {
                return "<span class=\"colored\">Добавить комментарий</span>";
            }

            @Override
            public Component getPopupComponent() {
                VerticalLayout layout = new VerticalLayout();
                layout.setSpacing(true);

                RichTextArea textArea = new RichTextArea();
                layout.addComponent(textArea);

                Button addBtn = new Button("Добавить", FontAwesome.COMMENT_O);
                addBtn.addStyleName(ExtaTheme.BUTTON_PRIMARY);
                addBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
                addBtn.addClickListener(e -> {
                    TComment newComment = null;
                    try {
                        newComment = commentClass.newInstance();
                    } catch (Throwable ex) { }
                    newComment.setCreatedBy(lookup(UserManagementService.class).getCurrentUserLogin());
                    newComment.setCreatedAt(DateTime.now());
                    newComment.setText(textArea.getValue());
                    container.addBean(newComment);
                    setValue(container.getItemIds());
                    addCommentPopup.setPopupVisible(false);
                });
                layout.addComponent(addBtn);

                return layout;
            }
        });
        addCommentPopup.setHideOnMouseOut(false);
        root.addComponent(addCommentPopup);

        return root;
    }

    private Component getItemComponent(TComment comment) {
        VerticalLayout root = new VerticalLayout();

        Label icon = new Label(FontAwesome.COMMENT_O.getHtml(), ContentMode.HTML);
        icon.addStyleName(ExtaTheme.LABEL_COLORED);
        Label user = new Label(comment.getCreatedBy(), ContentMode.HTML);
        user.addStyleName(ExtaTheme.LABEL_COLORED);
        user.addStyleName(ExtaTheme.LABEL_BOLD);
        user.setConverter(lookup(LoginToUserNameConverter.class));
        Label crTime = new Label(new ObjectProperty(comment.getCreatedAt()));
        crTime.addStyleName(ExtaTheme.LABEL_COLORED);

        Button editBtn = new Button("Редактировать", FontAwesome.PENCIL);
        editBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        editBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
//        editBtn.addStyleName(ExtaTheme.BUTTON_SMALL);

        Button delBtn = new Button("Удалить", FontAwesome.TRASH_O);
        delBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        delBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
//        delBtn.addStyleName(ExtaTheme.BUTTON_SMALL);

        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        header.addComponents(icon, user, crTime, editBtn, delBtn);
        header.setSpacing(true);
        root.addComponent(header);

        Label text = new Label(comment.getText(), ContentMode.HTML);
        root.addComponent(text);

        return  root;
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }
}
