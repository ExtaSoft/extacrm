/**
 * 
 */
package ru.extas.model;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.joda.time.LocalDate;

import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * Данные приема передачи форм строгой отчетности (БСО)
 * 
 * @author Valery Orlov
 * 
 */
@PersistenceCapable(detachable = "true")
public class FormTransfer extends AbstractExtaObject {

	private static final long serialVersionUID = -3750723587703870668L;

	/**
	 * Контакт от которого принимаются бланки
	 */
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Contact fromContact;

	/**
	 * Контакт которому передются бланки
	 */
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Contact toContact;

	/**
	 * Дата прередачи бланков
	 */
	@Persistent
	private LocalDate transferDate;

	/**
	 * Список номеров передаваемых бланков
	 */
	@Persistent
	private List<String> formNums;

	/**
	 * @return the fromContact
	 */
	public Contact getFromContact() {
		return fromContact;
	}

	/**
	 * @param fromContact
	 *            the fromContact to set
	 */
	public void setFromContact(final Contact fromContact) {
		this.fromContact = fromContact;
	}

	/**
	 * @return the toContact
	 */
	public Contact getToContact() {
		return toContact;
	}

	/**
	 * @param toContact
	 *            the toContact to set
	 */
	public void setToContact(final Contact toContact) {
		this.toContact = toContact;
	}

	/**
	 * @return the transferDate
	 */
	public LocalDate getTransferDate() {
		return transferDate;
	}

	/**
	 * @param transferDate
	 *            the transferDate to set
	 */
	public void setTransferDate(final LocalDate transferDate) {
		this.transferDate = transferDate;
	}

	/**
	 * @return the formNums
	 */
	public List<String> getFormNums() {
		return formNums;
	}

	/**
	 * @param formNums
	 *            the formNums to set
	 */
	public void setFormNums(final List<String> formNums) {
		this.formNums = formNums;
	}

}
