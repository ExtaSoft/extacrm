package ru.extas.web.commons;

import com.vaadin.server.Resource;

import java.util.List;

/**
 * Группа действий (как бы подменю)
 *
 * @author Valery Orlov
 *         Date: 03.08.2014
 *         Time: 16:15
 */
public abstract class UIActionGroup extends UIAction{

    private final List<UIAction> actionsGroup;

    protected UIActionGroup(String name, String description, Resource icon) {
        super(name, description, icon);
        actionsGroup = makeActionsGroup();
    }

    public List<UIAction> getActionsGroup() {
        return actionsGroup;
    }

    abstract protected List<UIAction> makeActionsGroup();

    @Override
    public void fire(Object itemId) {

    }
}
