package ru.extas.server;

import java.util.List;

import ru.extas.model.UserProfile;

/**
 * Интерфейс управления пользователями и правами доступа
 * 
 * @author Valery Orlov
 * 
 */
public interface UserManagementService {

	/**
	 * Найти пользователя по логину
	 * 
	 * @param login
	 *            логин
	 * @return найденный пользователь или null
	 */
	UserProfile findUserByLogin(String login);

	/**
	 * Сохранить пользователя
	 * 
	 * @param user
	 *            объект ддя сохранения
	 */
	void persistUser(UserProfile user);

	/**
	 * Загрузить список пользователей
	 * 
	 * @return список пользователей
	 */
	List<UserProfile> loadUsers();

}
