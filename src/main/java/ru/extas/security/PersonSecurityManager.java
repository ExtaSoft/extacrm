package ru.extas.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>PersonSecurityManager class.</p>
 *
 * @author Valery Orlov
 *         Date: 05.03.14
 *         Time: 22:31
 * @version $Id: $Id
 * @since 0.3
 */
@Component("PersonSecurityManager")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class PersonSecurityManager extends AbstractSecurityManager {

	/** {@inheritDoc} */
	@Override
	public Predicate createPredicateByOwners(final List<String> userList, final CriteriaBuilder cb, final CriteriaQuery<?> cq) {
		// Созданные или измененные мной физ. лица. Плюс клиенты из моих страховок, лидов, продаж.
		Root<Person> personRoot = (Root<Person>) cq.getRoots().iterator().next();

		Predicate ownPersons = cb.or(personRoot.get(Person_.createdBy).in(userList), personRoot.get(Person_.modifiedBy).in(userList));

		// Клиенты моих лидов
		CriteriaQuery<Person> leadPersons = cb.createQuery(Person.class);
		Root<Lead> leadRoot = leadPersons.from(Lead.class);
		leadPersons.select(leadRoot.get(Lead_.client));
		leadPersons.where(cb.or(leadRoot.get(Lead_.createdBy).in(userList), leadRoot.get(Lead_.modifiedBy).in(userList)));
		Predicate ownLeadsPersons = personRoot.in(leadPersons);

		// Клиенты моих продаж
		CriteriaQuery<Person> salesPersons = cb.createQuery(Person.class);
		Root<Sale> sale = salesPersons.from(Sale.class);
		salesPersons.select(sale.get(Sale_.client));
		salesPersons.where(cb.or(sale.get(Sale_.createdBy).in(userList), sale.get(Sale_.modifiedBy).in(userList)));
		Predicate ownSalesPersons = personRoot.in(salesPersons);

		// Клиенты моих страховок
		CriteriaQuery<Person> insPersons = cb.createQuery(Person.class);
		Root<Insurance> ins = insPersons.from(Insurance.class);
		insPersons.select(ins.get(Insurance_.client));
		insPersons.where(cb.or(ins.get(Insurance_.createdBy).in(userList), ins.get(Insurance_.modifiedBy).in(userList)));
		Predicate ownInsPersons = personRoot.in(insPersons);

		return cb.or(ownPersons, ownInsPersons, ownLeadsPersons, ownSalesPersons);
	}

}
