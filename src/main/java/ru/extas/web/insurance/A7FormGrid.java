/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Container;
import ru.extas.model.insurance.A7Form;
import ru.extas.security.A7FormSecurityFilter;
import ru.extas.server.insurance.A7FormRepository;
import ru.extas.web.commons.*;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>A7FormGrid class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class A7FormGrid extends ExtaGrid<A7Form> {

    private static final long serialVersionUID = 6290106109723378415L;

    /**
     * <p>Constructor for A7FormGrid.</p>
     */
    public A7FormGrid() {
        super(A7Form.class);
    }

    @Override
    public ExtaEditForm<A7Form> createEditForm(final A7Form a7Form, final boolean isInsert) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new A7FormDataDecl();
    }

    private class A7SecuredContainer extends SecuredDataContainer<A7Form> {

        public A7SecuredContainer() {
            super(new A7FormSecurityFilter());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        final ExtaJpaContainer<A7Form> cnt = new A7SecuredContainer();
        cnt.addNestedContainerProperty("owner.name");
        return cnt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new ItemAction("Утрачен", "Перевести выделенный в списке бланк в \"Утраченные\"", null) {
            @Override
            public void fire(final Set itemIds) {
                final A7Form.Status status = A7Form.Status.LOST;
                changeStatus(itemIds, status);
            }
        });

        actions.add(new ItemAction("Испорчен", "Перевести выделенный в списке бланк в \"Испорченные\"", null) {
            @Override
            public void fire(final Set itemIds) {
                final A7Form.Status status = A7Form.Status.BROKEN;
                changeStatus(itemIds, status);
            }
        });

        return actions;
    }

    private void changeStatus(final Set itemIds, final A7Form.Status status) {
        final Set<A7Form> a7Forms = getEntities(itemIds);

        final A7FormRepository formService = lookup(A7FormRepository.class);
        formService.changeStatus(a7Forms, status);
        refreshContainerItems(itemIds);
    }
}
