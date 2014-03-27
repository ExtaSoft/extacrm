/**
 *
 */
package ru.extas.web.commons.window;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * <p>Abstract AbstractEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class AbstractEditForm<TEditObject> extends Window {

    private final static Logger logger = LoggerFactory.getLogger(AbstractEditForm.class);

    private static final long serialVersionUID = -5592353839008000742L;
    protected boolean saved = false;
    private final HorizontalLayout buttonsPanel = new HorizontalLayout();
    private Button cancelBtn;
    private Button okBtn;
    private FieldGroup fieldGroup;
    private boolean modified;

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    protected AbstractEditForm(final String caption) {
        super(caption);
    }

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param beanItem a {@link com.vaadin.data.util.BeanItem} object.
     */
    protected AbstractEditForm(final String caption, final BeanItem<TEditObject> beanItem) {
        super(caption);

        initForm(beanItem);
    }

    /**
     * <p>initForm.</p>
     *
     * @param beanItem a {@link com.vaadin.data.util.BeanItem} object.
     */
    protected void initForm(BeanItem<TEditObject> beanItem) {
        final TEditObject bean = beanItem.getBean();
        initObject(bean);
        final ComponentContainer form = createEditFields(bean);

        // Now create a binder
        fieldGroup = new FieldGroup(beanItem);
        fieldGroup.setBuffered(true);
        fieldGroup.bindMemberFields(this);

        cancelBtn = new Button("Отмена", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                // TODO Проверять изменения и выдавать предупреждения
                fieldGroup.discard();
                close();
            }
        });
        cancelBtn.setStyleName("icon-cancel");
        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        okBtn = new Button("OK", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {

                if (isModified()) {
                    if (fieldGroup.isValid()) {
                        try {
                            fieldGroup.commit();
                            checkBeforeSave(bean);
                            saveObject(bean);
                            saved = true;
                            modified = false;
                        } catch (final CommitException e) {
                            // TODO Correct error handling
                            logger.error("Can't apply form changes", e);
                            Notification.show("Невозможно сохранить изменения!", Type.ERROR_MESSAGE);
                            return;
                        }
                        close();
                    } else
                        Notification.show("Невозможно сохранить изменения! Неверные данные в форме", Type.WARNING_MESSAGE);
                }
            }

        });

        okBtn.setStyleName("icon-ok");
        okBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER, ShortcutAction.ModifierKey.CTRL);
        this.buttonsPanel.addComponent(okBtn);
        this.buttonsPanel.setComponentAlignment(okBtn, Alignment.MIDDLE_RIGHT);
        this.buttonsPanel.addComponent(cancelBtn);
        this.buttonsPanel.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
        this.buttonsPanel.setSpacing(true);

        setDefaultFocus(form);
        setContent(form);

        this.addCloseListener(new CloseListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(final CloseEvent e) {

                // TODO Обработать закрытие формы по кресту

            }
        });
    }

    /**
     * <p>isModified.</p>
     *
     * @return a boolean.
     */
    public boolean isModified() {
        return modified || fieldGroup.isModified();
    }

    /**
     * <p>Setter for the field <code>modified</code>.</p>
     *
     * @param modified a boolean.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    private boolean setDefaultFocus(HasComponents container) {
        boolean focused = false;
        Iterator<Component> childs = container.iterator();
        while (childs.hasNext() && !focused) {
            Component comp = childs.next();
            if (comp instanceof Component.Focusable && comp.isEnabled() && comp.isVisible()) {
                ((Component.Focusable) comp).focus();
                focused = true;
            } else if (comp instanceof HasComponents) {
                focused = setDefaultFocus((HasComponents) comp);
            }
        }
        return focused;
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
     * <p>isSaved.</p>
     *
     * @return a boolean.
     */
    public boolean isSaved() {
        return this.saved;
    }

    /**
     * <p>showModal.</p>
     */
    public void showModal() {
        setClosable(true);
        setModal(true);

        UI.getCurrent().addWindow(this);
    }

    protected FieldGroup getFieldGroup() {
        return fieldGroup;
    }

    /**
     * <p>initObject.</p>
     *
     * @param obj a TEditObject object.
     */
    protected abstract void initObject(TEditObject obj);

    /**
     * <p>saveObject.</p>
     *
     * @param obj a TEditObject object.
     */
    protected abstract void saveObject(TEditObject obj);

    /**
     * <p>checkBeforeSave.</p>
     *
     * @param obj a TEditObject object.
     */
    protected abstract void checkBeforeSave(TEditObject obj);

    /**
     * <p>createEditFields.</p>
     *
     * @param obj a TEditObject object.
     * @return a {@link com.vaadin.ui.ComponentContainer} object.
     */
    protected abstract ComponentContainer createEditFields(TEditObject obj);

}
