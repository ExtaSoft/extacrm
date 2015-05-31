package ru.extas.model.contacts;

import ru.extas.model.common.Comment;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Комментарии к торговой точке
 *
 * @author Valery Orlov
 *         Date: 20.05.2015
 *         Time: 12:59
 */
@Entity
@Table(name = "SALE_POINT_COMMENT")
public class SalePointComment extends Comment {
}
