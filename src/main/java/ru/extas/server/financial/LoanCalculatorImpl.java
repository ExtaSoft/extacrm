package ru.extas.server.financial;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.extas.model.sale.ProdCredit;
import ru.extas.model.sale.ProdCreditPercent;
import ru.extas.server.product.ProdCreditRepository;

import javax.inject.Inject;
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

    @Inject
    ProdCreditRepository creditRepository;

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
        final BigDecimal pow = BigDecimal.ONE.add(interestFactor).pow(period, MathContext.DECIMAL128);
        final BigDecimal annuitant = interestFactor.multiply(pow, MathContext.DECIMAL128).divide(pow.subtract(BigDecimal.ONE), MathContext.DECIMAL128);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Рассчет Ежемесячного платежа
        final BigDecimal monthlyPay = annuitant.multiply(creditSum, MathContext.DECIMAL128);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Рассчет полной стоимости кредита
        final BigDecimal creditCost = monthlyPay.multiply(BigDecimal.valueOf(period), MathContext.DECIMAL128);

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
    public BigDecimal calcCreditSum(final ProdCredit product, final BigDecimal price, final BigDecimal downPaymentSum) {
        // Стоимость товара с учетом субсидии
        final BigDecimal subsidyPrice = calcSubsidyPrice(product, price);
        // Возвращаем сумму кредита
        return price.subtract(downPaymentSum);
    }

    /**
     * Стоимость товара с учетом субсидии
     *
     * @param product
     * @param price
     * @return
     */
    private BigDecimal calcSubsidyPrice(final ProdCredit product, final BigDecimal price) {
        return Optional.ofNullable(product.getDealerSubsidy())
                .map(p -> BigDecimal.ZERO.equals(p) ? price : p.multiply(price, MathContext.DECIMAL128))
                .orElse(price);
    }

    @Override
    public BigDecimal   calcInterest(final ProdCredit product, final BigDecimal downPayment, final int period) {
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

    @Override
    public BigDecimal calcDownPayment(final ProdCredit credit, final BigDecimal price, final BigDecimal creditSum) {
        // Стоимость товара с учетом субсидии
        final BigDecimal subsidyPrice = calcSubsidyPrice(credit, price);
        // Возвращаем сумму кредита
        return price.subtract(creditSum);
    }

    /**
     * Найти подходящие кредитные программы и рассчитать их параметры.
     *
     * @param price       цена товара
     * @param downPayment первоначальный платеж (руб.)
     * @param period      срок кредита (месяцы)
     * @return список полностью рассчитанных кредитных продуктов
     */
    @Override
    public List<LoanInfo> findSuitableProducts(BigDecimal price, BigDecimal downPayment, int period) {
        // Выбрать все продукты подходящие под параметры
        List<ProdCredit> products = creditRepository.findSuitableProducts(price, downPayment, period);
        // TODO: To implement
        return null;
    }

    /**
     * Найти подходящие кредитные программы и рассчитать их параметры.
     * В качестве срока кредита берется максимально возможный по программе.
     *
     * @param price       цена товара
     * @param downPayment первоначальный платеж (руб.)
     * @return список полностью рассчитанных кредитных продуктов
     */
    @Override
    public List<LoanInfo> findSuitableProducts(BigDecimal price, BigDecimal downPayment) {
        // TODO: To implement
        return null;
    }

    /**
     * Найти подходящие кредитные программы и рассчитать их параметры
     * В качестве первоначального взноса берется минимально возможный по программе
     *
     * @param price  цена товара
     * @param period срок кредита (месяцы)
     * @return список полностью рассчитанных кредитных продуктов
     */
    @Override
    public List<LoanInfo> findSuitableProducts(BigDecimal price, int period) {
        // TODO: To implement
        return null;
    }

    /**
     * Найти подходящие кредитные программы и рассчитать их параметры
     * В качестве первоначального взноса берется минимально возможный по программе
     * В качестве срока кредита берется максимально возможный по программе.
     *
     * @param price цена товара
     * @return список полностью рассчитанных кредитных продуктов
     */
    @Override
    public List<LoanInfo> findSuitableProducts(BigDecimal price) {
        // TODO: To implement
        return null;
    }

    /**
     * Найти подходящие кредитные продукты и рассчитать их параметры исходя из первоначального взноса.
     *
     * @param price      цена товара
     * @param monthlyPay первоначальный взнос
     * @return список полностью рассчитанных кредитных продуктов
     */
    @Override
    public List<LoanInfo> findSuitableProductsByMonthlyPay(BigDecimal price, BigDecimal monthlyPay) {
        // TODO: To implement
        return null;
    }
}
