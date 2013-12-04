package ru.extas.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Модель данных для Лида
 *
 * @author Valery Orlov
 *         Date: 03.10.13
 *         Time: 10:09
 */
@Entity
@Table(name = "LEAD")
public class Lead extends AbstractExtaObject {

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
    @Column(name = "MOTOR_PRICE")
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
    private Company vendor;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "PROCESS_ID")
    private String processId;

    @Enumerated(EnumType.STRING)
    private Result result;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Person getClient() {
        return client;
    }

    public void setClient(Person client) {
        this.client = client;
    }

    public Company getVendor() {
        return vendor;
    }

    public void setVendor(Company vendor) {
        this.vendor = vendor;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
