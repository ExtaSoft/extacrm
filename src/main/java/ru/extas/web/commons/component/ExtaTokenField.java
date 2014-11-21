package ru.extas.web.commons.component;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import org.vaadin.tokenfield.TokenField;

/**
 * @author Valery Orlov
 *         Date: 20.11.2014
 *         Time: 19:53
 */
public class ExtaTokenField extends TokenField {

    public ExtaTokenField(final String caption, final InsertPosition insertPosition) {
        super(caption, insertPosition);
    }

    public ExtaTokenField(final String caption) {
        super(caption);
    }

    public ExtaTokenField() {
    }

    public ExtaTokenField(final String caption, final Layout lo) {
        super(caption, lo);
    }

    public ExtaTokenField(final String caption, final Layout lo, final InsertPosition insertPosition) {
        super(caption, lo, insertPosition);
    }

    public ExtaTokenField(final Layout lo, final InsertPosition insertPosition) {
        super(lo, insertPosition);
    }

    public ExtaTokenField(final Layout lo) {
        super(lo);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);

        for (final Button b : buttons.values()) {
            b.setEnabled(!readOnly);
        }
    }
}
