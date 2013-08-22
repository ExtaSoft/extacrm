package ru.extas.server;

import ru.extas.model.Contact;
import ru.extas.model.UserProfile;

import java.util.List;

/**
 * Интерфейс управления пользователями и правами доступа
 *
 * @author Valery Orlov
 */
public interface UserManagementService {

    /**
     * Найти пользователя по логину
     *
     * @param login логин
     * @return найденный пользователь или null
     */
    UserProfile findUserByLogin(String login);

    /**
     * Найти контакт пользователя по логину
     *
     * @param login логин
     * @return найденный контакт пользователя или null
     */
    Contact findUserContactByLogin(String login);

    /**
     * Сохранить пользователя
     *
     * @param user объект ддя сохранения
     */
    void persistUser(UserProfile user);

    /**
     * Загрузить список пользователей
     *
     * @return список пользователей
     */
    List<UserProfile> loadUsers();

    /**
     * Получить профайл стандартного юзера (admin)
     *
     * @return профайл юзера admin
     */
    UserProfile getSuperuser();

    /**
     * Получить профайл текущего пользователя
     *
     * @return профайл текущего пользователя
     */
    UserProfile getCurrentUser();

    /**
     * Получить контакт текущего пользователя
     *
     * @return контакт текущего пользователя
     */
    Contact getCurrentUserContact();
}
