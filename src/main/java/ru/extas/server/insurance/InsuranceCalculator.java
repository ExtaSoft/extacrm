/**
 *
 */
package ru.extas.server.insurance;

import ru.extas.model.insurance.Insurance;

import java.math.BigDecimal;

/**
 * Калькулятор страховых премий
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public interface InsuranceCalculator {

    /**
     * Рассчитывает размер страховой премии для имущественной страховки
     *
     * @param ins параметры страховки
     * @return расчетная страховая премия
     */
    BigDecimal calcPropInsPremium(Insurance ins);

    /**
     * Рассчитывает размер страховой премии для имущественной страховки
     *
     * @param motorBrand марка техники
     * @param riskSum страховая сумма (стоимость техники)
     * @param coverTime период кредитования
     * @param usedMotor признак б/у техники
     * @return возвращает сумму страхвой премии
     */
    BigDecimal calcPropInsPremium(String motorBrand, BigDecimal riskSum, Insurance.PeriodOfCover coverTime, boolean usedMotor);

    /**
	 * Находит подходящий тариф.
	 *
	 * @param motorBrand бренд страхуемой техники
	 * @param coverTime  период страхования
     * @param isUsed a boolean.
     * @return найденный тариф или null
	 */
	BigDecimal findTarif(final String motorBrand, final Insurance.PeriodOfCover coverTime, boolean isUsed);

}
