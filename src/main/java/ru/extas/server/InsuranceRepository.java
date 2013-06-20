package ru.extas.server;

import java.util.Collection;

import ru.extas.model.Insurance;

/**
 * Интерфейс управления данными об иммущественном страховании
 * 
 * @author Valery Orlov
 * 
 */
public interface InsuranceRepository {

	/**
	 * Возвращает список всех зарегестрированных страховок
	 * 
	 * @return список страховок
	 */
	public abstract Collection<Insurance> loadAll();

	/**
	 * Сохраняет страховку
	 * 
	 * @param insurance
	 *            что сохраняем
	 */
	public abstract void persist(Insurance insurance);

	/**
	 * Удаляет запись о страховке
	 * 
	 * @param id
	 *            идентификатор удаляемой записи
	 */
	public abstract void deleteById(String id);

}