package ru.extas.model.product;

import ru.extas.model.common.AuditedObject;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Статьи расходов в продукте
 * <p>
 * Created by valery on 14.09.16.
 */
@Entity
@Table(name = "PROD_HIRE_PURCHASE_EXPENDITURE")
public class ProductExpenditure extends AuditedObject {

    public ProductExpenditure(final ProductInstance productInst) {
        this.productInstance = productInst;
    }

    public ProductExpenditure() {
    }

    public enum Type {
        /**
         * Страхование КАСКО
         */
        INS_KASKO,
        /**
         * Страхование ТОТАЛ+УГОН
         */
        INS_TOTAL_AND_STEALING,
        /**
         * Страхование Кража,
         */
        INS_STEALING,
        /**
         * Страхование ОСАГО,
         */
        INS_OSAGO,
        /**
         * Транспортный налог
         */
        TAX_PAY
    }

    @Column(name = "TYPE")
    @Enumerated
    private Type type;

    @Column(name = "COST", precision = 32, scale = 4)
    private BigDecimal cost;

    @Column(name = "COMMENT")
    @Size(max = 255)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private ProductInstance productInstance;

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(final BigDecimal cost) {
        this.cost = cost;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public ProductInstance getProductInstance() {
        return productInstance;
    }

    public void setProductInstance(final ProductInstance productInstance) {
        this.productInstance = productInstance;
    }
}
