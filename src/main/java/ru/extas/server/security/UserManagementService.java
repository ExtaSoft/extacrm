package ru.extas.server.security;

import ru.extas.model.contacts.Employee;
import ru.extas.model.security.*;

import java.util.Set;

/**
 * Интерфейс управления пользователями и правами доступа
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
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
    Employee findUserEmployeeByLogin(String login);

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
    Employee getCurrentUserEmployee();

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

    /**
     * Проверяет разрешен ли текущему пользователю доступ к одному из указанных разделов
     *
     * @param domains разделы системы для проверки
     * @return true если хотя бы один из разделов/подразделов доступен
     */
    boolean isPermittedOneOf(Set<ExtaDomain> domains);

    /**
     * Проверяет разрешен ли пользователю доступ к целевым объектам раздела
     *
     * @param domain раздел, объекты которого пповеряются
     * @param target целевые объекты
     * @return true если запроценные целевые объекты доступны
     */
    boolean isPermittedTarget(ExtaDomain domain, SecureTarget target);

    /**
     * Проверяет разрешено ли пользователю действие над целевым объектам раздела
     *
     * @param domain раздел, объекты которого пповеряются
     * @param target целевые объекты
     * @return true если запроценные целевые объекты доступны
     */
    boolean isPermitted(ExtaDomain domain, SecureTarget target, SecureAction action);

    /**
     * Проверяет разрешен ли пользователю доступ к указанному домену
     *
     * @param domain проверяемый домен
     * @return true если доступ разрешен
     */
    boolean isPermittedDomain(ExtaDomain domain);

    SecuredObject saveObjectAccess(SecuredObject securedObject, ObjectSecurityRule rule);

    /**
     * Определяет является ли текущий пользователь "нашим" (сотрудником ЕА)
     */
    boolean isItOurUser();

    /**
     * Определяет разрешен ли доступ к закрытым коментариям
     * @return
     */
    boolean isPermitPrivateComments();
}
