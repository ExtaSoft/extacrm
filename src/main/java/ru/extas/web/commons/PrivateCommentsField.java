package ru.extas.web.commons;

import ru.extas.model.common.Comment;
import ru.extas.server.security.UserManagementService;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Created by valery on 26.10.16.
 */
public class PrivateCommentsField<TComment extends Comment> extends CommentsField<TComment> {
    public PrivateCommentsField(final Class<TComment> tCommentClass) {
        super(tCommentClass);
        setVisible(lookup(UserManagementService.class).isPermitPrivateComments());
    }
}
