package ru.extas.model;

import javax.persistence.*;
import java.util.List;

/**
 * Модель данных "Точка продаж"
 *
 * @author Valery Orlov
 *         Date: 10.02.14
 *         Time: 15:24
 * @version $Id: $Id
 */
@Entity
@DiscriminatorValue("SALEPOINT")
@Table(name = "SALE_POINT")
public class SalePoint extends Contact implements Cloneable {

	// Компания
	@ManyToOne(optional = false)
	private Company company;

	// Юр. лица работающие на торговой точке
	@ManyToMany(targetEntity = LegalEntity.class)
	@JoinTable(
			name = "SALEPOINT_LEGALENTITY",
			joinColumns = {@JoinColumn(name = "SALEPOINT_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "LEGALENTITY_ID", referencedColumnName = "ID")})
	private List<LegalEntity> legalEntities;

	// Сотрудники
	@ManyToMany(targetEntity = Person.class)
	@JoinTable(
			name = "CONTACT_EMPLOYEE",
			joinColumns = {@JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")})
	private List<Person> employes;

	// Идентификация:

	//  - Код Экстрим Ассистанс
	@Column(name = "EXTA_CODE", length = 15)
	private String extaCode;

	//  - Код Альфа Банка
	@Column(name = "ALPHA_CODE", length = 15)
	private String alphaCode;

	//  - Код HomeCredit Банка
	@Column(name = "HOME_CODE", length = 15)
	private String homeCode;

	//  - Код Банка СЕТЕЛЕМ
	@Column(name = "SETELEM_CODE", length = 15)
	private String setelemCode;

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

	/**
	 * <p>Getter for the field <code>employes</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Person> getEmployes() {
		return employes;
	}

	/**
	 * <p>Setter for the field <code>employes</code>.</p>
	 *
	 * @param employes a {@link java.util.List} object.
	 */
	public void setEmployes(final List<Person> employes) {
		this.employes = employes;
	}

	/**
	 * <p>Getter for the field <code>extaCode</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getExtaCode() {
		return extaCode;
	}

	/**
	 * <p>Setter for the field <code>extaCode</code>.</p>
	 *
	 * @param extaCode a {@link java.lang.String} object.
	 */
	public void setExtaCode(final String extaCode) {
		this.extaCode = extaCode;
	}

	/**
	 * <p>Getter for the field <code>alphaCode</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAlphaCode() {
		return alphaCode;
	}

	/**
	 * <p>Setter for the field <code>alphaCode</code>.</p>
	 *
	 * @param alphaCode a {@link java.lang.String} object.
	 */
	public void setAlphaCode(final String alphaCode) {
		this.alphaCode = alphaCode;
	}

	/**
	 * <p>Getter for the field <code>homeCode</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHomeCode() {
		return homeCode;
	}

	/**
	 * <p>Setter for the field <code>homeCode</code>.</p>
	 *
	 * @param homeCode a {@link java.lang.String} object.
	 */
	public void setHomeCode(final String homeCode) {
		this.homeCode = homeCode;
	}

	/**
	 * <p>Getter for the field <code>setelemCode</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSetelemCode() {
		return setelemCode;
	}

	/**
	 * <p>Setter for the field <code>setelemCode</code>.</p>
	 *
	 * @param setelemCode a {@link java.lang.String} object.
	 */
	public void setSetelemCode(final String setelemCode) {
		this.setelemCode = setelemCode;
	}

	/**
	 * <p>Getter for the field <code>company</code>.</p>
	 *
	 * @return a {@link ru.extas.model.Company} object.
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * <p>Setter for the field <code>company</code>.</p>
	 *
	 * @param company a {@link ru.extas.model.Company} object.
	 */
	public void setCompany(final Company company) {
		this.company = company;
	}
}
