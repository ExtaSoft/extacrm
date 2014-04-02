package ru.extas.model.contacts;

import ru.extas.server.contacts.PersonRepository;
import ru.extas.server.users.UserManagementService;

import javax.persistence.*;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@DiscriminatorValue("COMPANY")
@Table(name = "COMPANY")
public class Company extends Contact implements Cloneable {

	private static final long serialVersionUID = -5681940552175752858L;

	// Собственник(и) Компании
	@ManyToMany(targetEntity = Person.class)
	@JoinTable(
			name = "COMPANY_OWNER",
			joinColumns = {@JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "OWNER_ID", referencedColumnName = "ID")})
	private Set<Person> owners = newHashSet();

	// Сотрудники компании
	@ManyToMany(targetEntity = Person.class)
	@JoinTable(
			name = "CONTACT_EMPLOYEE",
			joinColumns = {@JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")})
	private Set<Person> employees = newHashSet();

	// Юридические лица компании
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<LegalEntity> legalEntities = newHashSet();

	// Торговые точки компании
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SalePoint> salePoints = newHashSet();

	/**
	 * <p>Constructor for Company.</p>
	 */
	public Company() {
	}

    @Override
    protected void logSecurePrivileges() {
        super.logSecurePrivileges();
        // При этом необходимо сделать “видимыми” все связанные объекты компании:
        Person userContact = lookup(UserManagementService.class).getCurrentUserContact();
        // Собственник(и) Компании
        PersonRepository personRepository = lookup(PersonRepository.class);
        for(Person owner :getOwners()) {
            owner.getAssociateUsers().add(userContact);
            personRepository.save(owner);
        }
        // Сотрудники компании
        for(Person employee : getEmployees()){
            employee.getAssociateUsers().add(userContact);
            personRepository.save(employee);
        }
        // Юридические лица компании
        for(LegalEntity legalEntity : getLegalEntities()) {
            legalEntity.getAssociateUsers().add(userContact);
        }
        // Торговые точки компании
        for(SalePoint salePoint : getSalePoints()) {
            salePoint.getAssociateUsers().add(userContact);
        }
    }

    /** {@inheritDoc} */
	@Override
	public Company clone() {
		Company newObj = new Company();
		super.copyTo(newObj);
		return newObj;
	}

	/**
	 * <p>Getter for the field <code>owners</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public Set<Person> getOwners() {
		return owners;
	}

	/**
	 * <p>Setter for the field <code>owners</code>.</p>
	 *
	 * @param ownerList a {@link java.util.List} object.
	 */
	public void setOwners(final Set<Person> ownerList) {
		this.owners = ownerList;
	}

	/**
	 * <p>Getter for the field <code>salePoints</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public Set<SalePoint> getSalePoints() {
		return salePoints;
	}

	/**
	 * <p>Setter for the field <code>salePoints</code>.</p>
	 *
	 * @param salePointList a {@link java.util.List} object.
	 */
	public void setSalePoints(final Set<SalePoint> salePointList) {
		this.salePoints = salePointList;
	}

	/**
	 * <p>Getter for the field <code>employeeList</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public Set<Person> getEmployees() {
		return employees;
	}

	/**
	 * <p>Setter for the field <code>employeeList</code>.</p>
	 *
	 * @param employeeList a {@link java.util.List} object.
	 */
	public void setEmployees(final Set<Person> employeeList) {
		this.employees = employeeList;
	}

	/**
	 * <p>Getter for the field <code>legalEntities</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public Set<LegalEntity> getLegalEntities() {
		return legalEntities;
	}

	/**
	 * <p>Setter for the field <code>legalEntities</code>.</p>
	 *
	 * @param legalEntities a {@link java.util.List} object.
	 */
	public void setLegalEntities(final Set<LegalEntity> legalEntities) {
		this.legalEntities = legalEntities;
	}
}
