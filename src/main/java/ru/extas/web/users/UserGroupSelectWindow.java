package ru.extas.web.users;

import ru.extas.model.security.UserGroup;
import ru.extas.web.commons.DefaultAction;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.UIAction;
import ru.extas.web.commons.window.CloseOnlylWindow;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Окно выбора группы пользователей
 *
 * @author Valery Orlov
 *         Date: 27.06.2014
 *         Time: 12:35
 * @version $Id: $Id
 * @since 0.5.0
 */
public class UserGroupSelectWindow extends CloseOnlylWindow {

    private UserGroup selected;
    private boolean selectPressed;

    /**
     * <p>Constructor for UserGroupSelectWindow.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public UserGroupSelectWindow(String caption) {
        super(caption);
        addStyleName("base-view");
        setContent(new UserGroupGrid() {

//            @Override
//            protected Container createContainer() {
//                final ExtaDataContainer<UserGroup> container = new ExtaDataContainer<>(UserGroup.class);
//                return container;
//            }

            @Override
            protected List<UIAction> createActions() {
                List<UIAction> actions = newArrayList();

                actions.add(new DefaultAction("Выбрать", "Выбрать выделенную в списке группу и закрыть окно", Fontello.CHECK) {
                    @Override
                    public void fire(final Object itemId) {

                        selected = extractBean(table.getItem(itemId));
                        selectPressed = true;
                        close();
                    }
                });

//                actions.addAll(super.createActions());

                return actions;
            }

        });
    }

    /**
     * <p>Getter for the field <code>selected</code>.</p>
     *
     * @return a {@link ru.extas.model.security.UserGroup} object.
     */
    public UserGroup getSelected() {
        return selected;
    }

    /**
     * <p>Setter for the field <code>selected</code>.</p>
     *
     * @param selected a {@link ru.extas.model.security.UserGroup} object.
     */
    public void setSelected(UserGroup selected) {
        this.selected = selected;
    }

    /**
     * <p>isSelectPressed.</p>
     *
     * @return a boolean.
     */
    public boolean isSelectPressed() {
        return selectPressed;
    }

    /**
     * <p>Setter for the field <code>selectPressed</code>.</p>
     *
     * @param selectPressed a boolean.
     */
    public void setSelectPressed(boolean selectPressed) {
        this.selectPressed = selectPressed;
    }
}
