package ru.extas.web.commons;

import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.utils.RunnableSer;
import ru.extas.utils.SupplierSer;

import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Позволяет запрашивать подтверждение на выполнение некоторых действий.
 *
 * @author Valery Orlov
 *         Date: 14.11.2014
 *         Time: 19:38
 */
public class PredictConfirmedAction implements Serializable {

    private String confirmCaption;
    private String confirmMessage;

    // Условие, выполнение которого необходимо для запуска основной процедуры
    private SupplierSer<Boolean> condition;

    // Предварительные операции, чтобы выполнить условие
    private RunnableSer predictAction;

    public PredictConfirmedAction() {
    }

    public PredictConfirmedAction(final String confirmCaption, final String confirmMessage, final SupplierSer<Boolean> condition, final RunnableSer predictAction) {
        this.confirmCaption = confirmCaption;
        this.confirmMessage = confirmMessage;
        this.condition = condition;
        this.predictAction = predictAction;
    }

    public void run(final RunnableSer action) {
        checkNotNull(confirmCaption);
        checkNotNull(confirmMessage);
        checkNotNull(condition);
        checkNotNull(predictAction);
        checkNotNull(action);

        // проверяем выполняется ли условие
        if (!this.condition.get()) {
            // запрашиваем подтверждение на выполнение предварительных операций
            ConfirmDialog.show(UI.getCurrent(),
                    confirmCaption,
                    confirmMessage,
                    "Да", "Нет",
                    dialog -> {
                        if (dialog.isConfirmed()) {
                            // когда получили подтверждение - выполняем предварительные процедуры
                            predictAction.run();
                            // запускаем основную процедуру если предварительная закончилась успехом
                            if (this.condition.get())
                                action.run();
                        }
                    });
        } else
            action.run();
    }

    public String getConfirmCaption() {
        return confirmCaption;
    }

    public void setConfirmCaption(final String confirmCaption) {
        this.confirmCaption = confirmCaption;
    }

    public String getConfirmMessage() {
        return confirmMessage;
    }

    public void setConfirmMessage(final String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public SupplierSer<Boolean> getCondition() {
        return condition;
    }

    public void setCondition(final SupplierSer<Boolean> condition) {
        this.condition = condition;
    }

    public RunnableSer getPredictAction() {
        return predictAction;
    }

    public void setPredictAction(final RunnableSer predictAction) {
        this.predictAction = predictAction;
    }

}
