package ru.extas.model.sale;

import ru.extas.model.common.ChangeMarkedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Процентные ставки по кредиту в зависимости от срока кредита и первоначального взноса.
 *
 * @author Valery Orlov
 *         Date: 06.02.14
 *         Time: 12:55
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "PROD_CREDIT_PERCENT")
public class ProdCreditPercent extends ChangeMarkedObject {

	// процент ;
	@Column(name = "PERCENT", precision = 32, scale = 4)
	private BigDecimal percent;
	// срок ;
	@Column(name = "PERIOD")
	private int period;
	// первый взнос
	@Column(name = "DOWNPAYMENT", precision = 32, scale = 4)
	private BigDecimal downpayment;

	@ManyToOne(optional = false)
	private ProdCredit product;

	/**
	 * <p>Constructor for ProdCreditPercent.</p>
	 */
	public ProdCreditPercent() {
	}

	/**
	 * <p>Constructor for ProdCreditPercent.</p>
	 *
	 * @param product a {@link ProdCredit} object.
	 */
	public ProdCreditPercent(final ProdCredit product) {
		this.product = product;
	}

	/**
	 * <p>Getter for the field <code>percent</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getPercent() {
		return percent;
	}

	/**
	 * <p>Setter for the field <code>percent</code>.</p>
	 *
	 * @param percent a {@link java.math.BigDecimal} object.
	 */
	public void setPercent(final BigDecimal percent) {
		this.percent = percent;
	}

	/**
	 * <p>Getter for the field <code>period</code>.</p>
	 *
	 * @return a int.
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * <p>Setter for the field <code>period</code>.</p>
	 *
	 * @param period a int.
	 */
	public void setPeriod(final int period) {
		this.period = period;
	}

	/**
	 * <p>Getter for the field <code>downpayment</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getDownpayment() {
		return downpayment;
	}

	/**
	 * <p>Setter for the field <code>downpayment</code>.</p>
	 *
	 * @param downpayment a {@link java.math.BigDecimal} object.
	 */
	public void setDownpayment(final BigDecimal downpayment) {
		this.downpayment = downpayment;
	}

	/**
	 * <p>Getter for the field <code>product</code>.</p>
	 *
	 * @return a {@link ProdCredit} object.
	 */
	public ProdCredit getProduct() {
		return product;
	}

	/**
	 * <p>Setter for the field <code>product</code>.</p>
	 *
	 * @param product a {@link ProdCredit} object.
	 */
	public void setProduct(final ProdCredit product) {
		this.product = product;
	}
}
