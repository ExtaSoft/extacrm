package ru.extas.web.commons;

import com.vaadin.data.Container;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import org.tepi.filtertable.FilterGenerator;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Композитный фильтр
 * <p>
 * Created by valery on 27.04.16.
 */
public class CompositeFilterGenerator extends AbstractFilterGenerator {

    private final List<FilterGenerator> generators = newArrayList();

    public CompositeFilterGenerator(FilterGenerator... generators) {
        this.generators.addAll(Arrays.asList(generators));
    }

    public CompositeFilterGenerator() {
    }

    /**
     * Generates a new Filter for the property with the given ID, using the
     * Field object and its value as basis for the filtering.
     *
     * @param propertyId       ID of the filtered property.
     * @param originatingField Reference to the field that triggered this filter generating
     *                         request.
     * @return A generated Filter object, or NULL if you want to allow
     * FilterTable to generate the default Filter for this property.
     */
    @Override
    public Container.Filter generateFilter(Object propertyId, Field<?> originatingField) {
        Container.Filter filter = null;
        for (FilterGenerator generator : generators) {
            filter = generator.generateFilter(propertyId, originatingField);
            if (filter != null)
                return filter;
        }
        return filter;
    }

    @Override
    public Container.Filter generateFilter(Object propertyId, Object value) {
        Container.Filter filter = null;
        for (FilterGenerator generator : generators) {
            filter = generator.generateFilter(propertyId, value);
            if (filter != null)
                return filter;
        }
        return filter;
    }

    /**
     * Allows you to provide a custom filtering field for the properties as
     * needed.
     *
     * @param propertyId ID of the property for for which the field is asked for
     * @return a custom filtering field OR null if you want to use the generated
     * default field.
     */
    @Override
    public AbstractField<?> getCustomFilterComponent(Object propertyId) {
        AbstractField<?> field = null;
        for (FilterGenerator generator : generators) {
            field = generator.getCustomFilterComponent(propertyId);
            if(field != null)
                return field;
        }
        return field;
    }

    public void add(FilterGenerator generator) {
        generators.add(generator);
    }

    public CompositeFilterGenerator with(FilterGenerator generator) {
        generators.add(generator);
        return this;
    }

    public void remove(FilterGenerator generator) {
        generators.remove(generator);
    }

}
