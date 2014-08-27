/**
 * DISCLAIMER
 *
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 *
 * @author jouni@vaadin.com
 *
 */

package ru.extas.web.insurance;

import com.vaadin.ui.Component;
import ru.extas.model.security.ExtaDomain;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.ExtaGrid;
import ru.extas.web.commons.SubdomainView;
import ru.extas.web.commons.component.AbstractSubdomainInfo;
import ru.extas.web.commons.component.SubdomainInfo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Раздел страхование
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class InsuranceView extends SubdomainView {

    private static final long serialVersionUID = -2524035728558575428L;

    /**
     * <p>Constructor for InsuranceView.</p>
     */
    public InsuranceView() {
        super("Страхование техники");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SubdomainInfo> getSubdomainInfo() {
        UserManagementService userService = lookup(UserManagementService.class);
        final ArrayList<SubdomainInfo> ret = newArrayList();
        ret.add(new AbstractSubdomainInfo("Имущ. страховки", ExtaDomain.INSURANCE_PROP) {

            @Override
            public ExtaGrid createGrid() {
                return new InsuranceGrid();
            }
        });
        ret.add(new AbstractSubdomainInfo("Бланки (БСО)", ExtaDomain.INSURANCE_BSO) {

            @Override
            public ExtaGrid createGrid() {
                return new PolicyGrid();
            }
        });
        ret.add(new AbstractSubdomainInfo("Акты Приема/Передачи", ExtaDomain.INSURANCE_TRANSFER) {

            @Override
            public ExtaGrid createGrid() {
                return new FormTransferGrid();
            }
        });
        ret.add(new AbstractSubdomainInfo("Квитанции А-7", ExtaDomain.INSURANCE_A_7) {

            @Override
            public ExtaGrid createGrid() {
                return new A7FormGrid();
            }
        });
        return ret;
    }

}
