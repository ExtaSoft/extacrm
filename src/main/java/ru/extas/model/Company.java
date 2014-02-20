package ru.extas.model;

import javax.persistence.*;
import java.util.List;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
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

	public Company() {
	}

	@Override
	public Company clone() {
		Company newObj = new Company();
		super.copyTo(newObj);
		return newObj;
	}

	public List<Person> getOwners() {
		return owners;
	}

	public void setOwners(final List<Person> ownerList) {
		this.owners = ownerList;
	}

	public List<SalePoint> getSalePoints() {
		return salePoints;
	}

	public void setSalePoints(final List<SalePoint> salePointList) {
		this.salePoints = salePointList;
	}

	public List<Person> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(final List<Person> employeeList) {
		this.employeeList = employeeList;
	}

	public List<LegalEntity> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(final List<LegalEntity> legalEntities) {
		this.legalEntities = legalEntities;
	}
}