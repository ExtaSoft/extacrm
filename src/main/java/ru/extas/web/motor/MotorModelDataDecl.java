package ru.extas.web.motor;

import ru.extas.web.commons.GridDataDecl;

/**
 * Created by Valery on 03.06.2014.
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
public class MotorModelDataDecl extends GridDataDecl {
    /**
     * <p>Constructor for MotorModelDataDecl.</p>
     */
    public MotorModelDataDecl() {
        addMapping("brand", "Марка");
        addMapping("type", "Тип техники");
        addMapping("name", "Название модели");
        addMapping("code", "Код модели");
        super.addDefaultMappings();

    }
}
