package ru.extas.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Модель данных для продажи
 *
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:12
 */
@Entity
@Table(name = "SALE")
public class Sale extends AbstractExtaObject {

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
	private Company dealer;

	@Column(name = "COMMENT")
	private String comment;

	@Column(name = "PROCESS_ID")
	private String processId;

	@Enumerated(EnumType.STRING)
	private Result result;

	@OneToMany(mappedBy = "sale", targetEntity = ProductInSale.class, cascade = {CascadeType.ALL})
	private List<ProductInSale> productInSales;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Company getDealer() {
		return dealer;
	}

	public void setDealer(Company dealer) {
		this.dealer = dealer;
	}

	public Person getClient() {
		return client;
	}

	public void setClient(Person client) {
		this.client = client;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getMotorType() {
		return motorType;
	}

	public void setMotorType(String motorType) {
		this.motorType = motorType;
	}

	public String getMotorBrand() {
		return motorBrand;
	}

	public void setMotorBrand(String motorBrand) {
		this.motorBrand = motorBrand;
	}

	public String getMotorModel() {
		return motorModel;
	}

	public void setMotorModel(String motorModel) {
		this.motorModel = motorModel;
	}

	public BigDecimal getMotorPrice() {
		return motorPrice;
	}

	public void setMotorPrice(BigDecimal motorPrice) {
		this.motorPrice = motorPrice;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<ProductInSale> getProductInSales() {
		return productInSales;
	}

	public void setProductInSales(final List<ProductInSale> productInSales) {
		this.productInSales = productInSales;
	}
}
