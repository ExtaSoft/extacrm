/**
 *
 */
package ru.extas.model;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Контактное лицо контрагента, клиент физик или сотрудник
 *
 * @author Valery Orlov
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "CONTACT", indexes = {
		@Index(columnList = "NAME"),
		@Index(columnList = "TYPE, NAME")
})
public abstract class Contact extends AbstractExtaObject implements Cloneable {

	private static final long serialVersionUID = -2543373135823969745L;

	public static final int NAME_LENGTH = 50;
	public static final int PHONE_LINGHT = 20;

	// Фактический адрес
	@Embedded
	private AddressInfo actualAddress;

	// Имя контакта
	@Column(length = NAME_LENGTH)
	@Max(NAME_LENGTH)
	@NotNull
	private String name;

	// Телефон
	@Column(name = "CELL_PHONE", length = PHONE_LINGHT)
	@Max(20)
	private String phone;

	// Эл. почта
	@Column(length = 50)
	@Max(50)
	private String email;

	// Сайт
	@Column(length = 50)
	@Max(50)
	private String www;

	// Вышестоящая организация
	@OneToOne
	private Company affiliation;

	protected void copyTo(Contact toObj) {
		if (actualAddress != null)
			toObj.actualAddress = actualAddress.clone();
		toObj.name = name;
		toObj.phone = phone;
		toObj.email = email;
		toObj.www = www;
		if (affiliation != null)
			toObj.affiliation = affiliation.clone();
	}

	public Company getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(final Company affiliation) {
		this.affiliation = affiliation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public void setActualAddress(final AddressInfo actualAddress) {
		this.actualAddress = actualAddress;
	}

	public AddressInfo getActualAddress() {
		return actualAddress;
	}

	public String getWww() {
		return www;
	}

	public void setWww(final String www) {
		this.www = www;
	}
}
