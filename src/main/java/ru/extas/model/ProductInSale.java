package ru.extas.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Связь продукта с продажей (продажа продукта)
 *
 * @author Valery Orlov
 *         Date: 21.01.14
 *         Time: 12:16
 */
@Entity
@Table(name = "PRODUCT_IN_SALE")
public class ProductInSale extends AbstractExtaObject {

	// Продажа
	@ManyToOne(optional = false)
	private Sale sale;

	// Продукт
	@OneToOne
	private Product product;

	// Сумма
	@Column(name = "SUMM", precision = 32, scale = 4)
	private BigDecimal summ;

	// Срок
	@Column(name = "PERIOD")
	private int period;

	// первый взнос
	@Column(name = "DOWNPAYMENT", precision = 32, scale = 4)
	private BigDecimal downpayment;

	public ProductInSale() {
	}

	public ProductInSale(final Sale sale) {
		this.sale = sale;
	}

	public BigDecimal getDownpayment() {
		return downpayment;
	}

	public void setDownpayment(final BigDecimal downpayment) {
		this.downpayment = downpayment;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(final Sale sale) {
		this.sale = sale;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(final Product product) {
		this.product = product;
	}

	public BigDecimal getSumm() {
		return summ;
	}

	public void setSumm(final BigDecimal summ) {
		this.summ = summ;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(final int period) {
		this.period = period;
	}

}
