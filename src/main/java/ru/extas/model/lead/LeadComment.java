package ru.extas.model.lead;

import ru.extas.model.common.Comment;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Комментарий к лиду.
 *
 * @author sandarkin
 * @since 2.0.0
 */
@Entity
@Table(name = "LEAD_COMMENT")
public class LeadComment extends Comment {
}
