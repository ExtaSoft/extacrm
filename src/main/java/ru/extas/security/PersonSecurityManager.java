package ru.extas.security;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.extas.model.common.SecuredObject;
import ru.extas.model.contacts.*;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.insurance.Insurance_;
import ru.extas.model.lead.Lead;
import ru.extas.model.lead.Lead_;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.server.contacts.CompanyRepository;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.users.UserManagementService;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

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

    @Inject
    private UserManagementService userService;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private SalePointRepository salePointRepository;

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

    /** {@inheritDoc} */
    @Override
    public void addOwnerPrivileges(SecuredObject securedObject) {

        checkNotNull(securedObject);
        checkState(securedObject instanceof Person);

        // Получить контакт текущего пользователя
        Person user = userService.getCurrentUserContact();

        // Получить компании пользователя
        List<Company> companies = companyRepository.findByEmployee(user);

        // Получить торговые точки пользователя
        List<SalePoint> salePoints = salePointRepository.findByEmployee(user);

        // Уровень доступа

        Person person = (Person) securedObject;
        PersonPrivilege privilege = new PersonPrivilege();
        privilege.setPerson(person);
    }

}
