package ru.extas.server.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Lead;
import ru.extas.model.Sale;
import ru.extas.server.LeadRepository;
import ru.extas.server.SaleRegistry;

import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Отменяет бизнес процесс
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 11:55
 */
@Component("cancelSaleTaskDelegate")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class CancelSaleTaskDelegate implements JavaDelegate {

@Transactional
@Override
public void execute(DelegateExecution execution) throws Exception {

	Map<String, Object> processVariables = execution.getVariables();
	if (processVariables.containsKey("lead")) {
		Lead lead = (Lead) processVariables.get("lead");
		lead.setStatus(Lead.Status.CLOSED);
		if (processVariables.containsKey("getBankResponseTaskResult") && processVariables.get("getBankResponseTaskResult").equals("Rejected")) {
			lead.setResult(Lead.Result.VENDOR_REJECTED);
		} else
			lead.setResult(Lead.Result.CLIENT_REJECTED);
		LeadRepository leadRepository = lookup(LeadRepository.class);
		leadRepository.save(lead);
	}
	if (processVariables.containsKey("sale")) {
		Sale sale = (Sale) processVariables.get("sale");
		sale.setStatus(Sale.Status.CANCELED);
		if (processVariables.containsKey("getBankResponseTaskResult") && processVariables.get("getBankResponseTaskResult").equals("Rejected")) {
			sale.setResult(Sale.Result.VENDOR_REJECTED);
		} else
			sale.setResult(Sale.Result.CLIENT_REJECTED);
		SaleRegistry saleRegistry = lookup(SaleRegistry.class);
		saleRegistry.save(sale);
	}

}

}
