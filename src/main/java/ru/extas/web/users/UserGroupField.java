package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.security.UserGroup;
import ru.extas.web.commons.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Список принадлежности пользователя к группе
 *
 * @author Valery Orlov
 *         Date: 24.06.2014
 *         Time: 23:30
 * @version $Id: $Id
 * @since 0.5.0
 */
public class UserGroupField extends CustomField<Set> {

    private BeanItemContainer<UserGroup> itemContainer;

    /**
     * <p>Constructor for UserGroupField.</p>
     */
    public UserGroupField() {
        setBuffered(true);
        addStyleName(ExtaTheme.BASE_VIEW);
        setSizeFull();
        setWidth(600, Unit.PIXELS);
    }

    /** {@inheritDoc} */
    @Override
    protected Component initContent() {
        final UserGroupGrid grid = new UserGroupGrid(){
            @Override
            protected Container createContainer() {
                final Set<UserGroup> list = getValue() != null ? getValue() : newHashSet();
                itemContainer = new BeanItemContainer<>(UserGroup.class);
                if (list != null) {
                    for (final UserGroup item : list) {
                        itemContainer.addBean(item);
                    }
                }
                return itemContainer;
            }

            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new UIAction("Добавить", "Добавить пользователя в группу", Fontello.USER_ADD) {
                    @Override
                    public void fire(final Set itemIds) {
                        final UserGroupSelectWindow selectWindow = new UserGroupSelectWindow("Выберите группу");
                        selectWindow.addCloseListener(e -> {
                            if (selectWindow.isSelectPressed()) {
                                itemContainer.addAll(selectWindow.getSelected());
                                setValue(newHashSet(itemContainer.getItemIds()));
                                NotificationUtil.showSuccess("Группа добавлена");
                            }
                        });
                        selectWindow.showModal();
                    }
                });

                actions.add(new ItemAction("Удалить", "Удалить принадлежность к группе", Fontello.TRASH) {
                    @Override
                    public void fire(final Set itemIds) {
                        itemIds.forEach(id -> container.removeItem(id));
                        setValue(newHashSet(((BeanItemContainer<UserGroup>) container).getItemIds()));
                    }
                });
                return actions;
            }
        };
        return grid;
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }
}
