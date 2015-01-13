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

    // Кредитный продукт
    private final ProdCredit product;
    // Цена товара
    private final BigDecimal price;
    // Первоначальный взнос
    private final BigDecimal downPayment;
    // Срок кредита в месяцах
    private final int period;
    // Кредитная ставка
    private final BigDecimal interest;
    // Сумма кредита
    private final BigDecimal creditSum;
    // Коэффициент процентной ставки
    private final BigDecimal interestFactor;
    // Аннуитентныый коэффициент
    private final BigDecimal annuitant;
    // Ежемесячный платеж
    private final BigDecimal monthlyPay;
    // Полная стоимость кредита
    private final BigDecimal creditCost;
    // Переплата
    private final BigDecimal overpayment;
    // Ежегодное удорожание
    private final BigDecimal yearlyRise;
    // Ежемесячное удорожание
    private final BigDecimal monthlyRise;

    public LoanInfo(final ProdCredit product, final BigDecimal price, final BigDecimal downPayment, final int period, final BigDecimal interest, final BigDecimal creditSum, final BigDecimal interestFactor, final BigDecimal annuitant, final BigDecimal monthlyPay, final BigDecimal creditCost, final BigDecimal overpayment, final BigDecimal yearlyRise, final BigDecimal monthlyRise) {
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
