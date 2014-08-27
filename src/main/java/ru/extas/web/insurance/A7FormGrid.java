/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Container;
import ru.extas.model.insurance.A7Form;
import ru.extas.server.insurance.A7FormRepository;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridItem.extractBean;

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
    public AbstractEditForm<A7Form> createEditForm(A7Form a7Form) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected GridDataDecl createDataDecl() {
        return new A7FormDataDecl();
    }

    /** {@inheritDoc} */
    @Override
    protected Container createContainer() {
        ExtaDataContainer<A7Form> cnt = new ExtaDataContainer<>(A7Form.class);
        cnt.addNestedContainerProperty("owner.name");
        return cnt;
    }

    /** {@inheritDoc} */
    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new ItemAction("Утрачен", "Перевести выделенный в списке бланк в \"Утраченные\"", null) {
            @Override
            public void fire(Object itemId) {
                final A7Form.Status status = A7Form.Status.LOST;
                changeStatus(itemId, status);
            }
        });

        actions.add(new ItemAction("Испорчен", "Перевести выделенный в списке бланк в \"Испорченные\"", null) {
            @Override
            public void fire(Object itemId) {
                final A7Form.Status status = A7Form.Status.BROKEN;
                changeStatus(itemId, status);
            }
        });

        return actions;
    }

    private void changeStatus(Object itemId, A7Form.Status status) {
        final A7Form curObj = extractBean(table.getItem(itemId));

        A7FormRepository formService = lookup(A7FormRepository.class);
        formService.changeStatus(curObj, status);
        refreshContainerItem(itemId);
    }
}
