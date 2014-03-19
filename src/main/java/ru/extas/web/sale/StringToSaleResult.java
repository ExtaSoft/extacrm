/**
 *
 */
package ru.extas.web.sale;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.Sale;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертируем результат завершения продажи в текстовое представление
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Component
public class StringToSaleResult extends String2EnumConverter<Sale.Result> {

	/**
	 * <p>Constructor for StringToSaleResult.</p>
	 */
	public StringToSaleResult() {
		super(Sale.Result.class);
	}

	/** {@inheritDoc} */
	@Override
	protected BiMap<Sale.Result, String> createEnum2StringMap() {
		BiMap<Sale.Result, String> map = HashBiMap.create();
		map.put(Sale.Result.SUCCESSFUL, "Успешное выполнение");
		map.put(Sale.Result.CLIENT_REJECTED, "Отказ клиента");
		map.put(Sale.Result.VENDOR_REJECTED, "Отказ контрагента");
		return map;
	}

}
