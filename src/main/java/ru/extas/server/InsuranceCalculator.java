/**
 * 
 */
package ru.extas.server;

import java.math.BigDecimal;

import ru.extas.model.Insurance;

/**
 * Калькулятор страховых премий
 * 
 * @author Valery Orlov
 * 
 */
public interface InsuranceCalculator {

	/**
	 * Рассчитывает размер страховой премии для имущественной страховки
	 * 
	 * @param ins
	 *            параметры страховки
	 * @return расчетная страховая премия
	 */
	BigDecimal calcPropInsPremium(Insurance ins);
}
