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
import ru.extas.model.Company;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Таблица контактов (физ. лица)
 *
 * @author Valery Orlov
 */
public class CompaniesGrid extends ExtaGrid {

    private static final long serialVersionUID = 2299363623807745654L;

    public CompaniesGrid() {
        super();
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new CompanyDataDecl();
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final JPAContainer<Company> container = new ExtaDataContainer<>(Company.class);
        container.addNestedContainerProperty("actualAddress.region");
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new UIAction("Новый", "Ввод нового Контакта в систему", "icon-doc-new") {
            @Override
            public void fire(Object itemId) {
                final BeanItem<Company> newObj = new BeanItem<>(new Company());
                newObj.expandProperty("actualAddress");

                final CompanyEditForm editWin = new CompanyEditForm("Ввод нового юр. лица в систему", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refresh();
                            Notification.show("Контакт сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", "icon-edit-3") {
            @Override
            public void fire(final Object itemId) {
                final BeanItem<Company> beanItem = new BeanItem<>(((EntityItem<Company>) table.getItem(itemId)).getEntity());
                beanItem.expandProperty("actualAddress");
                final CompanyEditForm editWin = new CompanyEditForm("Редактирование контактных данных", beanItem);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refreshItem(itemId);
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
