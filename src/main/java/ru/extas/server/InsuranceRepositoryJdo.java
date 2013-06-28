package ru.extas.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Insurance;
import ru.extas.model.PMF;

/**
 * Имплементация сервиса управления имущественными страховками
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceRepositoryJdo implements InsuranceRepository {

	private final Logger logger = LoggerFactory.getLogger(UserManagementServiceJdo.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.InsuranceRepository#getAll()
	 */
	@Override
	public Collection<Insurance> loadAll() {
		logger.info("Requesting insuranses list...");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			List<Insurance> insurances = new ArrayList<Insurance>();
			Extent<Insurance> extent = pm.getExtent(Insurance.class, false);
			for (Insurance insurance : extent) {
				insurances.add(insurance);
			}
			extent.closeAll();
			logger.info("Retrieved {} insuranses", insurances.size());

			return insurances;
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.InsuranceRepository#create(ru.extas.model.Insurance)
	 */
	@Override
	public void persist(Insurance insurance) {
		logger.info("Persisting insurance: {}", insurance.getRegNum());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(insurance);
		} finally {
			pm.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.extas.server.InsuranceRepository#deleteById(java.lang.Long)
	 */
	@Override
	public void deleteById(String id) {
		logger.info("Deleting insurance with id: {}", id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(Insurance.class, id));
		} finally {
			pm.close();
		}
	}

	public void fillRegistry() {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd.MM.yyyy");
		persist(new Insurance("08905/213/00021/3", "СП-2013/00021", fmt.parseLocalDate("21.03.2013").toDate(), "Демидов Сергей Николаевич", fmt
				.parseLocalDate("10.04.1970").toDate(), "89031979067", true, "квадроцикл	", "CFMOTO 	", "CF800-2	", new BigDecimal(335000),
				new BigDecimal(15075), fmt.parseLocalDate("21.03.2013").toDate(), fmt.parseLocalDate("22.03.2013").toDate(), fmt.parseLocalDate(
						"21.03.2014").toDate(), "Филина", "ООО АлексМото"));
		persist(new Insurance("08905/213/00022/3", "СП-2013/00022", fmt.parseLocalDate("21.03.2013").toDate(), "Савельев Денис Владимирович", fmt
				.parseLocalDate("21.06.1976").toDate(), "89166904104", true, "квадроцикл", "CFMOTO ", "CF500A", new BigDecimal(190000),
				new BigDecimal(8550), fmt.parseLocalDate("21.03.2013").toDate(), fmt.parseLocalDate("22.03.2013").toDate(), fmt.parseLocalDate(
						"21.03.2014").toDate(), "Зияева", "ООО Гризли"));
		persist(new Insurance("08905/213/00023/3", "СП-2013/00023", fmt.parseLocalDate("28.03.2013").toDate(), "Аляутдинов Рафаэль Хамитович", fmt
				.parseLocalDate("20.10.1981").toDate(), "89267290790", true, "квадроцикл", "Kawasaki", "KVF650F", new BigDecimal(357397),
				new BigDecimal(16083), fmt.parseLocalDate("27.03.2013").toDate(), fmt.parseLocalDate("28.03.2013").toDate(), fmt.parseLocalDate(
						"27.03.2014").toDate(), "Зияева", "ООО Гризли"));
		persist(new Insurance("08905/213/00024/3", "СП-2013/00024", fmt.parseLocalDate("28.03.2013").toDate(), "Коробейкин Роман Сергеевич", fmt
				.parseLocalDate("05.03.1979").toDate(), "84957781938", true, "квадроцикл", "CFMOTO ", "CF 625 C", new BigDecimal(275000),
				new BigDecimal(12375), fmt.parseLocalDate("28.03.2013").toDate(), fmt.parseLocalDate("29.03.2013").toDate(), fmt.parseLocalDate(
						"28.03.2014").toDate(), "Филина", "ООО АлексМото"));
		persist(new Insurance("08905/213/00025/3", "СП-2013/00025", fmt.parseLocalDate("19.04.2013").toDate(), "Куприянов Кирилл Сергеевич", fmt
				.parseLocalDate("03.05.1975").toDate(), "89161400800", true, "квадроцикл", "Kawasaki", "650", new BigDecimal(400000), new BigDecimal(
				18000), fmt.parseLocalDate("18.04.2013").toDate(), fmt.parseLocalDate("19.04.2013").toDate(), fmt.parseLocalDate("19.04.2014")
				.toDate(), "Зияева", "ООО Гризли"));
		persist(new Insurance("08905/213/00026/3", "СП-2013/00026", fmt.parseLocalDate("19.04.2013").toDate(), "Рогачев Кирилл Евгеньевич", fmt
				.parseLocalDate("17.12.1984").toDate(), "89672084131", true, "квадроцикл", "CFMOTO ", "CF 625 C", new BigDecimal(275000),
				new BigDecimal(12375), fmt.parseLocalDate("19.04.2013").toDate(), fmt.parseLocalDate("19.04.2013").toDate(), fmt.parseLocalDate(
						"18.04.2014").toDate(), "Погосян", null));
		persist(new Insurance("08905/213/00027/3", "СП-2013/00027", fmt.parseLocalDate("08.04.2013").toDate(), "Вакула Леонид Александрович", fmt
				.parseLocalDate("04.12.1984").toDate(), "89160258615", true, "квадроцикл", "CFMOTO ", "X6 (тип СА625-С)", new BigDecimal(270000),
				new BigDecimal(12150), fmt.parseLocalDate("08.04.2013").toDate(), fmt.parseLocalDate("09.04.2013").toDate(), fmt.parseLocalDate(
						"08.04.2013").toDate(), "	Филина", "ООО Алекс мото"));
		persist(new Insurance("08905/213/00028/3", "СП-2013/00028", fmt.parseLocalDate("19.04.2013").toDate(), "Арсенян Саргис Андраникович", fmt
				.parseLocalDate("26.05.1958").toDate(), "89161743229", true, "квадроцикл", "CFMOTO ", "X6 (тип СА625-С)", new BigDecimal(275000),
				new BigDecimal(12375), fmt.parseLocalDate("19.04.2013").toDate(), fmt.parseLocalDate("20.04.2013").toDate(), fmt.parseLocalDate(
						"19.04.2013").toDate(), "	Филина", "ООО Алекс мото"));
	}

}
