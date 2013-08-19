package ru.extas.vaadin.addon.jdocontainer;

import java.io.Serializable;

/**
 * Query factory to be used with JdoQuery.
 *
 * @author Valery Orlov
 */
public final class JdoQueryFactory implements QueryFactory, Serializable {
    /**
     * Java serialization version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public JdoQueryFactory() {
    }

    /**
     * Constructs a new query according to the given QueryDefinition.
     *
     * @param queryDefinition Properties participating in the sorting.
     * @return A new query constructed according to the given sort state.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Query constructQuery(final QueryDefinition queryDefinition) {
        return new JdoQuery((JdoQueryDefinition) queryDefinition);
    }

}
