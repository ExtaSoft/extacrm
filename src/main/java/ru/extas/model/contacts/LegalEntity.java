package ru.extas.model.contacts;

import ru.extas.model.sale.ProdCredit;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Модель данных для юридического лица
 *
 * @author Valery Orlov
 *         Date: 10.02.14
 *         Time: 16:44
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@DiscriminatorValue("LEGAL_ENTITY")
@Table(name = "LEGAL_ENTITY")
public class LegalEntity extends Contact implements Cloneable {

	// Компания
	@ManyToOne(optional = false)
	private Company company;

	// ОГРН/ОГРИП
	@Column(name = "OGRN_OGRIP", length = 15)
	private String ogrnOgrip;

	// ИНН
	@Column(name = "INN", length = 15)
	private String inn;

	// Генеральный директор
	@OneToOne
	private Person director;

	// Банки и кредитные продукты
	@ManyToMany
	@JoinTable(
			name = "LEGAL_ENTITY_PROD_CREDIT",
			joinColumns = {@JoinColumn(name = "LEGAL_ENTITY_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "PROD_CREDIT_ID", referencedColumnName = "ID")})
	private List<ProdCredit> credProducts;

	// Дилерство
	@ElementCollection
	@CollectionTable(name = "LEGAL_ENTITY_MOTOR_BRAND")
	private Set<String> motorBrands;

	/**
	 * <p>Getter for the field <code>ogrnOgrip</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOgrnOgrip() {
		return ogrnOgrip;
	}

	/**
	 * <p>Setter for the field <code>ogrnOgrip</code>.</p>
	 *
	 * @param ogrnOgrip a {@link java.lang.String} object.
	 */
	public void setOgrnOgrip(final String ogrnOgrip) {
		this.ogrnOgrip = ogrnOgrip;
	}

	/**
	 * <p>Getter for the field <code>inn</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getInn() {
		return inn;
	}

	/**
	 * <p>Setter for the field <code>inn</code>.</p>
	 *
	 * @param inn a {@link java.lang.String} object.
	 */
	public void setInn(final String inn) {
		this.inn = inn;
	}

	/**
	 * <p>Getter for the field <code>director</code>.</p>
	 *
	 * @return a {@link Person} object.
	 */
	public Person getDirector() {
		return director;
	}

	/**
	 * <p>Setter for the field <code>director</code>.</p>
	 *
	 * @param director a {@link Person} object.
	 */
	public void setDirector(final Person director) {
		this.director = director;
	}


	/**
	 * <p>Getter for the field <code>credProducts</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ProdCredit> getCredProducts() {
		return credProducts;
	}

	/**
	 * <p>Setter for the field <code>credProducts</code>.</p>
	 *
	 * @param credProducts a {@link java.util.List} object.
	 */
	public void setCredProducts(final List<ProdCredit> credProducts) {
		this.credProducts = credProducts;
	}

	/**
	 * <p>Getter for the field <code>motorBrands</code>.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<String> getMotorBrands() {
		return motorBrands;
	}

	/**
	 * <p>Setter for the field <code>motorBrands</code>.</p>
	 *
	 * @param motorBrands a {@link java.util.Set} object.
	 */
	public void setMotorBrands(final Set<String> motorBrands) {
		this.motorBrands = motorBrands;
	}

	/**
	 * <p>Getter for the field <code>company</code>.</p>
	 *
	 * @return a {@link Company} object.
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * <p>Setter for the field <code>company</code>.</p>
	 *
	 * @param company a {@link Company} object.
	 */
	public void setCompany(final Company company) {
		this.company = company;
	}
}
