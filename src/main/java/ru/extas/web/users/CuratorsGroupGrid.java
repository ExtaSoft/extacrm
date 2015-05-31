package ru.extas.web.users;

import com.vaadin.data.Container;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaDbContainer;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Грид управляет группами пользователей
 *
 * @author Valery Orlov
 *         Date: 20.06.2014
 *         Time: 10:48
 * @version $Id: $Id
 * @since 0.5.0
 */
public class CuratorsGroupGrid extends ExtaGrid<CuratorsGroup> {
    public CuratorsGroupGrid() {
        super(CuratorsGroup.class);
    }

    @Override
    public ExtaEditForm<CuratorsGroup> createEditForm(final CuratorsGroup userGroup, final boolean isInsert) {
        return new CuratorsGroupEditForm(userGroup);
    }

    /** {@inheritDoc} */
    @Override
    protected GridDataDecl createDataDecl() {
        return new CuratorsGroupDataDecl();
    }

    /** {@inheritDoc} */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDbContainer<CuratorsGroup> container = new ExtaDbContainer<>(CuratorsGroup.class);
        return container;
    }

    /** {@inheritDoc} */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новая", "Ввод новой группы кураторов в систему", Fontello.USER_ADD));
        actions.add(new EditObjectAction("Изменить", "Редактирование группы", Fontello.USER_1));

        return actions;
    }
}
