/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package ru.extas.web.insurance;

import static com.google.common.collect.Lists.newArrayList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.web.commons.ExtaAbstractView;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Раздел страхование
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceView extends ExtaAbstractView {

	private static final long serialVersionUID = -2524035728558575428L;
	private final Logger logger = LoggerFactory.getLogger(InsuranceView.class);

	public interface TabInfo extends Serializable {

		public String getCaption();

		public Component createComponent();
	}

	public abstract class AbstractTabInfo implements TabInfo {
		private static final long serialVersionUID = -4891758708180700074L;
		private final String caption;

		/**
		 * @param caption
		 */
		public AbstractTabInfo(String caption) {
			this.caption = caption;
		}

		/**
		 * @return the caption
		 */
		@Override
		public String getCaption() {
			return caption;
		}

	}

	public InsuranceView() {
	}

	@Override
	protected Component getContent() {
		logger.info("Creating view content...");

		TabSheet tabsheet = new TabSheet();
		tabsheet.setSizeFull();

		// Create tab content dynamically when tab is selected
		tabsheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// Find the tabsheet
				TabSheet tabsheet = event.getTabSheet();
				// Find the tab (here we know it's a layout)
				VerticalLayout tab = (VerticalLayout) tabsheet.getSelectedTab();

				if (tab.getComponentCount() == 0) {
					// Инициализируем содержимое закладки
					TabInfo info = (TabInfo) tab.getData();

					tab.removeAllComponents();
					Component tabContent = info.createComponent();
					tabContent.setSizeFull();
					tab.addComponent(tabContent);
				}
			}
		});

		// Создаем закладки в соответствии с описанием
		for (TabInfo info : getTabComponentsInfo()) {
			VerticalLayout viewTab = new VerticalLayout();
			viewTab.setSizeFull();
			viewTab.setData(info);
			tabsheet.addTab(viewTab, info.getCaption());
		}

		return tabsheet;
	}

	protected List<TabInfo> getTabComponentsInfo() {
		ArrayList<TabInfo> ret = newArrayList();
		ret.add(new AbstractTabInfo("Имущ. страховки") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component createComponent() {
				return new InsuranceGrid();
			}
		});
		ret.add(new AbstractTabInfo("Бланки (БСО)") {
			private static final long serialVersionUID = 1L;

			@Override
			public Component createComponent() {
				return new BSOGrid();
			}
		});
		return ret;
	}

	/**
	 * @return
	 */
	@Override
	protected Component getTitle() {
		final Component title = new Label("Страхование техники");
		title.setSizeUndefined();
		title.addStyleName("h1");
		return title;
	}

}
