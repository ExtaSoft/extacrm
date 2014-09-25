package ru.extas.server.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Person;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.lead.LeadRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Sets.newHashSet;

/**
 * <p>SaleServiceImpl class.</p>
 *
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:33
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class SaleRepositoryImpl extends AbstractSecuredRepository<Sale> implements SaleService {

	private final static Logger logger = LoggerFactory.getLogger(SaleRepositoryImpl.class);

	@Inject private SaleRepository saleRepository;
    @Inject private PersonRepository personRepository;
    @Inject private SalePointRepository salePointRepository;
    @Inject private LeadRepository leadRepository;

	/** {@inheritDoc} */
	@Transactional
	@Override
	public Sale ctreateSaleByLead(Lead lead) {
		Sale sale = new Sale();

		sale.setClient(lead.getClient());
		sale.setStatus(Sale.Status.NEW);
		sale.setRegion(lead.getRegion());
		sale.setMotorType(lead.getMotorType());
		sale.setMotorBrand(lead.getMotorBrand());
		sale.setMotorModel(lead.getMotorModel());
		sale.setMotorPrice(lead.getMotorPrice());
		sale.setDealer(lead.getVendor());
		sale.setComment(lead.getComment());
		sale.setProcessId(lead.getProcessId());
        sale.setLead(lead);

		return saleRepository.secureSave(sale);

	}

    @Transactional
    @Override
    public void finishSale(Sale sale, Sale.Result result) {
        Lead.Result leadResult = Lead.Result.SUCCESSFUL;
        sale.setResult(result);

        switch (result) {
            case SUCCESSFUL:
                sale.setStatus(Sale.Status.FINISHED);
                leadResult = Lead.Result.SUCCESSFUL;
                break;
            case VENDOR_REJECTED:
                sale.setStatus(Sale.Status.CANCELED);
                leadResult = Lead.Result.VENDOR_REJECTED;
                break;
            case CLIENT_REJECTED:
                sale.setStatus(Sale.Status.CANCELED);
                leadResult = Lead.Result.CLIENT_REJECTED;
                break;
        }
        sale = secureSave(sale);
        Lead lead = sale.getLead();
        if(lead != null)
            leadRepository.finishLead(lead, leadResult);
    }

    /** {@inheritDoc} */
    @Override
    public JpaRepository<Sale, ?> getEntityRepository() {
        return saleRepository;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectBrands(Sale sale) {
        if(!isNullOrEmpty(sale.getMotorBrand()))
            newHashSet(sale.getMotorBrand());

        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<String> getObjectRegions(Sale sale) {
        Set<String> regions = newHashSet();
        if(sale.getClient() != null
                && sale.getClient().getRegAddress() != null
                && !isNullOrEmpty(sale.getClient().getRegAddress().getRegion()))
            regions.add(sale.getClient().getRegAddress().getRegion());
        if(sale.getDealer() != null
                && sale.getDealer().getRegAddress() != null
                && !isNullOrEmpty(sale.getDealer().getRegAddress().getRegion()))
            regions.add(sale.getDealer().getRegAddress().getRegion());
        return regions;
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Sale permitAndSave(Sale sale, Person userContact, Collection<String> regions, Collection<String> brands) {
        if (sale != null) {
            sale = super.permitAndSave(sale, userContact, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты лида:
            // Клиент
//            if(lead.getClient() instanceof Person)
            personRepository.permitAndSave(sale.getClient(), userContact, regions, brands);
//            else
//                companyRepository.permitAndSave((Company) lead.getClient(), userContact, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(sale.getDealer(), userContact, regions, brands);
        }
        return sale;
    }
}
