package ru.extas.web.sale;

import com.vaadin.data.Buffered;
import com.vaadin.data.Validator;

/**
 * @author Valery Orlov
 *         Date: 08.12.2014
 *         Time: 11:12
 */
public interface ProductItemComponent {

    void commit() throws Buffered.SourceException, Validator.InvalidValueException;

    boolean isModified();

    void validate();
}
