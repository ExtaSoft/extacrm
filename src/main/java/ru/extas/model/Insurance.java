package ru.extas.model;

import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * Полис страхования
 *
 * @author Valery Orlov
 */
@Entity
@Table(name = "INSURANCE",
		indexes = {
				@Index(columnList = "REG_NUM"),
				@Index(columnList = "A7_NUM"),
				@Index(columnList = "\"DATE\"")
		})
public class Insurance extends AbstractExtaObject {

	private static final long serialVersionUID = -1289533183659860816L;


	// Номер полиса
	@Column(name = "REG_NUM", length = Policy.REG_NUM_LENGTH, unique = true)
	@Max(Policy.REG_NUM_LENGTH)
	private String regNum;

	// Номер квитанции А-7
	@Column(name = "A7_NUM", length = A7Form.REG_NUM_LENGTH, unique = true)
	@Max(A7Form.REG_NUM_LENGTH)
	private String a7Num;

	// Дата заключения полиса
	@Column(name = "\"DATE\"")
	private LocalDate date;

	// Клиент
	@OneToOne
	private Person client;

	// Предмет страхования - тип
	@Column(name = "MOTOR_TYPE", length = 50)
	@Max(50)
	private String motorType;

	// Предмет страхования - марка
	@Column(name = "MOTOR_BRAND", length = 50)
	@Max(50)
	private String motorBrand;

	// Предмет страхования - модель
	@Column(name = "MOTOR_MODEL", length = 50)
	@Max(50)
	private String motorModel;

	// Страховая сумма, руб.
	@Column(name = "RISK_SUM", precision = 32, scale = 4)
	private BigDecimal riskSum;

	// Период покрытия
	@Column(name = "COVER_TIME")
	private PeriodOfCover coverTime;

	// Страховая премия, руб.
	@Column(precision = 32, scale = 4)
	private BigDecimal premium;

	// Дата оплаты страховой премии
	@Column(name = "PAYMENT_DATE")
	private LocalDate paymentDate;

	// Дата начала срока действия договора
	@Column(name = "START_DATE")
	private LocalDate startDate;

	// Дата окончания срока действия договора
	@Column(name = "END_DATE")
	private LocalDate endDate;

	// Точка продажи (Контрагент)
	@Column(name = "POINT_OF_SALE")
	private String pointOfSale;

	@OneToOne
	private SalePoint dealer;

	public enum PeriodOfCover {
		YEAR,
		HALF_A_YEAR;
	}

	public Insurance() {
	}

	public PeriodOfCover getCoverTime() {
		return coverTime;
	}

	public void setCoverTime(PeriodOfCover coverTime) {
		this.coverTime = coverTime;
	}

	/**
	 * @param motorBrand
	 * @param riskSum
	 */
	public Insurance(String motorBrand, BigDecimal riskSum, PeriodOfCover coverPeriod) {
		this.motorBrand = motorBrand;
		this.riskSum = riskSum;
		this.coverTime = coverPeriod;
	}

	/**
	 * @return the regNum
	 */
	public final String getRegNum() {
		return regNum;
	}

	/**
	 * @param regNum the regNum to set
	 */
	public final void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	/**
	 * @return the motorType
	 */
	public final String getMotorType() {
		return motorType;
	}

	/**
	 * @param motorType the motorType to set
	 */
	public final void setMotorType(String motorType) {
		this.motorType = motorType;
	}

	/**
	 * @return the motorBrand
	 */
	public final String getMotorBrand() {
		return motorBrand;
	}

	/**
	 * @param motorBrand the motorBrand to set
	 */
	public final void setMotorBrand(String motorBrand) {
		this.motorBrand = motorBrand;
	}

	/**
	 * @return the motorModel
	 */
	public final String getMotorModel() {
		return motorModel;
	}

	/**
	 * @param motorModel the motorModel to set
	 */
	public final void setMotorModel(String motorModel) {
		this.motorModel = motorModel;
	}

	/**
	 * @return the riskSum
	 */
	public final BigDecimal getRiskSum() {
		return riskSum;
	}

	/**
	 * @param riskSum the riskSum to set
	 */
	public final void setRiskSum(BigDecimal riskSum) {
		this.riskSum = riskSum;
	}

	/**
	 * @return the premium
	 */
	public final BigDecimal getPremium() {
		return premium;
	}

	/**
	 * @param premium the premium to set
	 */
	public final void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the client
	 */
	public Person getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Person client) {
		this.client = client;
	}

	/**
	 * @return the paymentDate
	 */
	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the pointOfSale
	 */
	public String getPointOfSale() {
		return pointOfSale;
	}

	/**
	 * @param pointOfSale the pointOfSale to set
	 */
	public void setPointOfSale(String pointOfSale) {
		this.pointOfSale = pointOfSale;
	}

	public String getA7Num() {
		return a7Num;
	}

	public void setA7Num(final String a7Num) {
		this.a7Num = a7Num;
	}

	public SalePoint getDealer() {
		return dealer;
	}

	public void setDealer(SalePoint dealer) {
		this.dealer = dealer;
	}
}
