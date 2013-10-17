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
 * @author Valery Orlov
 */
public abstract class AbstractEditForm<TEditObject> extends Window {

    private final Logger logger = LoggerFactory.getLogger(AbstractEditForm.class);

    private static final long serialVersionUID = -5592353839008000742L;
    private boolean saved = false;
    private final HorizontalLayout buttonsPanel = new HorizontalLayout();
    private final Button cancelBtn;
    private final Button okBtn;

    protected AbstractEditForm(final String caption, final BeanItem<TEditObject> beanItem) {
        super(caption);

        final TEditObject bean = beanItem.getBean();
        final ComponentContainer form = createEditFields(bean);
        initObject(bean);

        // Now create a binder
        final FieldGroup binder = new FieldGroup(beanItem);
        binder.setBuffered(true);
        binder.bindMemberFields(this);

        cancelBtn = new Button("Отмена", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                // TODO Проверять изменения и выдавать предупреждения
                binder.discard();
                UI.getCurrent().removeWindow(AbstractEditForm.this);
            }
        });
        cancelBtn.setStyleName("icon-cancel");
        cancelBtn.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        okBtn = new Button("OK", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {

                if (binder.isModified() && binder.isValid()) {
                    try {
                        binder.commit();
                        checkBeforeSave(bean);
                        saveObject(bean);
                        saved = true;
                    } catch (final CommitException e) {
                        // TODO Correct error handling
                        logger.error("Can't apply form changes", e);
                        Notification.show("Невозможно сохранить изменения", Type.ERROR_MESSAGE);
                        return;
                    }
                    UI.getCurrent().removeWindow(AbstractEditForm.this);
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

    public boolean isSaved() {
        return this.saved;
    }

    public void showModal() {
        setClosable(true);
        setModal(true);

        UI.getCurrent().addWindow(this);
    }

    /**
     * @param obj
     */
    protected abstract void initObject(TEditObject obj);

    /**
     * @param obj
     */
    protected abstract void saveObject(TEditObject obj);

    /**
     * @param obj
     */
    protected abstract void checkBeforeSave(TEditObject obj);

    /**
     * @return
     */
    protected abstract ComponentContainer createEditFields(TEditObject obj);

}