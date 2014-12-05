package ru.extas.server.financial;

import ru.extas.model.sale.ProdCredit;

import java.math.BigDecimal;

/**
 * Полные данные о кредите после рассчета
 *
 * @author Valery Orlov
 *         Date: 05.12.2014
 *         Time: 15:49
 */
public class LoanInfo {

    private final ProdCredit product;
    private final BigDecimal price;
    private final BigDecimal downPayment;
    private final int period;
    private final BigDecimal interest;
    private final BigDecimal creditSum;
    private final BigDecimal interestFactor;
    private final BigDecimal annuitant;
    private final BigDecimal monthlyPay;
    private final BigDecimal creditCost;
    private final BigDecimal overpayment;
    private final BigDecimal yearlyRise;
    private final BigDecimal monthlyRise;

    public LoanInfo(ProdCredit product, BigDecimal price, BigDecimal downPayment, int period, BigDecimal interest, BigDecimal creditSum, BigDecimal interestFactor, BigDecimal annuitant, BigDecimal monthlyPay, BigDecimal creditCost, BigDecimal overpayment, BigDecimal yearlyRise, BigDecimal monthlyRise) {
        this.product = product;
        this.price = price;
        this.downPayment = downPayment;
        this.period = period;
        this.interest = interest;
        this.creditSum = creditSum;
        this.interestFactor = interestFactor;
        this.annuitant = annuitant;
        this.monthlyPay = monthlyPay;
        this.creditCost = creditCost;
        this.overpayment = overpayment;
        this.yearlyRise = yearlyRise;
        this.monthlyRise = monthlyRise;
    }

    public ProdCredit getProduct() {
        return product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public int getPeriod() {
        return period;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public BigDecimal getCreditSum() {
        return creditSum;
    }

    public BigDecimal getInterestFactor() {
        return interestFactor;
    }

    public BigDecimal getAnnuitant() {
        return annuitant;
    }

    public BigDecimal getMonthlyPay() {
        return monthlyPay;
    }

    public BigDecimal getCreditCost() {
        return creditCost;
    }

    public BigDecimal getOverpayment() {
        return overpayment;
    }

    public BigDecimal getYearlyRise() {
        return yearlyRise;
    }

    public BigDecimal getMonthlyRise() {
        return monthlyRise;
    }
}
