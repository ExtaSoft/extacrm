package ru.extas.server;

import ru.extas.model.Contact;
import ru.extas.model.UserProfile;
import ru.extas.model.UserRole;

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
 *
 * @return найденный пользователь или null
 */
UserProfile findUserByLogin(String login);

/**
 * Найти контакт пользователя по логину
 *
 * @param login логин
 *
 * @return найденный контакт пользователя или null
 */
Contact findUserContactByLogin(String login);

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
 * Получить логин текущего пользователя
 *
 * @return логин текущего пользователя
 */
String getCurrentUserLogin();

/**
 * Получить контакт текущего пользователя
 *
 * @return контакт текущего пользователя
 */
Contact getCurrentUserContact();

/**
 * Определить есть ли аутентифицированный пользователь
 *
 * @return true если есть аутентифицированный пользователь
 */
boolean isUserAuthenticated();

/**
 * Определить облидает ли текущий пользователь указанной ролью
 *
 * @param role проверяемая роль
 *
 * @return true если текущий пользователь облидает указанной ролью
 */
boolean isCurUserHasRole(UserRole role);

/**
 * Аутентифицировать пользователя.
 * Выбрасывает исключение при неудачной аутентификации
 *
 * @param login    логин
 * @param password пароль
 */
void authenticate(String login, String password);

/**
 * Выйти из системы
 */
void logout();
}
