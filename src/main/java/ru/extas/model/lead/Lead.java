package ru.extas.model.lead;

import ru.extas.model.common.SecuredObject;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Модель данных для Лида
 *
 * @author Valery Orlov
 *         Date: 03.10.13
 *         Time: 10:09
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "LEAD")
public class Lead extends SecuredObject {

	/**
	 * Статусы лида
	 */
	public enum Status {
		NEW,
		QUALIFIED,
		CLOSED
	}

	/**
	 * Результат завершения лида
	 */
	public enum Result {
		/**
		 * Успешное выполнение (кредит получен).
		 */
		SUCCESSFUL,
		/**
		 * Отказ контрагента (отказ банка).
		 */
		VENDOR_REJECTED,
		/**
		 * Отказ клиента.
		 */
		CLIENT_REJECTED
	}

	// Регион покупки техники
	@Column(name = "REGION")
	private String region;

	// Тип техники
	@Column(name = "MOTOR_TYPE")
	private String motorType;

	// Марка техники
	@Column(name = "MOTOR_BRAND")
	private String motorBrand;

	// Модель техники
	@Column(name = "MOTOR_MODEL")
	private String motorModel;

	// Стоимость техники
	@Column(name = "MOTOR_PRICE", precision = 32, scale = 4)
	private BigDecimal motorPrice;

	// Мотосалон
	@Column(name = "POINT_OF_SALE")
	private String pointOfSale;

	@Column(name = "COMMENT")
	private String comment;

	//    Персональные данные
	//-----------------------------------

	// Имя контакта
	@Column(name = "CONTACT_NAME")
	private String contactName;
	// Телефон
	@Column(name = "CONTACT_PHONE")
	private String contactPhone;
	// Эл. почта
	@Column(name = "CONTACT_EMAIL")
	private String contactEmail;


	// Квалифицированные данные
	// -----------------------------------

	// Клиент
	@OneToOne
	private Person client;

	// Продавец (дилер, страх. компания)
	@OneToOne
	private SalePoint vendor;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "PROCESS_ID")
	private String processId;

	@Enumerated(EnumType.STRING)
	private Result result;

	/**
	 * <p>Getter for the field <code>result</code>.</p>
	 *
	 * @return a {@link ru.extas.model.lead.Lead.Result} object.
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * <p>Setter for the field <code>result</code>.</p>
	 *
	 * @param result a {@link ru.extas.model.lead.Lead.Result} object.
	 */
	public void setResult(Result result) {
		this.result = result;
	}

	/**
	 * <p>Getter for the field <code>processId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * <p>Setter for the field <code>processId</code>.</p>
	 *
	 * @param processId a {@link java.lang.String} object.
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * <p>Getter for the field <code>comment</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * <p>Setter for the field <code>comment</code>.</p>
	 *
	 * @param comment a {@link java.lang.String} object.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * <p>Getter for the field <code>region</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * <p>Setter for the field <code>region</code>.</p>
	 *
	 * @param region a {@link java.lang.String} object.
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * <p>Getter for the field <code>motorType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMotorType() {
		return motorType;
	}

	/**
	 * <p>Setter for the field <code>motorType</code>.</p>
	 *
	 * @param motorType a {@link java.lang.String} object.
	 */
	public void setMotorType(String motorType) {
		this.motorType = motorType;
	}

	/**
	 * <p>Getter for the field <code>motorBrand</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMotorBrand() {
		return motorBrand;
	}

	/**
	 * <p>Setter for the field <code>motorBrand</code>.</p>
	 *
	 * @param motorBrand a {@link java.lang.String} object.
	 */
	public void setMotorBrand(String motorBrand) {
		this.motorBrand = motorBrand;
	}

	/**
	 * <p>Getter for the field <code>motorModel</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMotorModel() {
		return motorModel;
	}

	/**
	 * <p>Setter for the field <code>motorModel</code>.</p>
	 *
	 * @param motorModel a {@link java.lang.String} object.
	 */
	public void setMotorModel(String motorModel) {
		this.motorModel = motorModel;
	}

	/**
	 * <p>Getter for the field <code>motorPrice</code>.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getMotorPrice() {
		return motorPrice;
	}

	/**
	 * <p>Setter for the field <code>motorPrice</code>.</p>
	 *
	 * @param motorPrice a {@link java.math.BigDecimal} object.
	 */
	public void setMotorPrice(BigDecimal motorPrice) {
		this.motorPrice = motorPrice;
	}

	/**
	 * <p>Getter for the field <code>pointOfSale</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPointOfSale() {
		return pointOfSale;
	}

	/**
	 * <p>Setter for the field <code>pointOfSale</code>.</p>
	 *
	 * @param pointOfSale a {@link java.lang.String} object.
	 */
	public void setPointOfSale(String pointOfSale) {
		this.pointOfSale = pointOfSale;
	}

	/**
	 * <p>Getter for the field <code>contactName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * <p>Setter for the field <code>contactName</code>.</p>
	 *
	 * @param contactName a {@link java.lang.String} object.
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * <p>Getter for the field <code>contactPhone</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContactPhone() {
		return contactPhone;
	}

	/**
	 * <p>Setter for the field <code>contactPhone</code>.</p>
	 *
	 * @param contactPhone a {@link java.lang.String} object.
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	/**
	 * <p>Getter for the field <code>contactEmail</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * <p>Setter for the field <code>contactEmail</code>.</p>
	 *
	 * @param contactEmail a {@link java.lang.String} object.
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * <p>Getter for the field <code>client</code>.</p>
	 *
	 * @return a {@link ru.extas.model.contacts.Person} object.
	 */
	public Person getClient() {
		return client;
	}

	/**
	 * <p>Setter for the field <code>client</code>.</p>
	 *
	 * @param client a {@link ru.extas.model.contacts.Person} object.
	 */
	public void setClient(Person client) {
		this.client = client;
	}

	/**
	 * <p>Getter for the field <code>vendor</code>.</p>
	 *
	 * @return a {@link ru.extas.model.contacts.SalePoint} object.
	 */
	public SalePoint getVendor() {
		return vendor;
	}

	/**
	 * <p>Setter for the field <code>vendor</code>.</p>
	 *
	 * @param vendor a {@link ru.extas.model.contacts.SalePoint} object.
	 */
	public void setVendor(SalePoint vendor) {
		this.vendor = vendor;
	}

	/**
	 * <p>Getter for the field <code>status</code>.</p>
	 *
	 * @return a {@link ru.extas.model.lead.Lead.Status} object.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * <p>Setter for the field <code>status</code>.</p>
	 *
	 * @param status a {@link ru.extas.model.lead.Lead.Status} object.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
}
