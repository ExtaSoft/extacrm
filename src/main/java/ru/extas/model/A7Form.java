/**
 *
 */
package ru.extas.model;

import com.google.appengine.datanucleus.annotations.Unowned;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Данные о квитанции форма № А-7
 *
 * @author Valery Orlov
 */
@PersistenceCapable(detachable = "true")
public class A7Form extends AbstractExtaObject {

    private static final long serialVersionUID = -4643812782207400426L;

    /**
     * Статусы формы А-7
     *
     * @author Valery Orlov
     */
    public enum Status {
        /**
         * Новый бланк
         */
        NEW,
        /**
         * Использованный бланк
         */
        SPENT,
        /**
         * Потерянный бланк
         */
        LOST,
        /**
         * Испорченный бланк
         */
        BROKEN
    }

    /**
     * Номер квитанции
     */
    @Persistent
    private String regNum;

    /**
     * Статус квитанции
     */
    @Persistent
    private Status status = Status.NEW;

    /**
     * Владелец квитанции
     */
    @Unowned
    @Persistent(defaultFetchGroup = "true")
    private Contact owner;

    /**
     * Создает новую запись о бланке
     *
     * @param regNum Номер бланка
     * @param owner  Владелец бланка
     */
    public A7Form(final String regNum, final Contact owner) {
        super();
        this.regNum = regNum;
        this.owner = owner;
    }

    public A7Form() {
    }

    /**
     * @return the regNum
     */
    public String getRegNum() {
        return regNum;
    }

    /**
     * @param regNum the regNum to set
     */
    public void setRegNum(final String regNum) {
        this.regNum = regNum;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final Status status) {
        this.status = status;
    }

    /**
     * @return the owner
     */
    public Contact getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(final Contact owner) {
        this.owner = owner;
    }

}
