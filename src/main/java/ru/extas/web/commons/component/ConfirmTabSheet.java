package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.utils.RunnableSer;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.PredictConfirmedAction;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

/**
 * Таб, позволяющий создание контента каждой вкладки, только перед ее отображением
 *
 * @author Valery Orlov
 *         Date: 20.10.2014
 *         Time: 18:57
 */
public class ConfirmTabSheet extends TabSheet {

    private final PredictConfirmedAction confirmedAction;

    public ConfirmTabSheet(final SupplierSer<Boolean> condition, final RunnableSer action) {
        confirmedAction = new PredictConfirmedAction(
                "Необходимо сохранить компанию...",
                "Необходимо сохранить компанию прежде чем продолжить редактирование. Сохранить сейчас?",
                condition, action);

        addSelectedTabChangeListener(event -> {
            final Component selectedTab = getSelectedTab();
            if (selectedTab instanceof ConfirmTab) {
                final ConfirmTab confirmTab = (ConfirmTab) selectedTab;
                confirmedAction.run(() -> confirmTab.show());
            }
        });
    }

    public void addConfirmTab(final Component content, final String caption) {
        addTab(new ConfirmTab(content), caption);
    }

    public static class ConfirmTab extends VerticalLayout {

        private final Component content;

        public ConfirmTab(final Component content) {
            this.content = content;
        }

        public void show() {
            if (getComponentCount() == 0) {
                setHeight(content.getHeight(), content.getHeightUnits());
                setWidth(content.getWidth(), content.getWidthUnits());
                addComponent(content);
            }
        }
    }
}
