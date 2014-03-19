/**
 *
 */
package ru.extas.web.insurance;

import ru.extas.web.commons.GridDataDecl;

/**
 * Опции отображения квитанций А-7
 *
 * @author Valery Orlov
 */
class A7FormDataDecl extends GridDataDecl {

    /**
     * <p>Constructor for A7FormDataDecl.</p>
     */
    public A7FormDataDecl() {
        addMapping("regNum", "Номер квитанции");
        addMapping("owner.name", "Владелец");
        addMapping("status", "Статус");
        super.addCreateModifyMarkers();
    }

}
