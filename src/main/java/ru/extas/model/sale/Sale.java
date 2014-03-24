package ru.extas.model.sale;

import ru.extas.model.common.ChangeMarkedObject;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Модель данных для продажи
 *
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:12
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "SALE")
public class Sale extends ChangeMarkedObject {

	/**
	 * Статусы продажи
	 */
	public enum Status {
		NEW,
		CANCELED,
		FINISHED
	}

	/**
	 * Результат завершения продажи
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

	// Клиент
	@OneToOne
	private Person client;

	@Enumerated(EnumType.STRING)
	private Status status;

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
	@OneToOne
	private SalePoint dealer;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "PROCESS_ID")
	private String processId;

	@Enumerated(EnumType.STRING)
	private Result result;

	@OneToMany(mappedBy = "sale", targetEntity = ProductInSale.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<ProductInSale> productInSales;

	/**
	 * <p>Getter for the field <code>result</code>.</p>
	 *
	 * @return a {@link Sale.Result} object.
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * <p>Setter for the field <code>result</code>.</p>
	 *
	 * @param result a {@link Sale.Result} object.
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
	 * <p>Getter for the field <code>dealer</code>.</p>
	 *
	 * @return a {@link ru.extas.model.contacts.SalePoint} object.
	 */
	public SalePoint getDealer() {
		return dealer;
	}

	/**
	 * <p>Setter for the field <code>dealer</code>.</p>
	 *
	 * @param dealer a {@link ru.extas.model.contacts.SalePoint} object.
	 */
	public void setDealer(SalePoint dealer) {
		this.dealer = dealer;
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
	 * <p>Getter for the field <code>status</code>.</p>
	 *
	 * @return a {@link Sale.Status} object.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * <p>Setter for the field <code>status</code>.</p>
	 *
	 * @param status a {@link Sale.Status} object.
	 */
	public void setStatus(Status status) {
		this.status = status;
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
	 * <p>Getter for the field <code>productInSales</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ProductInSale> getProductInSales() {
		return productInSales;
	}

	/**
	 * <p>Setter for the field <code>productInSales</code>.</p>
	 *
	 * @param productInSales a {@link java.util.List} object.
	 */
	public void setProductInSales(final List<ProductInSale> productInSales) {
		this.productInSales = productInSales;
	}
}
