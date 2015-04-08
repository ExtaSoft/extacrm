/**
 *
 */
package ru.extas.web.sale;

import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.sale.Sale;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертируем причину отмены продажи в текстовое представление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Component
public class StringToSaleCancelReason extends String2EnumConverter<Sale.CancelReason> {

	/**
	 * <p>Constructor for StringToSaleResult.</p>
	 */
	public StringToSaleCancelReason() {
		super(Sale.CancelReason.class);
	}

	/** {@inheritDoc} */
	@Override
	protected HashBiMap<Sale.CancelReason, String> createEnum2StringMap() {
        final HashBiMap<Sale.CancelReason, String> map = HashBiMap.create();
		map.put(Sale.CancelReason.VENDOR_REJECTED,
				"Отказ контрагента (банков, лизинговых компаний и пр.)");
		map.put(Sale.CancelReason.MOTOR_LOST,
				"Отказ клиента - нет техники у дилера");
		map.put(Sale.CancelReason.DOWNPAYMENT_TO_BIG,
				"Отказ клиента - нет первоначального взноса");
		map.put(Sale.CancelReason.DELIVER_ISSUE,
				"Отказ клиента - плохие условия доставки техники");
		map.put(Sale.CancelReason.PERCENT_TO_BIG,
				"Отказ клиента - высокий % по кредиту");
		map.put(Sale.CancelReason.PRICE_TO_BIG,
				"Отказ клиента - дорогая техника");
		map.put(Sale.CancelReason.FIRST_CHECK_ISSUE,
				"Несоответствие клиента требованиям контрагента");
		map.put(Sale.CancelReason.DEALER_ACCRED_ISSUE,
				"Дилер не аккредитован у контрагента/нет контрагента в регионе");
		map.put(Sale.CancelReason.OTHER,
				"Прочие");
		return map;
	}

}
