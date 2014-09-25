package ru.extas.web.tasks;

import com.vaadin.data.Item;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.DataDeclMapping;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.GridItem;
import ru.extas.web.users.LoginToUserNameConverter;

import java.util.EnumSet;
import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

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
        addMapping("clientName", "Клиент", new ComponentColumnGenerator() {
            @Override
            public Object generateCell(Object columnId, Item item, Object itemId) {
                String clientName = null;
                Task task = GridItem.extractBean(item);
                RuntimeService runtimeService = lookup(RuntimeService.class);
                Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
                if (processVariables.containsKey("lead")) {
                    Lead lead = (Lead) processVariables.get("lead");
                    clientName = lead.getContactName();
                }
                return clientName;
            }
        });
        addMapping("dealerName", "Мотосалон", new ComponentColumnGenerator() {
            @Override
            public Object generateCell(Object columnId, Item item, Object itemId) {
                String dealerName = null;
                Task task = GridItem.extractBean(item);
                RuntimeService runtimeService = lookup(RuntimeService.class);
                Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
                if (processVariables.containsKey("lead")) {
                    Lead lead = (Lead) processVariables.get("lead");
                    dealerName = lead.getPointOfSale();
                }
                return dealerName;
            }
        });
    }
}
