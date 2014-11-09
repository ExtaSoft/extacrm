/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.util.ReflectTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.data.collectioncontainer.CollectionContainer;
import ru.extas.model.common.IdentifiedObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;
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
    private BeanFieldGroup<TEditObject> fieldGroup;
    private boolean modified;
    private TEditObject bean;
    private Button okBtn;

    private float winWidth = 600;
    private float winHeight = SIZE_UNDEFINED;
    private Unit winWidthUnit = Unit.PIXELS;
    private Unit winHeightUnit = Unit.PIXELS;

    public float getWinWidth() {
        return winWidth;
    }

    public void setWinWidth(float winWidth, Unit unit) {
        this.winWidth = winWidth;
        this.winWidthUnit = unit;
    }

    public float getWinHeight() {
        return winHeight;
    }

    public void setWinHeight(float winHeight, Unit unit) {
        this.winHeight = winHeight;
        this.winHeightUnit = unit;
    }

    public Unit getWinWidthUnit() {
        return winWidthUnit;
    }

    public Unit getWinHeightUnit() {
        return winHeightUnit;
    }

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
    protected ExtaEditForm(final String caption, final TEditObject bean) {
        this.caption = caption;
        this.bean = bean;
        // Must set a dummy root in constructor
        setCompositionRoot(new Label(""));
    }

    @Override
    public void attach() {
        initForm();
        super.attach();
    }

    public String getCaption() {
        return caption;
    }

//    public BeanItem<TEditObject> getBeanItem() {
//        return beanItem;
//    }

//    public void setBeanItem(BeanItem<TEditObject> beanItem) {
//        checkNotNull(beanItem);
//        this.beanItem = beanItem;
//        this.bean = beanItem.getBean();
//    }

    /**
     * <p>initForm.</p>
     */
    private void initForm() {
        checkNotNull(bean);
        initObject(bean);
        final ComponentContainer form = createEditFields(bean);

        // Now create a binder
        fieldGroup = new BeanFieldGroup<>((Class<TEditObject>) bean.getClass());
        BeanItem<TEditObject> beanItem = createBeanItem(bean);
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

        addAttachListener(e -> {
            fieldGroup.setReadOnly(isReadOnly());
            okBtn.setEnabled(!isReadOnly());
        });
    }

    protected BeanItem<TEditObject> createBeanItem(TEditObject bean) {
        return new BeanItem<>(bean);
    }

    protected boolean save() {
        boolean success = true;
        if (fieldGroup.isValid()) {
            if (isModified()) {
                try {
                    fieldGroup.commit();
                    bean = saveObject(bean);
                    BeanItem<TEditObject> beanItem = createBeanItem(bean);
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
            VerticalLayout root = new VerticalLayout();

            if (content instanceof TabSheet) {
                content.addStyleName(ExtaTheme.TABSHEET_PADDED_TABBAR);
            } else {
                Panel panel = new Panel();
                panel.setSizeFull();
                panel.addStyleName(ExtaTheme.PANEL_BORDERLESS);
                panel.addStyleName(ExtaTheme.PANEL_SCROLL_DIVIDER);
                VerticalLayout panelLayout = new VerticalLayout();
                panelLayout.addComponent(content);
                panelLayout.setMargin(true);
                panel.setContent(panelLayout);
                content = panel;
            }
            content.setSizeFull();
            root.addComponent(content);
            root.addComponent(buttonsPanel);
            root.setSizeFull();
            root.setExpandRatio(content, 1);
            setSizeFull();
            content = root;
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
