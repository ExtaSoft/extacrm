package ru.extas.server.sale;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.model.sale.Sale;
import ru.extas.model.security.AccessRole;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.lead.LeadRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
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

    @Inject
    private SaleRepository saleRepository;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private SalePointRepository salePointRepository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private LeadRepository leadRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Sale ctreateSaleByLead(final Lead lead) {
        final Sale sale = new Sale();

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
        sale.setResponsible(lead.getResponsible());

        return saleRepository.secureSave(sale);
    }

    @Transactional
    @Override
    public void finishSale(Sale sale, final Sale.Result result) {
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
        final Lead lead = sale.getLead();
        if (lead != null)
            leadRepository.finishLead(lead, leadResult);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JpaRepository<Sale, ?> getEntityRepository() {
        return saleRepository;
    }

    @Override
    protected Collection<Pair<Employee, AccessRole>> getObjectUsers(final Sale sale) {
        final ArrayList<Pair<Employee, AccessRole>> users = newArrayList();

        // Текущий пользователь как Владелец
        users.add(getCurUserAccess(sale));
        // Ответственный пользователь как Редактор
        if (sale.getResponsible() != null)
            users.add(new ImmutablePair<>(sale.getResponsible(), AccessRole.EDITOR));

        return users;
    }

    @Override
    protected Collection<Company> getObjectCompanies(final Sale sale) {
        final List<Company> companies = newArrayList();

        // Добавляем в область видимости компании дилера
        if(sale.getDealer() != null)
            companies.add(sale.getDealer().getCompany());

        return companies;
    }

    @Override
    protected Collection<SalePoint> getObjectSalePoints(final Sale sale) {
        final List<SalePoint> salePoints = newArrayList();

        // Добавляем в область видимости торговой точки
        if (sale.getDealer() != null)
            salePoints.add(sale.getDealer());

        return salePoints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> getObjectBrands(final Sale sale) {
        // Бренд техники
        if (!isNullOrEmpty(sale.getMotorBrand()))
            return newHashSet(sale.getMotorBrand());

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> getObjectRegions(final Sale sale) {
        final Set<String> regions = newHashSet();
        // Область видимости в регионе дилера
        if (sale.getDealer() != null
                && sale.getDealer().getRegAddress() != null
                && !isNullOrEmpty(sale.getDealer().getRegAddress().getRegion()))
            regions.add(sale.getDealer().getRegAddress().getRegion());
        return regions;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Sale permitAndSave(Sale sale,
                              final Collection<Pair<Employee, AccessRole>> users,
                              final Collection<SalePoint> salePoints,
                              final Collection<Company> companies,
                              final Collection<String> regions,
                              final Collection<String> brands) {
        if (sale != null) {
            sale = super.permitAndSave(sale, users, salePoints, companies, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты лида:
            // Клиент
            final Collection<Pair<Employee, AccessRole>> readers = reassigneRole(users, AccessRole.READER);
            personRepository.permitAndSave(sale.getClient(), readers, salePoints, companies, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(sale.getDealer(), readers, salePoints, companies, regions, brands);
            // Компания продавца
            if (sale.getDealer() != null)
                companyRepository.permitAndSave(sale.getDealer().getCompany(), readers, salePoints, companies, regions, brands);
        }
        return sale;
    }
}
