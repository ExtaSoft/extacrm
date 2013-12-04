package ru.extas.server.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Lead;
import ru.extas.model.Sale;
import ru.extas.server.LeadService;
import ru.extas.server.SaleService;

import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Завершает задачу в рамках бизнес процесса
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 11:55
 */
@Component("finishSaleTaskDelegate")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class FinishSaleTaskDelegate implements JavaDelegate {

    @Transactional
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Map<String, Object> processVariables = execution.getVariables();
        if (processVariables.containsKey("lead")) {
            Lead lead = (Lead) processVariables.get("lead");
            lead.setStatus(Lead.Status.CLOSED);
            lead.setResult(Lead.Result.SUCCESSFUL);
            LeadService leadService = lookup(LeadService.class);
            leadService.persist(lead);
        }
        if (processVariables.containsKey("sale")) {
            Sale sale = (Sale) processVariables.get("sale");
            sale.setStatus(Sale.Status.FINISHED);
            SaleService saleService = lookup(SaleService.class);
            saleService.persist(sale);
        }

    }

}
