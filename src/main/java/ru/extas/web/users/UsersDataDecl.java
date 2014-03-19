package ru.extas.web.users;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отрбражения пользователей
 *
 * @author Valery Orlov
 */
class UsersDataDecl extends GridDataDecl {

    /**
     * <p>Constructor for UsersDataDecl.</p>
     */
    public UsersDataDecl() {
        super();
        addMapping("contact.name", "Имя");
        addMapping("login", "Логин (e-mail)");
        addMapping("role", "Роль");
        addMapping("blocked", "Блокирован");
        addMapping("changePassword", "Сменить пароль");
        super.addCreateModifyMarkers();
    }

}
