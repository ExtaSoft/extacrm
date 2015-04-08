package ru.extas.model.sale;

import ru.extas.model.common.Comment;
import ru.extas.model.common.FileContainer;
import ru.extas.model.common.ModelUtils;
import ru.extas.model.contacts.*;
import ru.extas.model.lead.Lead;
import ru.extas.model.motor.MotorBrand;
import ru.extas.model.motor.MotorModel;
import ru.extas.model.motor.MotorType;
import ru.extas.model.security.SecuredObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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
public class Sale extends SecuredObject {

    @Column(unique = true, columnDefinition = "BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE")
    private Long num;

    // Клиент
	@OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	@JoinColumn(name = "CLIENT", referencedColumnName = "ID")
	private Client client;

	@Enumerated(EnumType.STRING)
	private Status status;

	// Регион покупки техники
	@Column(name = "REGION", length = AddressInfo.REGION_LENGTH)
    @Size(max = AddressInfo.REGION_LENGTH)
	private String region;

	// Тип техники
	@Column(name = "MOTOR_TYPE", length = MotorType.NAME_LENGTH)
    @Size(max = MotorType.NAME_LENGTH)
	private String motorType;

	// Марка техники
	@Column(name = "MOTOR_BRAND", length = MotorBrand.NAME_LENGTH)
    @Size(max = MotorBrand.NAME_LENGTH)
	private String motorBrand;

	// Модель техники
	@Column(name = "MOTOR_MODEL", length = MotorModel.NAME_LENGTH)
    @Size(max = MotorModel.NAME_LENGTH)
	private String motorModel;

	// Стоимость техники
	@Column(name = "MOTOR_PRICE", precision = 32, scale = 4)
	private BigDecimal motorPrice;

	// Мотосалон
	@OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	private SalePoint dealer;

	@OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	private LegalEntity dealerLE;

    @Column(name = "COMMENT")
    @Size(max = 255)
	private String comment;

    @Column(name = "PROCESS_ID", length = ModelUtils.ACTIVITI_ID_LENGTH)
	private String processId;

	@Enumerated(EnumType.STRING)
    @Column(name = "CANCEL_REASON", length = ModelUtils.ENUM_STRING_LENGTH)
    private CancelReason cancelReason;

	@OneToMany(mappedBy = "sale", targetEntity = ProductInSale.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<ProductInSale> productInSales = newArrayList();

    // Ответственный с нашей стороны
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Employee responsible;

    // Помощник ответственного с нашей стороны
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "RESPONSIBLE_ASSIST_ID")
    private Employee responsibleAssist;

    // Ответственный со стороны дилера
    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "DEALER_MANAGER_ID")
    private Employee dealerManager;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Lead lead;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = Comment.OWNER_ID_COLUMN)
    @OrderBy("createdDate")
    private List<SaleComment> comments = newArrayList();

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = FileContainer.OWNER_ID_COLUMN)
	private List<SaleFileContainer> files = newArrayList();

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(final CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public LegalEntity getDealerLE() {
		return dealerLE;
	}

	public void setDealerLE(final LegalEntity dealerLE) {
		this.dealerLE = dealerLE;
	}

	public List<SaleFileContainer> getFiles() {
		return files;
	}

	public void setFiles(final List<SaleFileContainer> files) {
		this.files = files;
	}

	public Employee getDealerManager() {
        return dealerManager;
    }

    public void setDealerManager(final Employee daelerManager) {
        this.dealerManager = daelerManager;
    }

    public Employee getResponsibleAssist() {
        return responsibleAssist;
    }

    public void setResponsibleAssist(final Employee responsibleAssist) {
        this.responsibleAssist = responsibleAssist;
    }

    public Employee getResponsible() {
        return responsible;
    }

    public void setResponsible(final Employee responsible) {
        this.responsible = responsible;
    }

    public List<SaleComment> getComments() {
        return comments;
    }

    public void setComments(final List<SaleComment> comments) {
        this.comments = comments;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(final Lead lead) {
        this.lead = lead;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(final Long num) {
        this.num = num;
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
	public void setProcessId(final String processId) {
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
	public void setDealer(final SalePoint dealer) {
		this.dealer = dealer;
	}

	/**
	 * <p>Getter for the field <code>client</code>.</p>
	 *
	 * @return a {@link ru.extas.model.contacts.Person} object.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * <p>Setter for the field <code>client</code>.</p>
	 *
	 * @param client a {@link ru.extas.model.contacts.Person} object.
	 */
	public void setClient(final Client client) {
		this.client = client;
	}

	/**
	 * <p>Getter for the field <code>status</code>.</p>
	 *
	 * @return a {@link ru.extas.model.sale.Sale.Status} object.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * <p>Setter for the field <code>status</code>.</p>
	 *
	 * @param status a {@link ru.extas.model.sale.Sale.Status} object.
	 */
	public void setStatus(final Status status) {
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
	public void setRegion(final String region) {
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
	public void setMotorType(final String motorType) {
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
	public void setMotorBrand(final String motorBrand) {
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
	public void setMotorModel(final String motorModel) {
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
	public void setMotorPrice(final BigDecimal motorPrice) {
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
	public void setComment(final String comment) {
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

    /**
	 * Статусы продажи
	 */
	public enum Status {
		NEW,
		CANCELED,
		FINISHED
	}

	/**
	 * Причина отказа продажи
	 */
	public enum CancelReason {
		/**
		 * 1. Отказ контрагентов при рассмотрении заявки (банков, лизинговых компаний и пр.)
		 */
		VENDOR_REJECTED,
		/**
		 * 2. Отказ клиента по причине отсутствия техники у дилера
		 */
		MOTOR_LOST,
		/**
		 * 3. Отказ клиента по причине недостатка средств для первоначального взноса
		 */
		DOWNPAYMENT_TO_BIG,
		/**
		 * 4. Отказ клиента по причине не устроивших условий доставки техники
		 */
		DELIVER_ISSUE,
		/**
		 * 5. Отказ клиента в связи с высоким % по кредиту
		 */
		PERCENT_TO_BIG,
		/**
		 * 6. Отказ клиента в связи с высокой стоимостью техники
		 */
		PRICE_TO_BIG,
		/**
		 * 7. Несоответствие клиента требованиям контрагента (банков, лизинговых компаний и пр.)
		 */
		FIRST_CHECK_ISSUE,
		/**
		 * 8. Отсутствие у дилера аккредитации у контрагента/отсутствие контрагента в регионе
		 */
		DEALER_ACCRED_ISSUE,
		/**
		 * 9. Прочие.
		 */
		OTHER
	}

//	public enum Result {
//		/**
//		 * Успешное выполнение (кредит получен).
//		 */
//		SUCCESSFUL,
//		/**
//		 * Отказ контрагента (банка, дилера).
//		 */
//		VENDOR_REJECTED,
//		/**
//		 * Отказ клиента.
//		 */
//		CLIENT_REJECTED
//	}
}
