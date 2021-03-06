package ru.extas.web.users;

import ru.extas.web.commons.GridDataDecl;

/**
 * Описание данных списка групп кураторов
 *
 * @author Valery Orlov
 *         Date: 21.06.2014
 *         Time: 13:46
 * @version $Id: $Id
 * @since 0.5.0
 */
public class CuratorsGroupDataDecl extends GridDataDecl {

    /**
     * <p>Constructor for .</p>
     */
    public CuratorsGroupDataDecl() {
        addMapping("name", "Название");
        addMapping("description", "Описпние");
        super.addDefaultMappings();
    }
}
