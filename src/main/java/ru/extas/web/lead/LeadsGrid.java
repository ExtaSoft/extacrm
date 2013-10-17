package ru.extas.web.lead;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import ru.extas.model.Lead;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 */
public class LeadsGrid extends ExtaGrid {
    private static final long serialVersionUID = 4876073256421755574L;

    public LeadsGrid() {
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new LeadDataDecl();
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final JPAContainer<Lead> container = new ExtaDataContainer<>(Lead.class);
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new UIAction("Новый", "Ввод нового лида", "icon-doc-new") {
            @Override
            public void fire(Object itemId) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке лид", "icon-edit-3") {
            @Override
            public void fire(Object itemId) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        return actions;
    }

}
