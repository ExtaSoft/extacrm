package ru.extas.server.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.server.SaleService;

import java.util.Map;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Создает продажу в рамках БП
 *
 * @author Valery Orlov
 *         Date: 14.11.13
 *         Time: 11:27
 * @version $Id: $Id
 * @since 0.3
 */
@Component("createSaleTaskDelegate")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class CreateSaleTaskDelegate implements JavaDelegate {

//    @Inject
//    private RuntimeService runtimeService;
//    @Autowired
//    private SaleRegistry saleService;

/** {@inheritDoc} */
@Transactional
@Override
public void execute(DelegateExecution execution) throws Exception {
	Map<String, Object> processVariables = execution.getVariables();
	if (processVariables.containsKey("lead")) {
		Lead lead = (Lead) processVariables.get("lead");
		SaleService saleRegistry = lookup(SaleService.class);
		Sale sale = saleRegistry.ctreateSaleByLead(lead);
		execution.setVariable("sale", sale);
	}
}


}
