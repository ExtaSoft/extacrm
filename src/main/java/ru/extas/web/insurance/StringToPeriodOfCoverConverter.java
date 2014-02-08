/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;
import ru.extas.model.Insurance;
import ru.extas.web.commons.converters.String2EnumConverter;

/**
 * Конвертируем полис БСО в строку
 *
 * @author Valery Orlov
 */
@Component
public class StringToPeriodOfCoverConverter extends String2EnumConverter<Insurance.PeriodOfCover> {

	public StringToPeriodOfCoverConverter() {
		super(Insurance.PeriodOfCover.class);
	}

	@Override
	protected BiMap<Insurance.PeriodOfCover, String> createEnum2StringMap() {
		BiMap<Insurance.PeriodOfCover, String> map = HashBiMap.create();
		map.put(Insurance.PeriodOfCover.YEAR, "Год");
		map.put(Insurance.PeriodOfCover.HALF_A_YEAR, "Полгода");
		return map;
	}
}
