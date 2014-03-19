package ru.extas.web.commons.window;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Стандартное окошко OK/Отмена
 *
 * @author Valery Orlov
 * @version $Id: $Id
 */
public class OkCancelWindow extends Window {

	private static final long serialVersionUID = -1869372339151029572L;
	private boolean okPressed = false;
	private final HorizontalLayout buttonsPanel = new HorizontalLayout();
	protected Button cancelBtn;
	protected Button okBtn;

	/**
	 * <p>Constructor for OkCancelWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 * @param content a {@link com.vaadin.ui.Component} object.
	 */
	public OkCancelWindow(final String caption, final Component content) {
		super(caption);
		initInputWindow();
		setContent(content);
	}

	/**
	 * <p>Constructor for OkCancelWindow.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public OkCancelWindow(final String caption) {
		super(caption);
		initInputWindow();
	}

	private void initInputWindow() {
		cancelBtn = new Button("Отмена", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				OkCancelWindow.this.okPressed = false;
				close();
			}
		});
		cancelBtn.setStyleName("icon-cancel");
		okBtn = new Button("OK", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				OkCancelWindow.this.okPressed = true;
				close();
			}
		});
		okBtn.setStyleName("icon-ok");
		this.buttonsPanel.addComponent(okBtn);
		this.buttonsPanel.setComponentAlignment(okBtn, Alignment.MIDDLE_RIGHT);
		this.buttonsPanel.addComponent(cancelBtn);
		this.buttonsPanel.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		this.buttonsPanel.setSpacing(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.ui.AbstractSingleComponentContainer#setContent(com.vaadin.
	 * ui.Component)
	 */
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
	 * <p>isOkPressed.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isOkPressed() {
		return this.okPressed;
	}

}
