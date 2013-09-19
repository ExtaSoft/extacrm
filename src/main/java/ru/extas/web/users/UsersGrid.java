/**
 *
 */
package ru.extas.web.users;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.UserProfile;
import ru.extas.web.commons.ExtaDataContainer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Таблица пользователей
 *
 * @author Valery Orlov
 */
public class UsersGrid extends CustomComponent {

    private static final long serialVersionUID = -4385482673967616119L;
    private final Logger logger = LoggerFactory.getLogger(UsersGrid.class);
    private final Table table;

    /**
     *
     */
    public UsersGrid() {
        super();
        // Запрос данных
        final JPAContainer<UserProfile> container = new ExtaDataContainer<>(UserProfile.class);
        container.addNestedContainerProperty("contact.name");

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Формируем тулбар
        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);

        final Button newBtn = new Button("Новый");
        newBtn.addStyleName("icon-user-add");
        newBtn.setDescription("Ввод нового пользователя в систему");
        newBtn.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.debug("New User...");
                final BeanItem<UserProfile> newObj = new BeanItem<>(new UserProfile());

                final UserEditForm editWin = new UserEditForm("Ввод нового пользователя в систему", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.refresh();
                            Notification.show("Пользователь сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        commandBar.addComponent(newBtn);

        final Button editBtn = new Button("Изменить");
        editBtn.addStyleName("icon-user-1");
        editBtn.setDescription("Редактирование данных пользователя");
        editBtn.setEnabled(false);
        editBtn.addClickListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.debug("Edit User...");
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<UserProfile> curObj = new BeanItem<>(((EntityItem<UserProfile>) table.getItem(curObjId)).getEntity());

                final UserEditForm editWin = new UserEditForm("Редактирование данных пользователя", curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.refreshItem(curObjId);
                            Notification.show("Пользователь сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        commandBar.addComponent(editBtn);

        panel.addComponent(commandBar);

        // Создаем таблицу скроллинга
        table = new Table();
        table.setContainerDataSource(container);
        table.setSizeFull();

        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.setImmediate(true);
        table.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final boolean enableBtb = event.getProperty().getValue() != null;
                editBtn.setEnabled(enableBtb);
            }
        });
// if (table.size() > 0)
// table.select(table.firstItemId());

        final UsersDataDecl ds = new UsersDataDecl();
        ds.initTableColumns(table);

        panel.addComponent(table);
        setCompositionRoot(panel);
    }

}
