package ru.extas.web.tasks;

import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.users.LoginToUserNameConverter;

import java.util.EnumSet;

/**
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 18:08
 */
class TaskDataDecl extends GridDataDecl {
    /**
     * <p>Constructor for TaskDataDecl.</p>
     */
    public TaskDataDecl() {
        addMapping("name", "Название");
        addMapping("description", "Описание", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("priority", "Приоритет", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("dueDate", "Дата завершения");
        addMapping("createTime", "Дата создания", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
	    addMapping("owner", "Владелец", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), LoginToUserNameConverter.class);
	    addMapping("assignee", "Ответственный", LoginToUserNameConverter.class);
    }
}
