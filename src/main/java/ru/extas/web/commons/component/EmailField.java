package ru.extas.web.commons.component;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;

import java.text.MessageFormat;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Поле ввода адреса электронной почты
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 13:15
 * @version $Id: $Id
 * @since 0.3
 */
public class EmailField extends ExtaCustomField<String> {

    private Link link;
    private EditField linkEdit;
    private Button editBtn;

    /**
     * <p>Constructor for EmailField.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public EmailField(final String caption, final String description) {
        super(caption, description);
    }

    /**
     * <p>Constructor for EmailField.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public EmailField(final String caption) {
        this(caption, "Введите имя e-mail контакта который будет использоваться для связи");
    }

    @Override
    protected Component initContent() {
        link = new Link();
        link.setTargetName("_blank");

        linkEdit = new EditField();
        linkEdit.addStyleName(ExtaTheme.TEXTFIELD_BORDERLESS);
        linkEdit.setWidth(100, Unit.PERCENTAGE);
        linkEdit.setNullRepresentation("");
        linkEdit.setInputPrompt("e-mail");
        linkEdit.setPropertyDataSource(getPropertyDataSource());
        linkEdit.addValueChangeListener(e -> {
            setValue((String) e.getProperty().getValue());
        });
        linkEdit.addBlurListener(e -> {
            if (!linkEdit.isModified())
                refreshFieldState(getValue());
        });

        editBtn = new Button("Изменить", Fontello.EDIT_1);
        editBtn.setDescription("Нажмите чтобы изменить адрес электронной почты");
        editBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        editBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        editBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        editBtn.addClickListener(l -> {
            link.setVisible(false);
            editBtn.setVisible(false);
            linkEdit.setVisible(true);
            linkEdit.focus();
        });
        addReadOnlyStatusChangeListener(e -> editBtn.setVisible(!isReadOnly()));

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
            link.setResource(new ExternalResource(MessageFormat.format("mailto:{0}", newFieldValue)));
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
