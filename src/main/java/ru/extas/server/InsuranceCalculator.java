/**
 *
 */
package ru.extas.server;

import ru.extas.model.Insurance;

import java.math.BigDecimal;

/**
 * Калькулятор страховых премий
 *
 * @author Valery Orlov
 * @version $Id: $Id
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
	 * Находит подходящий тариф.
	 *
	 * @param motorBrand бренд страхуемой техники
	 * @param coverTime  период страхования
	 * @return найденный тариф или null
	 * @param isUsed a boolean.
	 */
	BigDecimal findTarif(final String motorBrand, final Insurance.PeriodOfCover coverTime, boolean isUsed);

}
