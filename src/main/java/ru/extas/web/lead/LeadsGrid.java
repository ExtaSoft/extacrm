package ru.extas.web.lead;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import ru.extas.model.Lead;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 */
public class LeadsGrid extends ExtaGrid {
    private static final long serialVersionUID = 4876073256421755574L;

    public LeadsGrid() {
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new LeadDataDecl();
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final JPAContainer<Lead> container = new ExtaDataContainer<>(Lead.class);
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new UIAction("Новый", "Ввод нового лида", "icon-doc-new") {
            @Override
            public void fire(Object itemId) {
                final BeanItem<Lead> newObj = new BeanItem<>(new Lead());

                final LeadEditForm editWin = new LeadEditForm("Ввод нового лида в систему", newObj);
                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refresh();
                            Notification.show("Лид сохранен", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке лид", "icon-edit-3") {
            @Override
            public void fire(final Object itemId) {
                final BeanItem<Lead> curObj = new BeanItem<>(((EntityItem<Lead>) table.getItem(itemId)).getEntity());

                final LeadEditForm editWin = new LeadEditForm("Редактирование лида", curObj);
                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refreshItem(itemId);
                            Notification.show("Лид сохранен", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        return actions;
    }

}
