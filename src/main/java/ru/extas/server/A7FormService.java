/**
 * 
 */
package ru.extas.server;

import java.util.List;

import ru.extas.model.A7Form;
import ru.extas.model.Contact;

/**
 * Управление формами А-7
 * 
 * @author Valery Orlov
 * 
 */
public interface A7FormService {

	/**
	 * Найти квитанцию по номеру
	 * 
	 * @param formNum
	 *            Номер квитанции
	 * @return Найденная квитанция или null
	 */
	A7Form findByNum(String formNum);

	/**
	 * Найти набор квитанций по номенам
	 * 
	 * @param formNums
	 *            Номера квитанций
	 * @return Найденные квитанции или null
	 */
	List<A7Form> findByNum(List<String> formNums);

	/**
	 * Вставить/обновить квитанцию
	 * 
	 * @param form
	 *            Квитанцию для вставки\обновления
	 */
	void persist(A7Form form);

	/**
	 * Использовать квитанцию
	 * 
	 * @param formNum
	 *            номер квитанции
	 */
	void spendForm(String formNum);

	/**
	 * Сменить владельца для набора квитанций
	 * 
	 * @param formNums
	 *            Список номеров квитанций
	 * @param owner
	 *            Новый владелец
	 */
	void changeOwner(List<String> formNums, Contact owner);

	/**
	 * Сменить статус для набора квитанций
	 * 
	 * @param formNums
	 *            Список номеров квитанций
	 * @param newStatus
	 *            Новый статус
	 */
	void changeStatus(List<String> formNums, A7Form.Status newStatus);
}
