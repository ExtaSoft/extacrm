package ru.extas.web.commons.window;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Стандартное окошко OK/Отмена
 *
 * @author Valery Orlov
 */
public class CloseOnlylWindow extends Window {

	private static final long serialVersionUID = -1869372339151029572L;
	private boolean okPressed = false;
	private final HorizontalLayout buttonsPanel = new HorizontalLayout();
	protected Button closeBtn;

	public CloseOnlylWindow(final String caption, final Component content) {
		super(caption);
		initInputWindow();
		setContent(content);
	}

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
		closeBtn.setStyleName("icon-ok");
		this.buttonsPanel.addComponent(closeBtn);
		this.buttonsPanel.setComponentAlignment(closeBtn, Alignment.MIDDLE_RIGHT);
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
