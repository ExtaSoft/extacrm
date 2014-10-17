package ru.extas.server.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.server.lead.LeadRepository;
import ru.extas.server.sale.SaleRepository;

import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Завершает задачу в рамках бизнес процесса
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 11:55
 * @version $Id: $Id
 * @since 0.3
 */
@Component("finishSaleTaskDelegate")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class FinishSaleTaskDelegate implements JavaDelegate {

/** {@inheritDoc} */
@Transactional
@Override
public void execute(final DelegateExecution execution) throws Exception {

	final Map<String, Object> processVariables = execution.getVariables();
	if (processVariables.containsKey("lead")) {
		final Lead lead = (Lead) processVariables.get("lead");
		lead.setStatus(Lead.Status.CLOSED);
		lead.setResult(Lead.Result.SUCCESSFUL);
		final LeadRepository leadRepository = lookup(LeadRepository.class);
		leadRepository.secureSave(lead);
	}
	if (processVariables.containsKey("sale")) {
		final Sale sale = (Sale) processVariables.get("sale");
		sale.setStatus(Sale.Status.FINISHED);
		sale.setResult(Sale.Result.SUCCESSFUL);
		final SaleRepository saleRepository = lookup(SaleRepository.class);
		saleRepository.secureSave(sale);
	}

}

}
