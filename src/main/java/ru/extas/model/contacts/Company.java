package ru.extas.model.contacts;

import javax.persistence.*;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

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
public class Company extends Contact {

	private static final long serialVersionUID = -5681940552175752858L;

	// Собственник(и) Компании
	@ManyToMany(targetEntity = Person.class, cascade = CascadeType.REFRESH)
	@JoinTable(
			name = "COMPANY_OWNER",
			joinColumns = {@JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "OWNER_ID", referencedColumnName = "ID")})
	private Set<Person> owners = newHashSet();

	// Сотрудники компании
	@ManyToMany(targetEntity = Person.class, cascade = CascadeType.REFRESH)
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
