package ru.extas.model.contacts;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Модель данных "Точка продаж"
 *
 * @author Valery Orlov
 *         Date: 10.02.14
 *         Time: 15:24
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@DiscriminatorValue("SALEPOINT")
@Table(name = "SALE_POINT")
public class SalePoint extends Contact {

    private static final int CODE_LENGTH = 15;
    // Компания
	@ManyToOne(optional = false, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	private Company company;

	// Юр. лица работающие на торговой точке
	@ManyToMany(targetEntity = LegalEntity.class, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(
			name = "SALEPOINT_LEGALENTITY",
			joinColumns = {@JoinColumn(name = "SALEPOINT_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "LEGALENTITY_ID", referencedColumnName = "ID")})
	private Set<LegalEntity> legalEntities = newHashSet();

	// Сотрудники
	@ManyToMany(targetEntity = Person.class, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(
			name = "CONTACT_EMPLOYEE",
			joinColumns = {@JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "ID")})
	private Set<Person> employees = newHashSet();

	// Идентификация:

	//  - Код Экстрим Ассистанс
	@Column(name = "EXTA_CODE", length = CODE_LENGTH)
    @Size(max = CODE_LENGTH)
	private String extaCode;

	//  - Код Альфа Банка
	@Column(name = "ALPHA_CODE", length = CODE_LENGTH)
    @Size(max = CODE_LENGTH)
	private String alphaCode;

	//  - Код HomeCredit Банка
	@Column(name = "HOME_CODE", length = CODE_LENGTH)
    @Size(max = CODE_LENGTH)
	private String homeCode;

	//  - Код Банка СЕТЕЛЕМ
	@Column(name = "SETELEM_CODE", length = CODE_LENGTH)
    @Size(max = CODE_LENGTH)
	private String setelemCode;

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

	/**
	 * <p>Getter for the field <code>employes</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public Set<Person> getEmployees() {
		return employees;
	}

	/**
	 * <p>Setter for the field <code>employes</code>.</p>
	 *
	 * @param employes a {@link java.util.List} object.
	 */
	public void setEmployees(final Set<Person> employes) {
		this.employees = employes;
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
	 * @return a {@link ru.extas.model.contacts.Company} object.
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * <p>Setter for the field <code>company</code>.</p>
	 *
	 * @param company a {@link ru.extas.model.contacts.Company} object.
	 */
	public void setCompany(final Company company) {
		this.company = company;
	}
}
