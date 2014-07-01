package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window;
import ru.extas.model.security.UserGroup;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Грид управляет группами пользователей
 *
 * @author Valery Orlov
 *         Date: 20.06.2014
 *         Time: 10:48
 */
public class UserGroupGrid extends ExtaGrid {
    @Override
    protected GridDataDecl createDataDecl() {
        return new UserGroupDataDecl();
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDataContainer<UserGroup> container = new ExtaDataContainer<>(UserGroup.class);
//        container.addNestedContainerProperty("contact.name");
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new UIAction("Новая", "Ввод новой группы пользователей в систему", "icon-user-add") {
            @Override
            public void fire(Object itemId) {
                final BeanItem<UserGroup> newObj = new BeanItem<>(new UserGroup());

                final UserGroupEditForm editWin = new UserGroupEditForm("Ввод новой группы", newObj);
                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            refreshContainer();
                        }
                    }
                });
                editWin.showModal();
            }
        });


        actions.add(new DefaultAction("Изменить", "Редактирование группы", "icon-user-1") {
            @Override
            public void fire(final Object itemId) {
                final BeanItem<UserGroup> curObj = new GridItem<>(table.getItem(itemId));

                final UserGroupEditForm editWin = new UserGroupEditForm("Редактирование группы", curObj);
                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            refreshContainerItem(itemId);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        actions.add(new ItemAction("Копировать", "Копировать текущую группу в новую запись", "icon-copycard") {
            @Override
            public void fire(final Object itemId) {
                final UserGroup curObj = GridItem.extractBean(table.getItem(itemId));

                UserGroup copy = curObj.clone();
                copy.setName("Копия - " + curObj.getName());
                final UserGroupEditForm editWin = new UserGroupEditForm("Редактирование копируемой группы", new BeanItem<>(copy));
                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            refreshContainerItem(itemId);
                        }
                    }
                });
                editWin.setModified(true);
                editWin.showModal();
            }
        });
        return actions;
    }
}
