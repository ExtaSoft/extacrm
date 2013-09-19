package ru.extas.model;

import org.joda.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Полис страхования
 *
 * @author Valery Orlov
 */
@Entity
@Table(name = "INSURANCE")
public class Insurance extends AbstractExtaObject {

    private static final long serialVersionUID = -1289533183659860816L;

    // Номер полиса
    private String regNum;

    // Номер квитанции А-7
    String a7Num;

    // Дата заключения полиса
    private LocalDate date;

    // Клиент
    @ManyToOne
    private Person client;

    // Предмет страхования - тип
    private String motorType;

    // Предмет страхования - марка
    private String motorBrand;

    // Предмет страхования - модель
    private String motorModel;

    // Страховая сумма, руб.
    private BigDecimal riskSum;

    // Страховая премия, руб.
    private BigDecimal premium;

    // Дата оплаты страховой премии
    private LocalDate paymentDate;

    // Дата начала срока действия договора
    private LocalDate startDate;

    // Дата окончания срока действия договора
    private LocalDate endDate;

    // Точка продажи (Контрагент)
    private String pointOfSale;

    public Insurance() {
    }

    /**
     * @param motorBrand
     * @param riskSum
     */
    public Insurance(String motorBrand, BigDecimal riskSum) {
        this.motorBrand = motorBrand;
        this.riskSum = riskSum;
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
}
