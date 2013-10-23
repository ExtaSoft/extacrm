package ru.extas.model;

import javax.persistence.*;
import java.math.BigDecimal;

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
        PENDING,
        CLOSED
    }

    /**
     * Типы продаж
     */
    public enum Type {
        CREDIT,
        INSURANCE
    }

    // Клиент
    @OneToOne
    private Person client;

    @Enumerated(EnumType.STRING)
    private Type type;

    // Продавец (дилер, страх. компания)
    @OneToOne
    private Company vendor;

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
    @Column(name = "MOTOR_PRICE")
    private BigDecimal motorPrice;

    @Column(name = "COMMENT")
    private String comment;

    public Person getClient() {
        return client;
    }

    public void setClient(Person client) {
        this.client = client;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
}
