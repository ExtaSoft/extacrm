/**
 * 
 */
package ru.extas.web.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.AbstractExtaObject;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Valery Orlov
 * 
 */
public abstract class AbstractEditForm<TEditObject extends AbstractExtaObject> extends Window {

	private final Logger logger = LoggerFactory.getLogger(AbstractEditForm.class);

	private static final long serialVersionUID = -5592353839008000742L;
	protected boolean saved = false;
	protected final HorizontalLayout buttonsPanel = new HorizontalLayout();
	protected final Button cancelBtn;
	protected final Button okBtn;

	public AbstractEditForm(String caption, final TEditObject obj) {
		super(caption);

		FormLayout form = createEditFields(obj);

		BeanItem<TEditObject> item = new BeanItem<TEditObject>(obj);

		initObject(obj);

		// Now create a binder
		final FieldGroup binder = new FieldGroup(item);
		binder.setBuffered(true);
		binder.bindMemberFields(this);

		cancelBtn = new Button("Отмена", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
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
						checkBeforeSave(obj);
						saveObject(obj);
						saved = true;
					} catch (CommitException e) {
						// TODO Correct error handling
						logger.error("Can't apply form changes", e);
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