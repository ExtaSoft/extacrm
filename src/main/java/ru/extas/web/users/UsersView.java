/**
 *
 */
package ru.extas.web.users;

import com.vaadin.ui.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.component.AbstractSubdomainInfo;
import ru.extas.web.commons.component.SubdomainInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Реализует экран управления пользователями и правами доступа
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class UsersView extends SubdomainView {

    private static final long serialVersionUID = -1272779672761523416L;

    /**
     * <p>Constructor for UsersView.</p>
     */
    public UsersView() {
        super("Пользователи");
    }

    /** {@inheritDoc} */
    @Override
    protected List<SubdomainInfo> getSubdomainInfo() {
        final ArrayList<SubdomainInfo> ret = newArrayList();
        ret.add(new AbstractSubdomainInfo("Пользователи", ExtaDomain.USERS) {
            @Override
            public ExtaGrid createGrid() {
                return new UsersGrid();
            }
        });
        ret.add(new AbstractSubdomainInfo("Группы", ExtaDomain.USER_GROUPS) {
            @Override
            public ExtaGrid createGrid() {
                return new UserGroupGrid();
            }
        });
        return ret;
    }

}
