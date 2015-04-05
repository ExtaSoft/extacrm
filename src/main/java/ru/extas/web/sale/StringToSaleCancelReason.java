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
				"Отказ контрагентов при рассмотрении заявки (банков, лизинговых компаний и пр.)");
		map.put(Sale.CancelReason.MOTOR_LOST,
				"Отказ клиента по причине отсутствия техники у дилера");
		map.put(Sale.CancelReason.DOWNPAYMENT_TO_BIG,
				"Отказ клиента по причине недостатка средств для первоначального взноса");
		map.put(Sale.CancelReason.DELIVER_ISSUE,
				"Отказ клиента по причине не устроивших условий доставки техники");
		map.put(Sale.CancelReason.PERCENT_TO_BIG,
				"Отказ клиента в связи с высоким % по кредиту");
		map.put(Sale.CancelReason.PRICE_TO_BIG,
				"Отказ клиента в связи с высокой стоимостью техники");
		map.put(Sale.CancelReason.FIRST_CHECK_ISSUE,
				"Несоответствие клиента требованиям контрагента (банков, лизинговых компаний и пр.)");
		map.put(Sale.CancelReason.DEALER_ACCRED_ISSUE,
				"Отсутствие у дилера аккредитации у контрагента/отсутствие контрагента в регионе");
		map.put(Sale.CancelReason.OTHER,
				"Прочие");
		return map;
	}

}
