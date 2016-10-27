package ru.extas.model.contacts;

import ru.extas.model.common.Comment;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Комментарии в Компании
 *
 * @author Valery Orlov
 *         Date: 25.09.2014
 *         Time: 18:14
 */
@Entity
@Table(name = "COMPANY_PRIVATE_COMMENT")
public class CompanyPrivateComment extends Comment {

    public CompanyPrivateComment() {
    }

    public CompanyPrivateComment(final Comment comment) {
        this.setArchived(comment.isArchived());
        this.setCreatedBy(comment.getCreatedBy());
        this.setCreatedDate(comment.getCreatedDate());
        this.setLastModifiedBy(comment.getLastModifiedBy());
        this.setLastModifiedDate(comment.getLastModifiedDate());
        this.setOwnerId(comment.getOwnerId());
        this.setText(comment.getText());
    }

}
