/**
 *
 */
package ru.extas.web.users;

import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainInfo;
import ru.extas.web.commons.SubdomainInfoImpl;
import ru.extas.web.commons.SubdomainView;

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
        ret.add(new SubdomainInfoImpl("Пользователи", ExtaDomain.USERS) {
            @Override
            public ExtaGrid createGrid() {
                return new UsersGrid();
            }
        });
        ret.add(new SubdomainInfoImpl("Группы пользователей", ExtaDomain.USER_GROUPS) {
            @Override
            public ExtaGrid createGrid() {
                return new UserGroupGrid();
            }
        });
        ret.add(new SubdomainInfoImpl("Группы кураторов", ExtaDomain.CURATORS_GROUPS) {
            @Override
            public ExtaGrid createGrid() {
                return new CuratorsGroupGrid();
            }
        });
        return ret;
    }

}
