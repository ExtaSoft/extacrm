/**
 *
 */
package ru.extas.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Заполнение простых справочников
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class SupplementServiceImpl implements SupplementService {
	// TODO Перенести данные в базу или в кэш

	static private class MotorTypesFactory {
		static final List<String> INSTANCE;

		static {
			// INSTANCE = newArrayList("Мотоцикл", "Скутер", "Квадроцикл",
			// "Снегоход", "Гидроцикл", "Лодочный мотор", "Лодка", "Катер",
			// "Снегоболотоход", "Мотовездеход");
			INSTANCE = newArrayList("Квадроцикл", "Снегоход", "Снегоболотоход", "Мотовездеход", "Гидроцикл");
			Collections.sort(INSTANCE);
		}
	}

	static private class MotorBrandsFactory {
		static final List<String> INSTANCE;

		static {
			// INSTANCE = newArrayList("Ducati", "Kawasaki", "Yamaha",
			// "Yamarin", "Husqvarna", "Arctic Cat", "Polaris", "CFMOTO", "SYM",
			// "Honda", "Suzuki", "CECTEK", "BRP", "Harley-Davidson", "KTM",
			// "Royal Enfield", "Tohatsu", "Mercury", "Solano", "Silver", "UMS",
			// "WindBoat", "Moto Guzzi", "Aprilia", "DERBI", "Piaggio", "Vespa",
			// "Gilera", "Русская механика", "Gamax");
			INSTANCE = newArrayList("CFMOTO", "Kawasaki", "Arctic Cat", "Polaris", "CECTEK", "Baltmotors",
					"Suzuki", "Honda", "Polar Fox", "BRP", "Тингер", "Yamaha", "STELS");
			Collections.sort(INSTANCE);
		}
	}

	static private class DocumentTypesFactory {
		static final List<String> INSTANCE;

		static {
			INSTANCE = newArrayList("Паспорт", "ПТС", "СТС", "Загранпаспорт", "Полис ДМС", "Справка 2НДФЛ",
					"Водительское удостоверение", "СНИЛС", "Трудовая книжка", "Справка о доходах (по форме банка)");
			Collections.sort(INSTANCE);
		}
	}

	static private class RegionCapitalsFactory {
		static final BiMap<String, String> INSTANCE;

		static {
			// Источник:
			// http://ru.wikipedia.org/wiki/%D0%A4%D0%B5%D0%B4%D0%B5%D1%80%D0%B0%D1%82%D0%B8%D0%B2%D0%BD%D0%BE%D0%B5_%D1%83%D1%81%D1%82%D1%80%D0%BE%D0%B9%D1%81%D1%82%D0%B2%D0%BE_%D0%A0%D0%BE%D1%81%D1%81%D0%B8%D0%B8
			INSTANCE = HashBiMap.create(128);
			INSTANCE.put("Адыгея", "Майкоп");
			INSTANCE.put("Алтай", "Горно-Алтайск");
			INSTANCE.put("Башкортостан", "Уфа");
			INSTANCE.put("Бурятия", "Улан-Удэ");
			INSTANCE.put("Дагестан", "Махачкала");
			INSTANCE.put("Ингушетия", "Магас");
			INSTANCE.put("Кабардино-Балкария", "Нальчик");
			INSTANCE.put("Калмыкия", "Элиста");
			INSTANCE.put("Карачаево-Черкесия", "Черкесск");
			INSTANCE.put("Карелия", "Петрозаводск");
			INSTANCE.put("Коми", "Сыктывкар");
			INSTANCE.put("Марий Эл", "Йошкар-Ола");
			INSTANCE.put("Мордовия", "Саранск");
			INSTANCE.put("Саха (Якутия)", "Якутск");
			INSTANCE.put("Северная Осетия — Алания", "Владикавказ");
			INSTANCE.put("Татарстан", "Казань");
			INSTANCE.put("Тыва (Тува)", "Кызыл");
			INSTANCE.put("Удмуртия", "Ижевск");
			INSTANCE.put("Хакасия", "Абакан");
			INSTANCE.put("Чечня", "Грозный");
			INSTANCE.put("Чувашия", "Чебоксары");
			INSTANCE.put("Алтайский край", "Барнаул");
			INSTANCE.put("Забайкальский край", "Чита");
			INSTANCE.put("Камчатский край", "Петропавловск-Камчатский");
			INSTANCE.put("Краснодарский край", "Краснодар");
			INSTANCE.put("Красноярский край", "Красноярск");
			INSTANCE.put("Пермский край", "Пермь");
			INSTANCE.put("Приморский край", "Владивосток");
			INSTANCE.put("Ставропольский край", "Ставрополь");
			INSTANCE.put("Хабаровский край", "Хабаровск");
			INSTANCE.put("Амурская область", "Благовещенск");
			INSTANCE.put("Архангельская область", "Архангельск");
			INSTANCE.put("Астраханская область", "Астрахань");
			INSTANCE.put("Белгородская область", "Белгород");
			INSTANCE.put("Брянская область", "Брянск");
			INSTANCE.put("Владимирская область", "Владимир");
			INSTANCE.put("Волгоградская область", "Волгоград");
			INSTANCE.put("Вологодская область", "Вологда");
			INSTANCE.put("Воронежская область", "Воронеж");
			INSTANCE.put("Ивановская область", "Иваново");
			INSTANCE.put("Иркутская область", "Иркутск");
			INSTANCE.put("Калининградская область", "Калининград");
			INSTANCE.put("Калужская область", "Калуга");
			INSTANCE.put("Кемеровская область", "Кемерово");
			INSTANCE.put("Кировская область", "Киров");
			INSTANCE.put("Костромская область", "Кострома");
			INSTANCE.put("Курганская область", "Курган");
			INSTANCE.put("Курская область", "Курск");
			INSTANCE.put("Ленинградская область", "Пушкин");
			INSTANCE.put("Липецкая область", "Липецк");
			INSTANCE.put("Магаданская область", "Магадан");
			INSTANCE.put("Московская область", "Красногорск");
			INSTANCE.put("Мурманская область", "Мурманск");
			INSTANCE.put("Нижегородская область", "Нижний Новгород");
			INSTANCE.put("Новгородская область", "Великий Новгород");
			INSTANCE.put("Новосибирская область", "Новосибирск");
			INSTANCE.put("Омская область", "Омск");
			INSTANCE.put("Оренбургская область", "Оренбург");
			INSTANCE.put("Орловская область", "Орёл");
			INSTANCE.put("Пензенская область", "Пенза");
			INSTANCE.put("Псковская область", "Псков");
			INSTANCE.put("Ростовская область", "Ростов-на-Дону");
			INSTANCE.put("Рязанская область", "Рязань");
			INSTANCE.put("Самарская область", "Самара");
			INSTANCE.put("Саратовская область", "Саратов");
			INSTANCE.put("Сахалинская область", "Южно-Сахалинск");
			INSTANCE.put("Свердловская область", "Екатеринбург");
			INSTANCE.put("Смоленская область", "Смоленск");
			INSTANCE.put("Тамбовская область", "Тамбов");
			INSTANCE.put("Тверская область", "Тверь");
			INSTANCE.put("Томская область", "Томск");
			INSTANCE.put("Тульская область", "Тула");
			INSTANCE.put("Тюменская область", "Тюмень");
			INSTANCE.put("Ульяновская область", "Ульяновск");
			INSTANCE.put("Челябинская область", "Челябинск");
			INSTANCE.put("Ярославская область", "Ярославль");
			INSTANCE.put("Москва", "Москва");
			INSTANCE.put("Санкт-Петербург", "Санкт-Петербург");
			INSTANCE.put("Еврейская АО", "Биробиджан");
			INSTANCE.put("Ненецкий АО", "Нарьян-Мар");
			INSTANCE.put("Ханты-Мансийский АО — Югра", "Ханты-Мансийск");
			INSTANCE.put("Чукотский АО", "Анадырь");
			INSTANCE.put("Ямало-Ненецкий АО", "Салехард");

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.SupplementService#loadRegions()
	 */
	/** {@inheritDoc} */
	@Override
	public Collection<String> loadRegions() {
		return getRegionCapitals().keySet();
	}

	private BiMap<String, String> getRegionCapitals() {
		return RegionCapitalsFactory.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.SupplementService#loadCities()
	 */
	/** {@inheritDoc} */
	@Override
	public Collection<String> loadCities() {
		return getRegionCapitals().values();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.SupplementService#findCityByRegion(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Override
	public String findCityByRegion(String region) {
		return getRegionCapitals().get(region);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.SupplementService#findRegionByCity(java.lang.String)
	 */
	/** {@inheritDoc} */
	@Override
	public String findRegionByCity(String city) {

		return getRegionCapitals().inverse().get(city);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.SupplementService#loadMotorTypes()
	 */
	/** {@inheritDoc} */
	@Override
	public Collection<String> loadMotorTypes() {
		return MotorTypesFactory.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ru.extas.server.SupplementService#loadMotorBrands()
	 */
	/** {@inheritDoc} */
	@Override
	public Collection<String> loadMotorBrands() {
		return MotorBrandsFactory.INSTANCE;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<String> loadDocumentTypes() {
		return DocumentTypesFactory.INSTANCE;
	}
}
