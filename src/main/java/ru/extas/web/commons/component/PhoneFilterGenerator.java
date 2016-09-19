package ru.extas.web.commons.component;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import ru.extas.web.commons.AbstractFilterGenerator;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PhoneFilterGenerator extends AbstractFilterGenerator {

        private final String phonePropId;

        public PhoneFilterGenerator(final String phonePropId) {
            this.phonePropId = phonePropId;
        }

        @Override
        public AbstractField<?> getCustomFilterComponent(final Object propertyId) {
            if(propertyId.equals(phonePropId))
                return new PhoneField("");
            return null;
        }

        @Override
        public Container.Filter generateFilter(final Object propertyId, final Field<?> originatingField) {
            if (originatingField instanceof PhoneField) {
                final String convertedValue = ((PhoneField) originatingField).getConvertedValue();
                return isNullOrEmpty(convertedValue) ? null : new Like(propertyId, convertedValue, false);
            }
            return null;
        }
    }
