package ru.extas.model;

import javax.persistence.*;

/**
 * Модель данных для продукта
 * (базовый класс продукта)
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 17:03
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "PRODUCT")
public abstract class Product extends AbstractExtaObject {

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

	// Комментарий к продукту
	@Column(name = "COMMENT")
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Company getVendor() {
		return vendor;
	}

	public void setVendor(final Company vendor) {
		this.vendor = vendor;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}
}
