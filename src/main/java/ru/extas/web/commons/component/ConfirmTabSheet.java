package ru.extas.web.commons.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.utils.RunnableSer;
import ru.extas.utils.SupplierSer;

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

    private final String confirmCaption = "Необходимо сохранить объект...";
    private final String confirmMessage = "Необходимо сохранить компанию прежде чем продолжить редактирование. Сохранить сейчас?";

    private final SupplierSer<Boolean> condition;
    private final RunnableSer action;

    public ConfirmTabSheet(SupplierSer<Boolean> condition, RunnableSer action) {
        this.condition = condition;
        this.action = action;

        addSelectedTabChangeListener(event -> {
            final Component selectedTab = getSelectedTab();
            if (selectedTab instanceof ConfirmTab) {
                final ConfirmTab confirmTab = (ConfirmTab) selectedTab;
                if (!this.condition.get()) {
                    ConfirmDialog.show(UI.getCurrent(),
                            confirmCaption,
                            confirmMessage,
                            "Да", "Нет",
                            dialog -> {
                                if (dialog.isConfirmed()) {
                                    Optional.ofNullable(action).ifPresent(a -> a.run());
                                    if (this.condition.get())
                                        confirmTab.show();
                                }
                            });

                } else
                    confirmTab.show();
            }
        });
    }

    public void addConfirmTab(Component content, String caption) {
        addTab(new ConfirmTab(content), caption);
    }

    public String getConfirmCaption() {
        return confirmCaption;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public static class ConfirmTab extends VerticalLayout {

        private final Component content;

        public ConfirmTab(Component content) {
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
