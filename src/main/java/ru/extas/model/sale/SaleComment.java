package ru.extas.model.sale;

import ru.extas.model.common.Comment;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Комментарии в продаже
 *
 * @author Valery Orlov
 *         Date: 25.09.2014
 *         Time: 18:14
 */
@Entity
@Table(name = "SALE_COMMENT")
public class SaleComment extends Comment {

}
