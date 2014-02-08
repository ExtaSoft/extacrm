package ru.extas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Необходимый документ в кредитном продукте.
 * Определяет список необходимых документов.
 *
 * @author Valery Orlov
 *         Date: 07.02.14
 *         Time: 14:43
 */
@Entity
@Table(name = "PROD_CREDIT_DOC")
public class ProdCreditDoc extends AbstractExtaObject {

	// Вид документа:
	// Паспорт
	// ПТС
	// СТС
	// загранпаспорт
	// полис ДМС
	// справка 2НДФЛ
	// водительское удостоверение
	// СНИЛС
	@Column(name = "NAME")
	private String name;

	// Признак обязательного документа
	@Column(name = "IS_REQUIRED")
	private boolean required;

	@ManyToOne(optional = false)
	private ProdCredit product;

	public ProdCreditDoc() {
	}

	public ProdCreditDoc(final ProdCredit product) {
		this.product = product;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public ProdCredit getProduct() {
		return product;
	}

	public void setProduct(final ProdCredit product) {
		this.product = product;
	}
}
