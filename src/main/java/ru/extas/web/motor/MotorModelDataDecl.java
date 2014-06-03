package ru.extas.web.motor;

import ru.extas.web.commons.GridDataDecl;

/**
 * Created by Valery on 03.06.2014.
 */
public class MotorModelDataDecl extends GridDataDecl {
    public MotorModelDataDecl() {
        addMapping("brand.name", "Марка");
        addMapping("type.name", "Тип техники");
        addMapping("name", "Название модели");
        addMapping("code", "Код модели");
        super.addDefaultMappings();

    }
}
