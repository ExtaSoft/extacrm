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

    public ExtaTokenField(String caption, InsertPosition insertPosition) {
        super(caption, insertPosition);
    }

    public ExtaTokenField(String caption) {
        super(caption);
    }

    public ExtaTokenField() {
    }

    public ExtaTokenField(String caption, Layout lo) {
        super(caption, lo);
    }

    public ExtaTokenField(String caption, Layout lo, InsertPosition insertPosition) {
        super(caption, lo, insertPosition);
    }

    public ExtaTokenField(Layout lo, InsertPosition insertPosition) {
        super(lo, insertPosition);
    }

    public ExtaTokenField(Layout lo) {
        super(lo);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        for (Button b : buttons.values()) {
            b.setEnabled(!readOnly);
        }
    }
}
