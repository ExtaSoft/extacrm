package ru.extas.web.commons.container;

import com.alsnightsoft.vaadin.containers.LazyPagedContainer;
import ru.extas.model.common.IdentifiedObject;

/**
 * Created by valery on 08.04.15.
 */
public class JpaLazyPagedContainer<TEntityType extends IdentifiedObject> extends LazyPagedContainer<TEntityType> {

    public JpaLazyPagedContainer(Class<? super TEntityType> type) throws IllegalArgumentException {
        super(type);
    }
}
