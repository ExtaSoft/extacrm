/**
 *
 */
package ru.extas.model;

import org.joda.time.DateTime;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Полис страхования в БСО
 *
 * @author Valery Orlov
 */
@PersistenceCapable(detachable = "true")
public class Policy extends AbstractExtaObject {

    private static final long serialVersionUID = 3160576591591414719L;

    // Номер полиса
    @Persistent
    private String regNum;

    // Время бронирования полиса
    @Persistent
    private DateTime bookTime;

    // Время реализации полиса
    @Persistent
    private DateTime issueDate;

    /**
     * @return the regNum
     */
    public String getRegNum() {
        return regNum;
    }

    /**
     * @param regNum the regNum to set
     */
    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    /**
     * @return the bookTime
     */
    public DateTime getBookTime() {
        return bookTime;
    }

    /**
     * @param bookTime the bookTime to set
     */
    public void setBookTime(DateTime bookTime) {
        this.bookTime = bookTime;
    }

    /**
     * @return the issueDate
     */
    public DateTime getIssueDate() {
        return issueDate;
    }

    /**
     * @param issueDate the issueDate to set
     */
    public void setIssueDate(DateTime issueDate) {
        this.issueDate = issueDate;
    }
}
