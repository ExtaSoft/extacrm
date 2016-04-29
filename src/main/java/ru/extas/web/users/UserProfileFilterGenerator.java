package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import ru.extas.model.security.UserProfile;
import ru.extas.web.commons.AbstractFilterGenerator;

import java.util.Set;

/**
 * Фильтр по пользователю когда в базе лежит логин
 *
 * <p>
 * Created by valery on 27.04.16.
 */
public class UserProfileFilterGenerator extends AbstractFilterGenerator {
    private final String propId;

    public UserProfileFilterGenerator(String propId) {
        this.propId = propId;
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
        if (originatingField instanceof UserProfileSelect) {
            final UserProfile userProfile = (UserProfile) ((UserProfileSelect) originatingField).getConvertedValue();
            if (userProfile != null) {
                final Set<String> aliases = userProfile.getAliases();
                Container.Filter[] filters = new Container.Filter[aliases.size()];
                int i = 0;
                for (String alias : aliases) {
                    filters[i++] = new Compare.Equal(propertyId, alias);
                }
                return filters.length > 1 ? new Or(filters) : filters[0];
            }
        }
        return null;
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
        if (propId.equals(propertyId))
            return new UserProfileSelect("");
        return null;
    }
}
