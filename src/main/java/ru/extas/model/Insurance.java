package ru.extas.model;

import java.math.BigDecimal;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.joda.time.LocalDate;

import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * Полис страхования
 * 
 * @author Valery Orlov
 * 
 */
@PersistenceCapable(detachable = "true")
public class Insurance extends AbstractExtaObject {

	private static final long serialVersionUID = -1289533183659860816L;

	// Номер полиса
	@Persistent
	private String regNum;

	// Дата заключения полиса
	@Persistent
	private LocalDate date;

	// Клиент
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Contact client;

	// Предмет страхования - тип
	@Persistent
	private String motorType;

	// Предмет страхования - марка
	@Persistent
	private String motorBrand;

	// Предмет страхования - модель
	@Persistent
	private String motorModel;

	// Страховая сумма, руб.
	@Persistent
	private BigDecimal riskSum;

	// Страховая премия, руб.
	@Persistent
	private BigDecimal premium;

	// Дата оплаты страховой премии
	@Persistent
	private LocalDate paymentDate;

	// Дата начала срока действия договора
	@Persistent
	private LocalDate startDate;

	// Дата окончания срока действия договора
	@Persistent
	private LocalDate endDate;

	// Точка продажи (Контрагент)
	@Persistent
	private String pointOfSale;

	public Insurance() {
	}

	/**
	 * @return the regNum
	 */
	public final String getRegNum() {
		return regNum;
	}

	/**
	 * @param regNum
	 *            the regNum to set
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
	 * @param motorType
	 *            the motorType to set
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
	 * @param motorBrand
	 *            the motorBrand to set
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
	 * @param motorModel
	 *            the motorModel to set
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
	 * @param riskSum
	 *            the riskSum to set
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
	 * @param premium
	 *            the premium to set
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
	 * @param date
	 *            the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the client
	 */
	public Contact getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Contact client) {
		this.client = client;
	}

	/**
	 * @return the paymentDate
	 */
	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate
	 *            the paymentDate to set
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
	 * @param startDate
	 *            the startDate to set
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
	 * @param endDate
	 *            the endDate to set
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
	 * @param pointOfSale
	 *            the pointOfSale to set
	 */
	public void setPointOfSale(String pointOfSale) {
		this.pointOfSale = pointOfSale;
	}

}
