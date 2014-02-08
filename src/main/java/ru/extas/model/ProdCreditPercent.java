package ru.extas.model;

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
 */
@Entity
@Table(name = "PROD_CREDIT_PERCENT")
public class ProdCreditPercent extends AbstractExtaObject {

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

	public ProdCreditPercent() {
	}

	public ProdCreditPercent(final ProdCredit product) {
		this.product = product;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(final BigDecimal percent) {
		this.percent = percent;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(final int period) {
		this.period = period;
	}

	public BigDecimal getDownpayment() {
		return downpayment;
	}

	public void setDownpayment(final BigDecimal downpayment) {
		this.downpayment = downpayment;
	}

	public ProdCredit getProduct() {
		return product;
	}

	public void setProduct(final ProdCredit product) {
		this.product = product;
	}
}
