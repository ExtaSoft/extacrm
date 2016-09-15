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
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.model.lead.Lead;
import ru.extas.model.lead.LeadComment;
import ru.extas.model.lead.LeadFileContainer;
import ru.extas.model.product.ProductExpenditure;
import ru.extas.model.product.ProductInstance;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.SaleComment;
import ru.extas.model.sale.SaleFileContainer;
import ru.extas.model.security.AccessRole;
import ru.extas.security.AbstractSecuredRepository;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.LegalEntityRepository;
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
    private LegalEntityRepository legalEntityRepository;
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
    public Sale createSaleByLead(final Lead lead) {
        final Sale sale = new Sale();

        sale.setClient(lead.getClient());
        sale.setStatus(Sale.Status.NEW);
        sale.setRegion(lead.getRegion());
        sale.setMotorType(lead.getMotorType());
        sale.setMotorBrand(lead.getMotorBrand());
        sale.setMotorModel(lead.getMotorModel());
        sale.setMotorPrice(lead.getMotorPrice());
        sale.setDealer(lead.getVendor());
        sale.setDealerManager(lead.getDealerManager());
        sale.setComment(lead.getComment());
        sale.setProcessId(lead.getProcessId());
        sale.setLead(lead);
        sale.setResponsible(lead.getResponsible());
        sale.setResponsibleAssist(lead.getResponsibleAssist());
        sale.setSource(lead.getSource());

        final List<ProductInstance> saleProductInstances = newArrayList();
        for (final ProductInstance instance : lead.getProductInstances()) {
            final ProductInstance productInstance = new ProductInstance();
            productInstance.setArchived(instance.isArchived());
            productInstance.setCreatedBy(instance.getCreatedBy());
            productInstance.setCreatedDate(instance.getCreatedDate());
            productInstance.setLastModifiedBy(instance.getLastModifiedBy());
            productInstance.setLastModifiedDate(instance.getLastModifiedDate());
            productInstance.setSale(sale);
            productInstance.setProduct(instance.getProduct());
            productInstance.setSumm(instance.getSumm());
            productInstance.setPeriod(instance.getPeriod());
            productInstance.setDownpayment(instance.getDownpayment());
            productInstance.setResponsible(instance.getResponsible());
            productInstance.setState(instance.getState());
            final List<ProductExpenditure> expenditures = newArrayList();
            for (final ProductExpenditure expenditure : instance.getExpenditureList()) {
                final ProductExpenditure exp = new ProductExpenditure();
                exp.setType(expenditure.getType());
                exp.setCost(expenditure.getCost());
                exp.setComment(expenditure.getComment());
                expenditures.add(exp);
            }
            productInstance.setExpenditureList(expenditures);
            saleProductInstances.add(productInstance);
        }
        sale.setProductInstances(saleProductInstances);

        final List<SaleComment> saleComments = newArrayList();
        for (final LeadComment leadComment : lead.getComments()) {
            final SaleComment saleComment = new SaleComment(leadComment);
            saleComment.setOwnerId(sale.getId());
            saleComments.add(saleComment);
        }
        sale.setComments(saleComments);

        final List<SaleFileContainer> saleFiles = newArrayList();
        for (final LeadFileContainer leadFile : lead.getFiles())
            saleFiles.add(new SaleFileContainer(leadFile));
        sale.setFiles(saleFiles);

        return saleRepository.secureSave(sale);
    }

    @Transactional
    @Override
    public void finishSale(Sale sale) {
        logger.info("BEGIN Finish sale");
        Lead.Result leadResult = Lead.Result.SUCCESSFUL;

        sale.setStatus(Sale.Status.FINISHED);
        sale.getProductInstances().stream()
                .filter(p -> p.getState() == ProductInstance.State.IN_PROGRESS)
                .forEach(p -> p.setState(ProductInstance.State.AGREED));
        leadResult = Lead.Result.SUCCESSFUL;
        logger.info("Save sale during Finishing");
        sale = secureSave(sale);
        final Lead lead = sale.getLead();
        if (lead != null) {
            logger.info("Save lead during Finishing");
            leadRepository.finishLead(lead, leadResult);
        }
        logger.info("END Finish sale");
    }

    @Transactional
    @Override
    public void reopenSale(Sale sale) {
        sale.setStatus(Sale.Status.NEW);
        sale = secureSave(sale);
        final Lead lead = sale.getLead();
        if (lead != null)
            leadRepository.reopenLead(lead);
    }

    @Transactional
    @Override
    public void reopenSales(final Set<Sale> sales) {
        sales.forEach(s -> reopenSale(s));
    }

    @Transactional
    @Override
    public void finishSales(final Set<Sale> sales) {
        sales.forEach(s -> finishSale(s));
    }

    @Transactional
    @Override
    public void cancelSale(Sale sale, final Sale.CancelReason reason) {
        logger.info("BEGIN Cancel sale");
        sale.setStatus(Sale.Status.CANCELED);
        sale.setCancelReason(reason);
        sale.getProductInstances().stream()
                .filter(p -> p.getState() == ProductInstance.State.IN_PROGRESS)
                .forEach(p -> p.setState(ProductInstance.State.REJECTED));
        logger.info("Save sale during Canceling");
        sale = secureSave(sale);
        final Lead lead = sale.getLead();
        if (lead != null) {
            logger.info("Cancel lead during Canceling");
            leadRepository.finishLead(lead, Lead.Result.CLIENT_REJECTED);
        }
        logger.info("END Cancel sale");
    }

    @Transactional
    @Override
    public void cancelSales(final Set<Sale> sales, final Sale.CancelReason reason) {
        sales.forEach(s -> cancelSale(s, reason));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JpaRepository<Sale, ?> getEntityRepository() {
        return saleRepository;
    }

    @Override
    protected Collection<Pair<String, AccessRole>> getObjectUsers(final Sale sale) {
        final ArrayList<Pair<String, AccessRole>> users = newArrayList();

        // Текущий пользователь как Владелец
        users.add(getCurUserAccess(sale));
        // Ответственный пользователь как Редактор
        if (sale.getResponsible() != null)
            users.add(new ImmutablePair<>(sale.getResponsible().getId(), AccessRole.EDITOR));
        if (sale.getResponsibleAssist() != null)
            users.add(new ImmutablePair<>(sale.getResponsibleAssist().getId(), AccessRole.EDITOR));
        if (sale.getDealerManager() != null)
            users.add(new ImmutablePair<>(sale.getDealerManager().getId(), AccessRole.READER));
        // Ответственные по продуктам
        for (final ProductInstance productInstance : sale.getProductInstances()) {
            final Employee prodResponsible = productInstance.getResponsible();
            if (prodResponsible != null) {
                users.add(new ImmutablePair<>(prodResponsible.getId(), AccessRole.READER));
            }
        }
        return users;
    }

    @Override
    protected Collection<String> getObjectCompanies(final Sale sale) {
        final List<String> companies = newArrayList();

        // Добавляем в область видимости компании дилера
        if (sale.getDealer() != null)
            companies.add(sale.getDealer().getCompany().getId());

        return companies;
    }

    @Override
    protected Collection<String> getObjectSalePoints(final Sale sale) {
        final List<String> salePoints = newArrayList();

        // Добавляем в область видимости торговой точки
        if (sale.getDealer() != null)
            salePoints.add(sale.getDealer().getId());

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
                && sale.getDealer().getPosAddress() != null
                && !isNullOrEmpty(sale.getDealer().getPosAddress().getRegionWithType()))
            regions.add(sale.getDealer().getPosAddress().getRegionWithType());
        return regions;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Sale permitAndSave(Sale sale,
                              final Collection<Pair<String, AccessRole>> users,
                              final Collection<String> salePoints,
                              final Collection<String> companies,
                              final Collection<String> regions,
                              final Collection<String> brands) {
        if (sale != null) {
            sale = super.permitAndSave(sale, users, salePoints, companies, regions, brands);
            // При этом необходимо сделать “видимыми” все связанные объекты лида:
            // Клиент
            final Collection<Pair<String, AccessRole>> readers = reassigneRole(users, AccessRole.READER);
            if (sale.getClient() != null)
                if (sale.getClient() instanceof Person)
                    personRepository.permitAndSave((Person) sale.getClient(), readers, salePoints, companies, regions, brands);
                else if (sale.getClient() instanceof LegalEntity)
                    legalEntityRepository.permitAndSave((LegalEntity) sale.getClient(), readers, salePoints, companies, regions, brands);
            // Продавец (торговая точка или компания)
            salePointRepository.permitAndSave(sale.getDealer(), readers, salePoints, companies, regions, brands);
            // Компания продавца
            if (sale.getDealer() != null)
                companyRepository.permitAndSave(sale.getDealer().getCompany(), readers, salePoints, companies, regions, brands);
        }
        return sale;
    }
}
