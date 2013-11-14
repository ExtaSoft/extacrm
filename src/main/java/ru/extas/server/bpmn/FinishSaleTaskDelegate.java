package ru.extas.server.bpmn;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * Завершает задачу в рамках бизнес процесса
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 11:55
 */
public class FinishSaleTaskDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
    }
}
