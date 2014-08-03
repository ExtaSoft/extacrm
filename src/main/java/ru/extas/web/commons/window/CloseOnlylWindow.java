package ru.extas.web.commons.window;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import ru.extas.web.commons.Fontello;

/**
 * Стандартное окошко OK/Отмена
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class CloseOnlylWindow extends Window {

    private static final long serialVersionUID = -1869372339151029572L;
    private boolean okPressed = false;
    private final HorizontalLayout buttonsPanel = new HorizontalLayout();
    protected Button closeBtn;

    /**
     * <p>Constructor for CloseOnlylWindow.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param content a {@link com.vaadin.ui.Component} object.
     */
    public CloseOnlylWindow(final String caption, final Component content) {
        super(caption);
        initInputWindow();
        setContent(content);
    }

    /**
     * <p>Constructor for CloseOnlylWindow.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public CloseOnlylWindow(final String caption) {
        super(caption);
        initInputWindow();
    }

    private void initInputWindow() {
        closeBtn = new Button("Закрыть", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                CloseOnlylWindow.this.okPressed = true;
                close();
            }
        });
        closeBtn.setIcon(Fontello.OK);
        this.buttonsPanel.addComponent(closeBtn);
        this.buttonsPanel.setComponentAlignment(closeBtn, Alignment.MIDDLE_RIGHT);
        this.buttonsPanel.setSpacing(true);
    }

    /** {@inheritDoc} */
    @Override
    public void setContent(Component content) {
        if (content != null) {
            final VerticalLayout contentContainer = new VerticalLayout(content, this.buttonsPanel);
            contentContainer.setMargin(true);
            contentContainer.setSpacing(true);
            contentContainer.setComponentAlignment(this.buttonsPanel, Alignment.MIDDLE_RIGHT);
            content = contentContainer;
        }
        super.setContent(content);
    }

    /**
     * <p>showModal.</p>
     */
    public void showModal() {
        setClosable(true);
        setModal(true);

        UI.getCurrent().addWindow(this);
    }

    /**
     * <p>Setter for the field <code>okPressed</code>.</p>
     *
     * @param okPressed a boolean.
     */
    protected void setOkPressed(final boolean okPressed) {
        this.okPressed = okPressed;
    }

    /**
     * <p>isOkPressed.</p>
     *
     * @return a boolean.
     */
    public boolean isOkPressed() {
        return this.okPressed;
    }

}
