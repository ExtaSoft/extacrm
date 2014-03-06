package ru.extas.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Модель данных для продукта "Страховка"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 17:38
 */
@Entity
@DiscriminatorValue("INSURANCE_PROP")
@Table(name = "PROD_INSURANCE")
public class ProdInsurance extends Product {

	// Процент страховой премии
	@Column(name = "PERCENT", precision = 32, scale = 4)
	private BigDecimal percent;

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(final BigDecimal percent) {
		this.percent = percent;
	}

}
