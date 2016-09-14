package ru.extas.model.product;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Продукт "Аренда с выкупом"
 *
 * Created by valery on 12.09.16.
 */
@Entity
@DiscriminatorValue("HIRE_PURCHASE")
@Table(name = "PROD_HIRE_PURCHASE")
public class ProdHirePurchase extends Product {

    // Максимальный срок кредита (в месяцах)
    @Column(name = "MAX_PERIOD")
    private int maxPeriod;

    // Минимальный первоначальный взнос
    @Column(name = "MIN_DOWNPAYMENT", precision = 32, scale = 4)
    private BigDecimal minDownpayment;

    // Максимальная сумма рассрочки
    @Column(name = "MAX_SUM", precision = 32, scale = 4)
    private BigDecimal maxSum;

    public int getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(int maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public BigDecimal getMinDownpayment() {
        return minDownpayment;
    }

    public void setMinDownpayment(BigDecimal minDownpayment) {
        this.minDownpayment = minDownpayment;
    }

    public BigDecimal getMaxSum() {
        return maxSum;
    }

    public void setMaxSum(BigDecimal maxSum) {
        this.maxSum = maxSum;
    }
}
