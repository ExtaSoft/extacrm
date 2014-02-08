package ru.extas.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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
	private List<ProdCreditPercent> percents;

	// шаг кредита, мес.
	@Column(name = "STEP")
	private int step;
	// Субсидия Дилера, % от суммы кредита
	@Column(name = "DEALER_SUBSIDY", precision = 32, scale = 4)
	private BigDecimal dealerSubsidy;

	// Комплект документов
	@OneToMany(mappedBy = "product", targetEntity = ProdCreditDoc.class, orphanRemoval = true, cascade = {CascadeType.ALL})
	private List<ProdCreditDoc> docList;

	public ProgramType getProgramType() {
		return programType;
	}

	public void setProgramType(final ProgramType programType) {
		this.programType = programType;
	}

	public BigDecimal getMinSum() {
		return minSum;
	}

	public void setMinSum(final BigDecimal minSum) {
		this.minSum = minSum;
	}

	public BigDecimal getMaxDownpayment() {
		return maxDownpayment;
	}

	public void setMaxDownpayment(final BigDecimal maxDownpayment) {
		this.maxDownpayment = maxDownpayment;
	}

	public int getMinPeriod() {
		return minPeriod;
	}

	public void setMinPeriod(final int minPeroid) {
		this.minPeriod = minPeroid;
	}

	public List<ProdCreditPercent> getPercents() {
		return percents;
	}

	public void setPercents(final List<ProdCreditPercent> percents) {
		this.percents = percents;
	}

	public int getStep() {
		return step;
	}

	public void setStep(final int step) {
		this.step = step;
	}

	public BigDecimal getDealerSubsidy() {
		return dealerSubsidy;
	}

	public void setDealerSubsidy(final BigDecimal dealerSubsidy) {
		this.dealerSubsidy = dealerSubsidy;
	}

	public List<ProdCreditDoc> getDocList() {
		return docList;
	}

	public void setDocList(final List<ProdCreditDoc> docList) {
		this.docList = docList;
	}

	public int getMaxPeriod() {
		return maxPeriod;
	}

	public void setMaxPeriod(final int maxPeroid) {
		this.maxPeriod = maxPeroid;
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
