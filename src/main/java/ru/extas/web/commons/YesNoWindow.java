package ru.extas.web.commons;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Стандартное окошко OK/Отмена
 * 
 * @author Valery Orlov
 * 
 */
public class YesNoWindow extends Window {

	private static final long serialVersionUID = -1869372339151029572L;
	private boolean okPressed = false;
	private final HorizontalLayout buttonsPanel = new HorizontalLayout();
	private Button cancelBtn;

	public YesNoWindow(final String caption, final Component content) {
		super(caption);
		initInputWindow();
		setContent(content);
	}

	public YesNoWindow(final String caption) {
		super(caption);
		initInputWindow();
	}

	private void initInputWindow() {
		cancelBtn = new Button("Отмена", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				YesNoWindow.this.okPressed = false;
				UI.getCurrent().removeWindow(YesNoWindow.this);
			}
		});
		cancelBtn.setStyleName("icon-cancel");
		final Button okBtn = new Button("OK", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				YesNoWindow.this.okPressed = true;
				UI.getCurrent().removeWindow(YesNoWindow.this);
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

	public void showModal() {
		setClosable(true);
		setModal(true);

		UI.getCurrent().addWindow(this);
	}

	public boolean isOkPressed() {
		return this.okPressed;
	}

}
