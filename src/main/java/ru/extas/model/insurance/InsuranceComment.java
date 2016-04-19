package ru.extas.model.insurance;

import ru.extas.model.common.Comment;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Коментарии в страховке
 *
 * @author Valery Orlov
 *         Date: 19.04.2016
 *         Time: 12:48
 */
@Entity
@Table(name = "INSURANCE_COMMENT")
public class InsuranceComment extends Comment {
}
