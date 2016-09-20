package ru.extas.model.sale;

import ru.extas.model.common.Address;
import ru.extas.model.common.Comment;
import ru.extas.model.common.FileContainer;
import ru.extas.model.common.ModelUtils;
import ru.extas.model.contacts.Client;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.model.motor.MotorInstance;
import ru.extas.model.product.ProductInstance;
import ru.extas.model.security.SecuredObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "CLIENT", referencedColumnName = "ID")
    private Client client;

    // Контактное лицо Клиента (для юриков)
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "CLIENT_EMPLOYEE", referencedColumnName = "ID")
    private Employee clientContact;

    @Enumerated(EnumType.STRING)
    private Status status;

    // Регион покупки техники
    @Column(name = "REGION", length = Address.REGION_WITH_TYPE_LEN)
    @Size(max = Address.REGION_WITH_TYPE_LEN)
    private String region;

    @OneToMany(mappedBy = "sale", targetEntity = SaleMotor.class, orphanRemoval = true, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<MotorInstance> motorInstances = newArrayList();

    // Мотосалон
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private SalePoint dealer;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private LegalEntity dealerLE;

    @Column(name = "COMMENT")
    @Size(max = 255)
    private String comment;

    @Column(name = "PROCESS_ID", length = ModelUtils.ACTIVITI_ID_LENGTH)
    private String processId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CANCEL_REASON", length = ModelUtils.ENUM_STRING_LENGTH)
    private CancelReason cancelReason;

    @OneToMany(mappedBy = "sale", targetEntity = ProductInstance.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ProductInstance> productInstances = newArrayList();

    // Ответственный с нашей стороны
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Employee responsible;

    // Помощник ответственного с нашей стороны
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "RESPONSIBLE_ASSIST_ID")
    private Employee responsibleAssist;

    // Ответственный со стороны дилера
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "DEALER_MANAGER_ID")
    private Employee dealerManager;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Lead lead;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = Comment.OWNER_ID_COLUMN)
    @OrderBy("createdDate")
    private List<SaleComment> comments = newArrayList();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = FileContainer.OWNER_ID_COLUMN)
    private List<SaleFileContainer> files = newArrayList();

    // источник
    @Column(name = "SOURCE", length = 50)
    @Size(max = 50)
    private String source;

    public List<MotorInstance> getMotorInstances() {
        return motorInstances;
    }

    public void setMotorInstances(final List<MotorInstance> motorInstances) {
        this.motorInstances = motorInstances;
    }

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
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProductInstance> getProductInstances() {
        return productInstances;
    }

    /**
     *
     * @param productInstances a {@link java.util.List} object.
     */
    public void setProductInstances(final List<ProductInstance> productInstances) {
        this.productInstances = productInstances;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public Employee getClientContact() {
        return clientContact;
    }

    public void setClientContact(final Employee clientContact) {
        this.clientContact = clientContact;
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
