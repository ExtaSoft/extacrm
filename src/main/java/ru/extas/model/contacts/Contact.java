/**
 *
 */
package ru.extas.model.contacts;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import ru.extas.model.security.SecuredObject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Общие данные для контактов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@MappedSuperclass
public abstract class Contact extends SecuredObject{

	private static final long serialVersionUID = -2543373135823969745L;

    /** Constant <code>NAME_LENGTH=50</code> */
	public static final int NAME_LENGTH = 50;
	/** Constant <code>PHONE_LINGHT=20</code> */
	public static final int PHONE_LINGHT = 20;
    public static final int EMAIL_LENGTH = 50;
    public static final int WWW_LENGTH = 50;

	// Имя контакта
	@Column(length = NAME_LENGTH)
    @Size(max = NAME_LENGTH)
	@NotNull
	private String name;

	// Телефон
	@Column(name = "CELL_PHONE", length = PHONE_LINGHT)
    @Size(max = PHONE_LINGHT)
	private String phone;

	// Эл. почта
	@Column(length = EMAIL_LENGTH)
	@Size(max = EMAIL_LENGTH)
    @Email
	private String email;

	// Сайт
	@Column(length = WWW_LENGTH)
	@Size(max = WWW_LENGTH)
    @URL
	private String www;

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * <p>Getter for the field <code>phone</code>.</p>
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * <p>Setter for the field <code>phone</code>.</p>
	 *
	 * @param phone the phone to set
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * <p>Getter for the field <code>email</code>.</p>
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <p>Setter for the field <code>email</code>.</p>
	 *
	 * @param email the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * <p>Getter for the field <code>www</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getWww() {
		return www;
	}

	/**
	 * <p>Setter for the field <code>www</code>.</p>
	 *
	 * @param www a {@link java.lang.String} object.
	 */
	public void setWww(final String www) {
		this.www = www;
	}
}
