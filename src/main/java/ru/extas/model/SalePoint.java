package ru.extas.model;

import javax.persistence.*;
import java.util.List;

/**
 * Модель данных "Точка продаж"
 *
 * @author Valery Orlov
 *         Date: 10.02.14
 *         Time: 15:24
 */
@Entity
@DiscriminatorValue("SALEPOINT")
@Table(name = "SALE_POINT")
public class SalePoint extends Contact implements Cloneable {

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
			name = "SALEPOINT_EMPLOYEE",
			joinColumns = {@JoinColumn(name = "SALEPOINT_ID", referencedColumnName = "ID")},
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

	public List<LegalEntity> getLegalEntities() {
		return legalEntities;
	}

	public void setLegalEntities(final List<LegalEntity> legalEntities) {
		this.legalEntities = legalEntities;
	}

	public List<Person> getEmployes() {
		return employes;
	}

	public void setEmployes(final List<Person> employes) {
		this.employes = employes;
	}

	public String getExtaCode() {
		return extaCode;
	}

	public void setExtaCode(final String extaCode) {
		this.extaCode = extaCode;
	}

	public String getAlphaCode() {
		return alphaCode;
	}

	public void setAlphaCode(final String alphaCode) {
		this.alphaCode = alphaCode;
	}

	public String getHomeCode() {
		return homeCode;
	}

	public void setHomeCode(final String homeCode) {
		this.homeCode = homeCode;
	}

	public String getSetelemCode() {
		return setelemCode;
	}

	public void setSetelemCode(final String setelemCode) {
		this.setelemCode = setelemCode;
	}
}
