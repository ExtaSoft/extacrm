package ru.extas.model.sale;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Модель данных для продукта "Кредит"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 17:37
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@DiscriminatorValue("CREDIT")
@Table(name = "PROD_CREDIT")
public class ProdCredit extends Product {

	// Тип программы
	public enum ProgramType {
		// незалоговая
		NO_COLLATERAL,
		// залоговая без передачи ПТС (ПСМ) в Банк,
		COLLATERAL_WITH_PTS,
		// залоговая с передачей ПТС (ПСМ) в Банк
		COLLATERAL_WITHOUT_PTS
	}

	;

	@Column(name = "PROGRAM_TYPE")
	@Enumerated(EnumType.STRING)
	private ProgramType programType;

	// Параметры кредитной программы
	// min. сумма кредита
	@Column(name = "MIN_SUM", precision = 32, scale = 4)
	private BigDecimal minSum;
	// Максимальная сумма кредита
	@Column(name = "MAX_SUM", precision = 32, scale = 4)
	private BigDecimal maxSum;
	// Минимальный первоначальный взнос
	@Column(name = "MIN_DOWNPAYMENT", precision = 32, scale = 4)
	private BigDecimal minDownpayment;
	// max. первый взнос
	@Column(name = "MAX_DOWNPAYMENT", precision = 32, scale = 4)
	private BigDecimal maxDownpayment;
	// min. срок кредита
	@Column(name = "MIN_PERIOD")
	private int minPeriod;
	// Максимальный срок кредита (в месяцах)
	@Column(name = "MAX_PERIOD")
	private int maxPeriod;

	// Процент по кредиту: процент ; срок ; первый взнос
	@OneToMany(mappedBy = "product", targetEntity = ProdCreditPercent.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
	@OrderBy("percent")
	private List<ProdCreditPercent> percents;

	// шаг кредита, мес.
	@Column(name = "STEP")
	private int step;
	// Субсидия Дилера, % от суммы кредита
	@Column(name = "DEALER_SUBSIDY", precision = 32, scale = 4)
	private BigDecimal dealerSubsidy;

	// Комплект документов
	@OneToMany(mappedBy = "product", targetEntity = ProdCreditDoc.class, orphanRemoval = true, cascade = {CascadeType.ALL})
	@OrderBy("required DESC")
	private List<ProdCreditDoc> docList;

	/**
	 * <p>Getter for the field <code>programType</code>.</p>
	 *
	 * @return a {@link ru.extas.model.sale.ProdCredit.ProgramType} object.
	 */
	public ProgramType getProgramType() {
		return programType;
	}

	/**
	 * <p>Setter for the field <code>programType</code>.</p>
	 *
	 * @param programType a {@link ru.extas.model.sale.ProdCredit.ProgramType} object.
	 */
	public void setProgramType(final ProgramType programType) {
		this.programType = programType;
	}

	/**
	 * <p>Getter for the field <code>minSum</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getMinSum() {
		return minSum;
	}

	/**
	 * <p>Setter for the field <code>minSum</code>.</p>
	 *
	 * @param minSum a {@link java.math.BigDecimal} object.
	 */
	public void setMinSum(final BigDecimal minSum) {
		this.minSum = minSum;
	}

	/**
	 * <p>Getter for the field <code>maxDownpayment</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getMaxDownpayment() {
		return maxDownpayment;
	}

	/**
	 * <p>Setter for the field <code>maxDownpayment</code>.</p>
	 *
	 * @param maxDownpayment a {@link java.math.BigDecimal} object.
	 */
	public void setMaxDownpayment(final BigDecimal maxDownpayment) {
		this.maxDownpayment = maxDownpayment;
	}

	/**
	 * <p>Getter for the field <code>minPeriod</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMinPeriod() {
		return minPeriod;
	}

	/**
	 * <p>Setter for the field <code>minPeriod</code>.</p>
	 *
	 * @param minPeroid a int.
	 */
	public void setMinPeriod(final int minPeroid) {
		this.minPeriod = minPeroid;
	}

	/**
	 * <p>Getter for the field <code>percents</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ProdCreditPercent> getPercents() {
		return percents;
	}

	/**
	 * <p>Setter for the field <code>percents</code>.</p>
	 *
	 * @param percents a {@link java.util.List} object.
	 */
	public void setPercents(final List<ProdCreditPercent> percents) {
		this.percents = percents;
	}

	/**
	 * <p>Getter for the field <code>step</code>.</p>
	 *
	 * @return a int.
	 */
	public int getStep() {
		return step;
	}

	/**
	 * <p>Setter for the field <code>step</code>.</p>
	 *
	 * @param step a int.
	 */
	public void setStep(final int step) {
		this.step = step;
	}

	/**
	 * <p>Getter for the field <code>dealerSubsidy</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getDealerSubsidy() {
		return dealerSubsidy;
	}

	/**
	 * <p>Setter for the field <code>dealerSubsidy</code>.</p>
	 *
	 * @param dealerSubsidy a {@link java.math.BigDecimal} object.
	 */
	public void setDealerSubsidy(final BigDecimal dealerSubsidy) {
		this.dealerSubsidy = dealerSubsidy;
	}

	/**
	 * <p>Getter for the field <code>docList</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ProdCreditDoc> getDocList() {
		return docList;
	}

	/**
	 * <p>Setter for the field <code>docList</code>.</p>
	 *
	 * @param docList a {@link java.util.List} object.
	 */
	public void setDocList(final List<ProdCreditDoc> docList) {
		this.docList = docList;
	}

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
	 * @param maxPeroid a int.
	 */
	public void setMaxPeriod(final int maxPeroid) {
		this.maxPeriod = maxPeroid;
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
