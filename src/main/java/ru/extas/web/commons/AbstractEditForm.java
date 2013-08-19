/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.AbstractExtaObject;

/**
 * @author Valery Orlov
 */
public abstract class AbstractEditForm<TEditObject extends AbstractExtaObject> extends Window {

    private final Logger logger = LoggerFactory.getLogger(AbstractEditForm.class);

    private static final long serialVersionUID = -5592353839008000742L;
    private boolean saved = false;
    private final HorizontalLayout buttonsPanel = new HorizontalLayout();
    private final Button cancelBtn;
    private final Button okBtn;

    protected AbstractEditForm(final String caption, final BeanItem<TEditObject> beanItem) {
        super(caption);

        final TEditObject bean = beanItem.getBean();
        final FormLayout form = createEditFields(bean);

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

        okBtn = new Button("OK", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {

                if (binder.isValid()) {
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
        this.buttonsPanel.addComponent(okBtn);
        this.buttonsPanel.setComponentAlignment(okBtn, Alignment.MIDDLE_RIGHT);
        this.buttonsPanel.addComponent(cancelBtn);
        this.buttonsPanel.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
        this.buttonsPanel.setSpacing(true);

        setContent(form);

        this.addCloseListener(new CloseListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(final CloseEvent e) {

                // TODO Обработать закрытие формы по кресту

            }
        });
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
    protected abstract FormLayout createEditFields(TEditObject obj);

}