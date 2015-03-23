/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.util.ReflectTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.common.IdentifiedObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.UiUtils.showValidationError;

/**
 * <p>Abstract AbstractEditForm class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public abstract class ExtaEditForm<TEntity> extends VerticalLayout {

    private final static Logger logger = LoggerFactory.getLogger(ExtaEditForm.class);

    private static final long serialVersionUID = -5592353839008000742L;
    private final String caption;
    protected boolean saved = false;
    private HorizontalLayout buttonsPanel;
    private BeanFieldGroup<TEntity> fieldGroup;
    private boolean modified;
    private TEntity entity;
    private Button okBtn;

    private float winWidth = 600;
    private float winHeight = SIZE_UNDEFINED;
    private Unit winWidthUnit = Unit.PIXELS;
    private Unit winHeightUnit = Unit.PIXELS;
    private final List<Component> toFullSize = newArrayList();

    public float getWinWidth() {
        return winWidth;
    }

    public void setWinWidth(final float winWidth, final Unit unit) {
        this.winWidth = winWidth;
        this.winWidthUnit = unit;
    }

    public float getWinHeight() {
        return winHeight;
    }

    public void setWinHeight(final float winHeight, final Unit unit) {
        this.winHeight = winHeight;
        this.winHeightUnit = unit;
    }

    public Unit getWinWidthUnit() {
        return winWidthUnit;
    }

    public Unit getWinHeightUnit() {
        return winHeightUnit;
    }

    public Object getEntityId() {
        if (entity != null) {
            if (entity instanceof IdentifiedObject)
                return ((IdentifiedObject) entity).getId();
            else
                return entity;
        }
        return null;
    }

    public TEntity getEntity() {
        return entity;
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
        HasComponents parent = getParent();
        if (parent instanceof Window)
            ((Window) parent).close();
        else
            fireCloseForm();
    }

    public void fireCloseForm() {
        fireEvent(new CloseFormEvent(this));
    }

    /**
     * <p>Constructor for AbstractEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    protected ExtaEditForm(final String caption, final TEntity entity) {
        this.caption = caption;
        this.entity = entity;
        addAttachListener(e -> initForm());
    }

    public String getCaption() {
        return caption;
    }

    /**
     * <p>initForm.</p>
     */
    private void initForm() {
        checkNotNull(entity);
        initEntity(entity);
        final ComponentContainer form = createEditFields();

        // Now create a binder
        fieldGroup = new BeanFieldGroup<>((Class<TEntity>) entity.getClass());
        final BeanItem<TEntity> beanItem = createBeanItem(entity);
        fieldGroup.setItemDataSource(beanItem);
        fieldGroup.setBuffered(true);
        fieldGroup.bindMemberFields(this);

        final Button cancelBtn = new Button("Отмена", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                // TODO Проверять изменения и выдавать предупреждения
                fieldGroup.discard();
                closeForm();
            }
        });
        cancelBtn.setIcon(Fontello.CANCEL);
        cancelBtn.setDisableOnClick(true);
        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        okBtn = new Button("Сохранить", event -> {
            try {
                if (save())
                    closeForm();
            } finally {
                okBtn.setEnabled(true);
            }
        });
        okBtn.addStyleName(ExtaTheme.BUTTON_PRIMARY);
        okBtn.setIcon(Fontello.OK);
        okBtn.setDisableOnClick(true);
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

        fieldGroup.setReadOnly(isReadOnly());
        okBtn.setEnabled(!isReadOnly());
    }

    protected BeanItem<TEntity> createBeanItem(final TEntity bean) {
        return new BeanItem<>(bean);
    }

    protected boolean save() {
        boolean success = true;
        if (fieldGroup.isValid()) {
            if (isModified()) {
                try {
                    fieldGroup.commit();
                    entity = saveEntity(entity);
                    final BeanItem<TEntity> beanItem = createBeanItem(entity);
                    fieldGroup.setItemDataSource(beanItem);
                    saved = true;
                    modified = false;
                } catch (final Throwable e) {
                    logger.error("Can't apply form changes", e);
                    NotificationUtil.showError("Невозможно сохранить изменения!", e.getLocalizedMessage());
                    success = false;
                }
            }
        } else {
            success = false;
            showValidationError("Невозможно сохранить изменения!", fieldGroup);
        }
        return success;
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

            if (content instanceof TabSheet) {
                content.addStyleName(ExtaTheme.TABSHEET_PADDED_TABBAR);
            } else {
                final Panel panel = new Panel();
                panel.addStyleName(ExtaTheme.PANEL_BORDERLESS);
                panel.addStyleName(ExtaTheme.PANEL_SCROLL_DIVIDER);
                final VerticalLayout panelLayout = new VerticalLayout();
                panelLayout.addComponent(content);
                panelLayout.setMargin(true);
                panel.setContent(panelLayout);
                content = panel;
            }
            addComponent(content);
            addComponent(buttonsPanel);
            setExpandRatio(content, 1);
            toFullSize.add(content);
            toFullSize.add(this);

        }
    }

    public void adjustSize() {
        toFullSize.forEach(c -> c.setSizeFull());
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
     * <p>initEntity.</p>
     *
     * @param entity a TEditObject object.
     */
    protected abstract void initEntity(TEntity entity);

    /**
     * <p>saveEntity.</p>
     *
     * @param entity a TEditObject object.
     */
    protected abstract TEntity saveEntity(TEntity entity);

    /**
     * <p>createEditFields.</p>
     *
     * @return a {@link com.vaadin.ui.ComponentContainer} object.
     */
    protected abstract ComponentContainer createEditFields();

}
