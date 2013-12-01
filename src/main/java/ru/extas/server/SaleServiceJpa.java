package ru.extas.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.extas.model.Lead;
import ru.extas.model.Sale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Valery Orlov
 *         Date: 24.10.13
 *         Time: 0:33
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class SaleServiceJpa implements SaleService {

    private final static Logger logger = LoggerFactory.getLogger(SaleServiceJpa.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void persist(Sale obj) {
        logger.debug("Persisting sale");
        if (obj.getId() == null)
            em.persist(obj);
        else
            em.merge(obj);
    }

    @Transactional
    @Override
    public Sale ctreateSaleByLead(Lead lead) {
        Sale sale = new Sale();

        sale.setClient(lead.getClient());
        sale.setType(Sale.Type.CREDIT);
        sale.setVendor(null);
        sale.setStatus(Sale.Status.NEW);
        sale.setRegion(lead.getRegion());
        sale.setMotorType(lead.getMotorType());
        sale.setMotorBrand(lead.getMotorBrand());
        sale.setMotorModel(lead.getMotorModel());
        sale.setMotorPrice(lead.getMotorPrice());
        sale.setDealer(lead.getVendor());
        sale.setComment(lead.getComment());
        sale.setProcessId(lead.getProcessId());

        persist(sale);

        return sale;
    }
}
