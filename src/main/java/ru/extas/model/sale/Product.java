package ru.extas.model.sale;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Company;

import javax.persistence.*;

/**
 * Модель данных для продукта
 * (базовый класс продукта)
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 17:03
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "PRODUCT")
public abstract class Product extends AuditedObject {

	/**
	 * <p>Constructor for Product.</p>
	 */
	protected Product() {
	}

	/**
	 * Типы продуктов
	 */
	public enum Type {
		CREDIT,
		INSURANCE,
		PAYMENT_BY_INSTALLMENTS
	}


	// Наименование продукта
	@Column(name = "NAME")
	private String name;

	// Поставщик продукта
	@OneToOne
	private Company vendor;

	// Признак активности продукта
	@Column(name = "IS_ACTIVE")
	private boolean active;

	// Примечание к продукту
	@Column(name = "COMMENT")
	private String comment;

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * <p>Getter for the field <code>vendor</code>.</p>
	 *
	 * @return a {@link ru.extas.model.contacts.Company} object.
	 */
	public Company getVendor() {
		return vendor;
	}

	/**
	 * <p>Setter for the field <code>vendor</code>.</p>
	 *
	 * @param vendor a {@link ru.extas.model.contacts.Company} object.
	 */
	public void setVendor(final Company vendor) {
		this.vendor = vendor;
	}

	/**
	 * <p>Getter for the field <code>comment</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * <p>Setter for the field <code>comment</code>.</p>
	 *
	 * @param comment a {@link java.lang.String} object.
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}

	/**
	 * <p>isActive.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * <p>Setter for the field <code>active</code>.</p>
	 *
	 * @param active a boolean.
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}
}
