package ru.extas.server.financial;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProdCreditPercent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Имплементация кредитных рассчетов
 *
 * @author Valery Orlov
 *         Date: 05.12.2014
 *         Time: 13:29
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class LoanCalculatorImpl implements LoanCalculator {

    @Override
    public LoanInfo calc(final ProdCredit product, final BigDecimal price, final BigDecimal downPaymentSum, final int period) {

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Определение входных данных кредита
        final BigDecimal downPayment = downPaymentSum.divide(price, MathContext.DECIMAL128);
        // Процентная ставка по программе
        final BigDecimal interest = calcInterest(product, downPayment, period);
        // Сумма кредита
        final BigDecimal creditSum = calcCreditSum(product, price, downPaymentSum);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Рассчет аннуитентного коэффициента
        //  annuitant = interestFactor * (1 + interestFactor)^period / ((1 + interestFactor)^period - 1)
        //      interestFactor - это коэффициент процентной ставки
        final BigDecimal interestFactor = interest.divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);
        final BigDecimal pow = BigDecimal.ONE.add(interestFactor).pow(period);
        final BigDecimal annuitant = interestFactor.multiply(pow).divide(pow.subtract(BigDecimal.ONE), MathContext.DECIMAL128);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Рассчет Ежемесячного платежа
        final BigDecimal monthlyPay = annuitant.multiply(creditSum);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Рассчет полной стоимости кредита
        final BigDecimal creditCost = monthlyPay.multiply(BigDecimal.valueOf(period));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Рассчет переплаты
        final BigDecimal overpayment = creditCost.subtract(creditSum);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Среднегодовое удорожание кредита = [переплата] / ([сумма кредита] * [срок кредита] / 12)
        final BigDecimal yearlyRise = overpayment.divide(
                creditSum.multiply(BigDecimal.valueOf(period)).divide(BigDecimal.valueOf(12), MathContext.DECIMAL128)
                , MathContext.DECIMAL128);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Среднемесячное удорожание кредита
        final BigDecimal monthlyRise = yearlyRise.divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);

        return new LoanInfo(product, price, downPaymentSum, period, interest, creditSum,
                interestFactor, annuitant, monthlyPay, creditCost, overpayment, yearlyRise, monthlyRise);
    }

    @Override
    public BigDecimal calcCreditSum(ProdCredit product, BigDecimal price, BigDecimal downPaymentSum) {
        // Стоимость товара с учетом субсидии
        final BigDecimal subsidyPrice = Optional.ofNullable(product.getDealerSubsidy())
                .map(p -> BigDecimal.ZERO.equals(p) ? price : p.multiply(price)).orElse(price);
        // Возвращаем сумму кредита
        return price.subtract(downPaymentSum);
    }

    @Override
    public BigDecimal calcInterest(ProdCredit product, BigDecimal downPayment, int period) {
        final List<ProdCreditPercent> percents = newArrayList(product.getPercents());
        if (percents.size() < 2)
            return percents.stream()
                    .findFirst()
                    .map(p -> p.getPercent())
                    .orElse(null);
        else
            return percents.stream()
                    .filter(p -> {
                        final int i = p.getDownpayment().compareTo(downPayment);
                        return p.getPeriod() <= period && (i == 0 || i == -1);
                    })
                    .max((a, b) ->
                            a.getDownpayment().add(BigDecimal.valueOf(a.getPeriod()))
                                    .compareTo(b.getDownpayment().add(BigDecimal.valueOf(b.getPeriod()))))
                    .map(p -> p.getPercent()).orElse(null);
    }
}
