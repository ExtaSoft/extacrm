package ru.extas.web.tasks;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;

import java.util.EnumSet;

/**
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 18:08
 */
class TaskDataDecl extends GridDataDecl {
    public TaskDataDecl() {
        addMapping("name", "Название");
        addMapping("description", "Описание");
        addMapping("priority", "Приоритет", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("dueDate", "Дата завершения");
        addMapping("createTime", "Дата создания", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("owner", "Владелец", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("assignee", "Ответственный");
        addMapping("id", "Идентификатор", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
    }
}
