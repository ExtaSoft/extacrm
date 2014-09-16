/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.util.ReflectTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.common.IdentifiedObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;

import static ru.extas.web.UiUtils.showValidationError;

/**
 * <p>Abstract AbstractEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ExtaEditForm<TEditObject> extends CustomComponent {

    private final static Logger logger = LoggerFactory.getLogger(ExtaEditForm.class);

    private static final long serialVersionUID = -5592353839008000742L;
    private final String caption;
    protected boolean saved = false;
    private HorizontalLayout buttonsPanel;
    private FieldGroup fieldGroup;
    private boolean modified;
    private TEditObject bean;

    public Object getObjectId() {
        if (bean != null) {
            if (bean instanceof IdentifiedObject)
                return ((IdentifiedObject) bean).getId();
            else
                return bean;
        }
        return null;
    }

    public TEditObject getObject() {
        return bean;
    }

    public static class CloseFormEvent extends Component.Event {

        /**
         * Constructs a new event with the specified source component.
         *
         * @param source the source component of the event
         */
        public CloseFormEvent(final Component source) {
            super(source);
        }
    }

    public interface CloseFormListener extends Serializable {

        public static final Method CLOSE_FORM_METHOD = ReflectTools
                .findMethod(CloseFormListener.class, "closeForm", CloseFormEvent.class);

        public void closeForm(CloseFormEvent event);

    }

    public void addCloseFormListener(final CloseFormListener listener) {
        addListener(CloseFormEvent.class, listener,
                CloseFormListener.CLOSE_FORM_METHOD);
    }

    public void removeCloseFormListener(final CloseFormListener listener) {
        removeListener(CloseFormEvent.class, listener,
                CloseFormListener.CLOSE_FORM_METHOD);
    }

    public void closeForm() {
        fireCloseForm();
    }

    protected void fireCloseForm() {
        fireEvent(new CloseFormEvent(this));
    }

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    protected ExtaEditForm(final String caption) {
        this.caption = caption;
    }

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *
     * @param caption  a {@link java.lang.String} object.
     * @param beanItem a {@link com.vaadin.data.util.BeanItem} object.
     */
    protected ExtaEditForm(final String caption, final BeanItem<TEditObject> beanItem) {
        this(caption);

        initForm(beanItem);
    }

    public String getCaption() {
        return caption;
    }

    /**
     * <p>initForm.</p>
     *
     * @param beanItem a {@link com.vaadin.data.util.BeanItem} object.
     */
    protected void initForm(final BeanItem<TEditObject> beanItem) {
        bean = beanItem.getBean();
        initObject(bean);
        final ComponentContainer form = createEditFields(bean);

        // Now create a binder
        fieldGroup = new FieldGroup(beanItem);
        fieldGroup.setBuffered(true);
        fieldGroup.bindMemberFields(this);

        Button cancelBtn = new Button("Отмена", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                // TODO Проверять изменения и выдавать предупреждения
                fieldGroup.discard();
                closeForm();
            }
        });
        cancelBtn.setIcon(Fontello.CANCEL);
        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        Button okBtn = new Button("OK", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {

                if (isModified()) {
                    if (fieldGroup.isValid()) {
                        try {
                            fieldGroup.commit();
                            bean = saveObject(bean);
                            saved = true;
                            modified = false;
                        } catch (final Throwable e) {
                            logger.error("Can't apply form changes", e);
                            NotificationUtil.showError("Невозможно сохранить изменения!", e.getLocalizedMessage());
                            return;
                        }
                        closeForm();
                    } else
                        showValidationError("Невозможно сохранить изменения!", fieldGroup);
                }
            }

        });
        okBtn.addStyleName(ExtaTheme.BUTTON_PRIMARY);
        okBtn.setIcon(Fontello.OK);
        okBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER, ShortcutAction.ModifierKey.CTRL);

        final Label footerText = new Label("");
        footerText.setSizeUndefined();

        buttonsPanel = new HorizontalLayout(footerText, okBtn, cancelBtn);
        buttonsPanel.setExpandRatio(footerText, 1);
        buttonsPanel.addStyleName(ExtaTheme.WINDOW_BOTTOM_TOOLBAR);
        buttonsPanel.setWidth(100, Unit.PERCENTAGE);
        buttonsPanel.setSpacing(true);

        setDefaultFocus(form);
        setContent(form);

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
    public void setModified(final boolean modified) {
        this.modified = modified;
    }

    private boolean setDefaultFocus(final HasComponents container) {
        boolean focused = false;
        final Iterator<Component> childs = container.iterator();
        while (childs.hasNext() && !focused) {
            final Component comp = childs.next();
            if (comp instanceof Component.Focusable && comp.isEnabled() && comp.isVisible()) {
                ((Component.Focusable) comp).focus();
                focused = true;
            } else if (comp instanceof HasComponents) {
                focused = setDefaultFocus((HasComponents) comp);
            }
        }
        return focused;
    }

    public void setContent(Component content) {
        if (content != null) {
            final VerticalLayout contentContainer = new VerticalLayout(content, this.buttonsPanel);
            contentContainer.setSizeUndefined();
            contentContainer.setMargin(new MarginInfo(false, true, false, true));
            contentContainer.setSpacing(true);
            content = contentContainer;
        }
        super.setCompositionRoot(content);
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
     * <p>Getter for the field <code>fieldGroup</code>.</p>
     *
     * @return a {@link com.vaadin.data.fieldgroup.FieldGroup} object.
     */
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
    protected abstract TEditObject saveObject(TEditObject obj);

    /**
     * <p>createEditFields.</p>
     *
     * @param obj a TEditObject object.
     * @return a {@link com.vaadin.ui.ComponentContainer} object.
     */
    protected abstract ComponentContainer createEditFields(TEditObject obj);

}
