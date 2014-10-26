package ru.extas.server.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.server.sale.SaleRepository;

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
public void execute(final DelegateExecution execution) throws Exception {
	final Map<String, Object> processVariables = execution.getVariables();
	if (processVariables.containsKey("lead")) {
		final Lead lead = (Lead) processVariables.get("lead");
        final SaleRepository saleRepository = lookup(SaleRepository.class);
		final Sale sale = saleRepository.ctreateSaleByLead(lead);
		execution.setVariable("sale", sale);
	}
}


}
