package ru.extas.web.commons.component;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Поле ввода ссылки
 *
 * @author Valery Orlov
 *         Date: 20.10.2014
 *         Time: 13:40
 */
public class WebSiteLinkField extends CustomField<String> {

    private Link link;
    private EditField linkEdit;
    private Button editBtn;

    public WebSiteLinkField() {
    }

    public WebSiteLinkField(final String caption) {
        this(caption, "Введите ввылку на web страницу");
    }

    public WebSiteLinkField(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
    }

    @Override
    protected Component initContent() {
        link = new Link();
        link.setTargetName("_blank");

        linkEdit = new EditField();
        linkEdit.addStyleName(ExtaTheme.TEXTFIELD_BORDERLESS);
        linkEdit.setWidth(100, Unit.PERCENTAGE);
        linkEdit.setNullRepresentation("");
        linkEdit.setInputPrompt("http://...");
        linkEdit.setPropertyDataSource(getPropertyDataSource());
        linkEdit.addValueChangeListener(e -> setValue((String) e.getProperty().getValue()));
        linkEdit.addBlurListener(e -> {
            if(!linkEdit.isModified())
                refreshFieldState(getValue());
        });

        editBtn = new Button("Изменить", Fontello.EDIT_1);
        editBtn.setDescription("Нажмите чтобы изменить ссылку");
        editBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        editBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        editBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        editBtn.addClickListener(l -> {
            link.setVisible(false);
            editBtn.setVisible(false);
            linkEdit.setVisible(true);
            linkEdit.focus();
        });

        addValueChangeListener(e -> {
            final String newFieldValue = (String) e.getProperty().getValue();
            refreshFieldState(newFieldValue);
        });

        refreshFieldState(getValue());

        final HorizontalLayout line = new HorizontalLayout();
        line.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        line.addComponents(link, linkEdit, editBtn);
        return line;
    }

    private void refreshFieldState(final String newFieldValue) {
        if (isNullOrEmpty(newFieldValue)) {
            link.setVisible(false);
            editBtn.setVisible(false);
            linkEdit.setVisible(!isReadOnly());
        } else {
            link.setVisible(true);
            link.setResource(new ExternalResource(newFieldValue));
            link.setCaption(newFieldValue);
            editBtn.setVisible(!isReadOnly());
            linkEdit.setVisible(false);
        }
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
