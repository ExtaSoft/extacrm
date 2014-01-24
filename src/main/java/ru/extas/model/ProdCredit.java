package ru.extas.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Модель данных для продукта "Кредит"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 17:37
 */
@Entity
@DiscriminatorValue("CREDIT")
@Table(name = "PROD_CREDIT")
public class ProdCredit extends Product {

	// Процент кредитной программы
	@Column(name = "PERCENT", precision = 32, scale = 4)
	private BigDecimal percent;

	// Максимальный срок кредита (в месяцах)
	@Column(name = "MAX_PERIOD")
	private int maxPeroid;

	// Минимальный первоначальный взнос
	@Column(name = "MIN_DOWNPAYMENT", precision = 32, scale = 4)
	private BigDecimal minDownpayment;

	// Максимальная сумма кредита
	@Column(name = "MAX_SUM", precision = 32, scale = 4)
	private BigDecimal maxSum;

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(final BigDecimal percent) {
		this.percent = percent;
	}

	public int getMaxPeroid() {
		return maxPeroid;
	}

	public void setMaxPeroid(final int maxPeroid) {
		this.maxPeroid = maxPeroid;
	}

	public BigDecimal getMinDownpayment() {
		return minDownpayment;
	}

	public void setMinDownpayment(final BigDecimal minDownpayment) {
		this.minDownpayment = minDownpayment;
	}

	public BigDecimal getMaxSum() {
		return maxSum;
	}

	public void setMaxSum(final BigDecimal maxSum) {
		this.maxSum = maxSum;
	}

}
