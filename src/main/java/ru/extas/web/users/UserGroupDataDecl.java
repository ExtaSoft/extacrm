package ru.extas.web.users;

import ru.extas.web.commons.GridDataDecl;

/**
 * Описание данных списка групп пользователей
 *
 * @author Valery Orlov
 *         Date: 21.06.2014
 *         Time: 13:46
 */
public class UserGroupDataDecl extends GridDataDecl {

    public UserGroupDataDecl() {
        addMapping("name", "Название");
        addMapping("description", "Описпние");
        super.addDefaultMappings();
    }
}
