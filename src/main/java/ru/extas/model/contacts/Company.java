package ru.extas.model.contacts;

import javax.persistence.*;
import java.util.List;

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
	private List<Person> owners;

	// Сотрудники компании
	@ManyToMany(targetEntity = Person.class)
	@JoinTable(
			name = "CONTACT_EMPLOYEE",
			joinColumns = {@JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")})
	private List<Person> employeeList;

	// Юридические лица компании
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LegalEntity> legalEntities;

	// Торговые точки компании
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SalePoint> salePoints;

	/**
	 * <p>Constructor for Company.</p>
	 */
	public Company() {
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
	public List<Person> getOwners() {
		return owners;
	}

	/**
	 * <p>Setter for the field <code>owners</code>.</p>
	 *
	 * @param ownerList a {@link java.util.List} object.
	 */
	public void setOwners(final List<Person> ownerList) {
		this.owners = ownerList;
	}

	/**
	 * <p>Getter for the field <code>salePoints</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<SalePoint> getSalePoints() {
		return salePoints;
	}

	/**
	 * <p>Setter for the field <code>salePoints</code>.</p>
	 *
	 * @param salePointList a {@link java.util.List} object.
	 */
	public void setSalePoints(final List<SalePoint> salePointList) {
		this.salePoints = salePointList;
	}

	/**
	 * <p>Getter for the field <code>employeeList</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Person> getEmployeeList() {
		return employeeList;
	}

	/**
	 * <p>Setter for the field <code>employeeList</code>.</p>
	 *
	 * @param employeeList a {@link java.util.List} object.
	 */
	public void setEmployeeList(final List<Person> employeeList) {
		this.employeeList = employeeList;
	}

	/**
	 * <p>Getter for the field <code>legalEntities</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<LegalEntity> getLegalEntities() {
		return legalEntities;
	}

	/**
	 * <p>Setter for the field <code>legalEntities</code>.</p>
	 *
	 * @param legalEntities a {@link java.util.List} object.
	 */
	public void setLegalEntities(final List<LegalEntity> legalEntities) {
		this.legalEntities = legalEntities;
	}
}
