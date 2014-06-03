package ru.extas.web.motor;

import ru.extas.web.commons.GridDataDecl;

/**
 * Created by Valery on 03.06.2014.
 */
public class MotorTypeDataDecl extends GridDataDecl {
    public MotorTypeDataDecl() {
        addMapping("name", "Название");
        super.addDefaultMappings();
    }
}
