package ru.extas.model;

import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.math.BigDecimal;

/**
 * Полис страхования
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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

    // признак пролонгации договора
    @Column(name = "IS_USED_MOTOR")
    private boolean usedMotor;

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

    @Column(name = "BENEFICIARY", length = Contact.NAME_LENGTH)
    @Max(Contact.NAME_LENGTH)
    private String beneficiary;

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

    // Серийный номер
    @Column(name = "MOTOR_VIN", length = 50)
    @Max(50)
    private String motorVin;

    // Номер договора купли-продажи
	@Column(name = "SALE_NUM", length = 50)
	@Max(50)
	private String saleNum;

	// Дата договора купли-продажи
	@Column(name = "SALE_DATE")
	private LocalDate saleDate;

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

	@OneToOne
	private SalePoint dealer;

	public enum PeriodOfCover {
		YEAR,
		HALF_A_YEAR;
	}

	/**
	 * <p>Constructor for Insurance.</p>
	 */
	public Insurance() {
	}

	/**
	 * <p>Getter for the field <code>coverTime</code>.</p>
	 *
	 * @return a {@link ru.extas.model.Insurance.PeriodOfCover} object.
	 */
	public PeriodOfCover getCoverTime() {
		return coverTime;
	}

	/**
	 * <p>Setter for the field <code>coverTime</code>.</p>
	 *
	 * @param coverTime a {@link ru.extas.model.Insurance.PeriodOfCover} object.
	 */
	public void setCoverTime(PeriodOfCover coverTime) {
		this.coverTime = coverTime;
	}

	/**
	 * <p>Constructor for Insurance.</p>
	 *
	 * @param motorBrand a {@link java.lang.String} object.
	 * @param riskSum a {@link java.math.BigDecimal} object.
	 * @param coverPeriod a {@link ru.extas.model.Insurance.PeriodOfCover} object.
	 * @param usedMotor a boolean.
	 */
	public Insurance(String motorBrand, BigDecimal riskSum, PeriodOfCover coverPeriod, boolean usedMotor) {
		this.motorBrand = motorBrand;
		this.riskSum = riskSum;
		this.coverTime = coverPeriod;
        this.usedMotor = usedMotor;
	}

	/**
	 * <p>Getter for the field <code>regNum</code>.</p>
	 *
	 * @return the regNum
	 */
	public final String getRegNum() {
		return regNum;
	}

	/**
	 * <p>Setter for the field <code>regNum</code>.</p>
	 *
	 * @param regNum the regNum to set
	 */
	public final void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	/**
	 * <p>Getter for the field <code>motorType</code>.</p>
	 *
	 * @return the motorType
	 */
	public final String getMotorType() {
		return motorType;
	}

	/**
	 * <p>Setter for the field <code>motorType</code>.</p>
	 *
	 * @param motorType the motorType to set
	 */
	public final void setMotorType(String motorType) {
		this.motorType = motorType;
	}

	/**
	 * <p>Getter for the field <code>motorBrand</code>.</p>
	 *
	 * @return the motorBrand
	 */
	public final String getMotorBrand() {
		return motorBrand;
	}

	/**
	 * <p>Setter for the field <code>motorBrand</code>.</p>
	 *
	 * @param motorBrand the motorBrand to set
	 */
	public final void setMotorBrand(String motorBrand) {
		this.motorBrand = motorBrand;
	}

	/**
	 * <p>Getter for the field <code>motorModel</code>.</p>
	 *
	 * @return the motorModel
	 */
	public final String getMotorModel() {
		return motorModel;
	}

	/**
	 * <p>Setter for the field <code>motorModel</code>.</p>
	 *
	 * @param motorModel the motorModel to set
	 */
	public final void setMotorModel(String motorModel) {
		this.motorModel = motorModel;
	}

	/**
	 * <p>Getter for the field <code>riskSum</code>.</p>
	 *
	 * @return the riskSum
	 */
	public final BigDecimal getRiskSum() {
		return riskSum;
	}

	/**
	 * <p>Setter for the field <code>riskSum</code>.</p>
	 *
	 * @param riskSum the riskSum to set
	 */
	public final void setRiskSum(BigDecimal riskSum) {
		this.riskSum = riskSum;
	}

	/**
	 * <p>Getter for the field <code>premium</code>.</p>
	 *
	 * @return the premium
	 */
	public final BigDecimal getPremium() {
		return premium;
	}

	/**
	 * <p>Setter for the field <code>premium</code>.</p>
	 *
	 * @param premium the premium to set
	 */
	public final void setPremium(BigDecimal premium) {
		this.premium = premium;
	}

	/**
	 * <p>Getter for the field <code>date</code>.</p>
	 *
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * <p>Setter for the field <code>date</code>.</p>
	 *
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * <p>Getter for the field <code>client</code>.</p>
	 *
	 * @return the client
	 */
	public Person getClient() {
		return client;
	}

	/**
	 * <p>Setter for the field <code>client</code>.</p>
	 *
	 * @param client the client to set
	 */
	public void setClient(Person client) {
		this.client = client;
	}

	/**
	 * <p>Getter for the field <code>paymentDate</code>.</p>
	 *
	 * @return the paymentDate
	 */
	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	/**
	 * <p>Setter for the field <code>paymentDate</code>.</p>
	 *
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * <p>Getter for the field <code>startDate</code>.</p>
	 *
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * <p>Setter for the field <code>startDate</code>.</p>
	 *
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * <p>Getter for the field <code>endDate</code>.</p>
	 *
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * <p>Setter for the field <code>endDate</code>.</p>
	 *
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * <p>Getter for the field <code>a7Num</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getA7Num() {
		return a7Num;
	}

	/**
	 * <p>Setter for the field <code>a7Num</code>.</p>
	 *
	 * @param a7Num a {@link java.lang.String} object.
	 */
	public void setA7Num(final String a7Num) {
		this.a7Num = a7Num;
	}

	/**
	 * <p>Getter for the field <code>dealer</code>.</p>
	 *
	 * @return a {@link ru.extas.model.SalePoint} object.
	 */
	public SalePoint getDealer() {
		return dealer;
	}

	/**
	 * <p>Setter for the field <code>dealer</code>.</p>
	 *
	 * @param dealer a {@link ru.extas.model.SalePoint} object.
	 */
	public void setDealer(SalePoint dealer) {
		this.dealer = dealer;
	}

	/**
	 * <p>Getter for the field <code>saleNum</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSaleNum() {
		return saleNum;
	}

	/**
	 * <p>Setter for the field <code>saleNum</code>.</p>
	 *
	 * @param saleNum a {@link java.lang.String} object.
	 */
	public void setSaleNum(final String saleNum) {
		this.saleNum = saleNum;
	}

	/**
	 * <p>Getter for the field <code>saleDate</code>.</p>
	 *
	 * @return a {@link org.joda.time.LocalDate} object.
	 */
	public LocalDate getSaleDate() {
		return saleDate;
	}

	/**
	 * <p>Setter for the field <code>saleDate</code>.</p>
	 *
	 * @param saleDate a {@link org.joda.time.LocalDate} object.
	 */
	public void setSaleDate(final LocalDate saleDate) {
		this.saleDate = saleDate;
	}

    /**
     * <p>isUsedMotor.</p>
     *
     * @return a boolean.
     */
    public boolean isUsedMotor() {
        return usedMotor;
    }

    /**
     * <p>Setter for the field <code>usedMotor</code>.</p>
     *
     * @param used a boolean.
     */
    public void setUsedMotor(boolean used) {
        this.usedMotor = used;
    }

    /**
     * <p>Getter for the field <code>motorVin</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMotorVin() {
        return motorVin;
    }

    /**
     * <p>Setter for the field <code>motorVin</code>.</p>
     *
     * @param motorVin a {@link java.lang.String} object.
     */
    public void setMotorVin(String motorVin) {
        this.motorVin = motorVin;
    }

    /**
     * <p>Getter for the field <code>beneficiary</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBeneficiary() {
        return beneficiary;
    }

    /**
     * <p>Setter for the field <code>beneficiary</code>.</p>
     *
     * @param beneficiary a {@link java.lang.String} object.
     */
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
}
