package ru.extas.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Модель данных для юридического лица
 *
 * @author Valery Orlov
 *         Date: 10.02.14
 *         Time: 16:44
 */
@Entity
@DiscriminatorValue("LEGAL_ENTITY")
@Table(name = "LEGAL_ENTITY")
public class LegalEntity extends Contact implements Cloneable {

	// ОГРН/ОГРИП
	@Column(name = "OGRN_OGRIP", length = 15)
	private String ogrnOgrip;

	// ИНН
	@Column(name = "INN", length = 15)
	private String inn;

	// Генеральный директор
	@OneToOne
	private Person director;

	// Афилированные лица
	@ManyToMany
	@JoinTable(
			name = "LEGAL_ENTITY_AFFILIATION",
			joinColumns = {@JoinColumn(name = "LEGAL_ENTITY_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "AFFILIATION_ID", referencedColumnName = "ID")})
	private List<Contact> affiliations;

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

	public String getOgrnOgrip() {
		return ogrnOgrip;
	}

	public void setOgrnOgrip(final String ogrnOgrip) {
		this.ogrnOgrip = ogrnOgrip;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(final String inn) {
		this.inn = inn;
	}

	public Person getDirector() {
		return director;
	}

	public void setDirector(final Person director) {
		this.director = director;
	}

	public List<Contact> getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(final List<Contact> affiliations) {
		this.affiliations = affiliations;
	}

	public List<ProdCredit> getCredProducts() {
		return credProducts;
	}

	public void setCredProducts(final List<ProdCredit> credProducts) {
		this.credProducts = credProducts;
	}

	public Set<String> getMotorBrands() {
		return motorBrands;
	}

	public void setMotorBrands(final Set<String> motorBrands) {
		this.motorBrands = motorBrands;
	}
}
