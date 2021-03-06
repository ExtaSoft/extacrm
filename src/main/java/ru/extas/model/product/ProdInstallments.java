package ru.extas.model.product;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Модель данных для продукта "Рассрочка"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 17:38
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@DiscriminatorValue("PAYMENT_BY_INSTALLMENTS")
@Table(name = "PROD_INSTALLMENTS")
public class ProdInstallments extends Product {

	// Максимальный срок кредита (в месяцах)
	@Column(name = "MAX_PERIOD")
	private int maxPeriod;

	// Минимальный первоначальный взнос
	@Column(name = "MIN_DOWNPAYMENT", precision = 32, scale = 4)
	private BigDecimal minDownpayment;

	// Максимальная сумма рассрочки
	@Column(name = "MAX_SUM", precision = 32, scale = 4)
	private BigDecimal maxSum;

	/**
	 * <p>Getter for the field <code>maxPeriod</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMaxPeriod() {
		return maxPeriod;
	}

	/**
	 * <p>Setter for the field <code>maxPeriod</code>.</p>
	 *
	 * @param maxPeriod a int.
	 */
	public void setMaxPeriod(final int maxPeriod) {
		this.maxPeriod = maxPeriod;
	}

	/**
	 * <p>Getter for the field <code>minDownpayment</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getMinDownpayment() {
		return minDownpayment;
	}

	/**
	 * <p>Setter for the field <code>minDownpayment</code>.</p>
	 *
	 * @param minDownpayment a {@link java.math.BigDecimal} object.
	 */
	public void setMinDownpayment(final BigDecimal minDownpayment) {
		this.minDownpayment = minDownpayment;
	}

	/**
	 * <p>Getter for the field <code>maxSum</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getMaxSum() {
		return maxSum;
	}

	/**
	 * <p>Setter for the field <code>maxSum</code>.</p>
	 *
	 * @param maxSum a {@link java.math.BigDecimal} object.
	 */
	public void setMaxSum(final BigDecimal maxSum) {
		this.maxSum = maxSum;
	}
}
