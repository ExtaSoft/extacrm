/**
 *
 */
package ru.extas.web.contacts;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import ru.extas.model.Person;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (физ. лица)
 *
 * @author Valery Orlov
 */
public class PersonsGrid extends ExtaGrid {

    public PersonsGrid() {
        super();
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new PersonDataDecl();
    }

    @Override
    protected Container createContainer() {
        final JPAContainer<Person> container = new ExtaDataContainer<>(Person.class);
        container.addNestedContainerProperty("actualAddress.region");
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new UIAction("Новый", "Ввод нового Контакта в систему", "icon-user-add-1") {
            @Override
            public void fire(Object itemId) {
                final BeanItem<Person> newObj = new BeanItem<>(new Person());
                newObj.expandProperty("actualAddress");

                final PersonEditForm editWin = new PersonEditForm("Ввод нового контакта в систему", newObj);
                editWin.addCloseListener(new CloseListener() {

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer<Person>) container).refresh();
                            Notification.show("Контакт сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", "icon-user-2") {
            @Override
            public void fire(final Object itemId) {

                final BeanItem<Person> curObj = new BeanItem<>(((EntityItem<Person>) table.getItem(itemId)).getEntity());
                curObj.expandProperty("actualAddress");

                final PersonEditForm editWin = new PersonEditForm("Редактирование контактных данных", curObj);
                editWin.addCloseListener(new CloseListener() {

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer<Person>) container).refreshItem(itemId);
                            Notification.show("Контакт сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        return actions;
    }
}
