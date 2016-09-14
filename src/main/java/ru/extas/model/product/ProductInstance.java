package ru.extas.model.product;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Employee;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Связь продукта с продажей (продажа продукта)
 *
 * @author Valery Orlov
 *         Date: 21.01.14
 *         Time: 12:16
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "PRODUCT_IN_SALE")
public class ProductInstance extends AuditedObject {

	/**
	 * Статусы заявок на кредиты
	 */
    public enum State {
		/**
		 * На рассмотрении
		 */
        IN_PROGRESS,
		/**
		 * Одобрена
		 */
        AGREED,
		/**
		 * Отклонен
		 */
        REJECTED,
		/**
		 * Сделка оформлена
		 */
		SOLD_OUT,
		/**
		 * Новая заявка
		 */
		NEW
    }

	// Продажа
	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	private Sale sale;

	// Лид
	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	private Lead lead;

	// Продукт
	@OneToOne(optional = false, fetch = FetchType.LAZY)
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

	// Ответственный сотрудник (Ответственный за продукт)
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	private Employee responsible;

    @Enumerated
    private State state = State.NEW;

	// Дополнительные расходы по породукту
	@OneToMany(mappedBy = "productInstance", targetEntity = ProductExpenditure.class, orphanRemoval = true, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	private List<ProductExpenditure> expenditureList;


	/**
	 */
	public ProductInstance() {
	}

	/**
	 *
	 */
	public ProductInstance(final Lead lead, final Sale sale) {
		this.sale = sale;
        this.lead = lead;
	}

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public Employee getResponsible() {
		return responsible;
	}

	public void setResponsible(final Employee responsible) {
		this.responsible = responsible;
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
	 * <p>Getter for the field <code>sale</code>.</p>
	 *
	 * @return a {@link ru.extas.model.sale.Sale} object.
	 */
	public Sale getSale() {
		return sale;
	}

	/**
	 * <p>Setter for the field <code>sale</code>.</p>
	 *
	 * @param sale a {@link ru.extas.model.sale.Sale} object.
	 */
	public void setSale(final Sale sale) {
		this.sale = sale;
	}

	/**
	 * <p>Getter for the field <code>product</code>.</p>
	 *
	 * @return a {@link Product} object.
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * <p>Setter for the field <code>product</code>.</p>
	 *
	 * @param product a {@link Product} object.
	 */
	public void setProduct(final Product product) {
		this.product = product;
	}

	/**
	 * <p>Getter for the field <code>summ</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getSumm() {
		return summ;
	}

	/**
	 * <p>Setter for the field <code>summ</code>.</p>
	 *
	 * @param summ a {@link java.math.BigDecimal} object.
	 */
	public void setSumm(final BigDecimal summ) {
		this.summ = summ;
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

    public List<ProductExpenditure> getExpenditureList() {
        return expenditureList;
    }

    public void setExpenditureList(List<ProductExpenditure> expenditureList) {
        this.expenditureList = expenditureList;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }
}
