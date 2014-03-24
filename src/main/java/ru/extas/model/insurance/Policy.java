/**
 *
 */
package ru.extas.model.insurance;

import org.joda.time.DateTime;
import ru.extas.model.common.ChangeMarkedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Max;


/**
 * Полис страхования в БСО
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "POLICY",
        indexes = {
                @Index(columnList = "ISSUE_DATE, BOOK_TIME, REG_NUM")
        })
public class Policy extends ChangeMarkedObject {

    private static final long serialVersionUID = 3160576591591414719L;
    /** Constant <code>REG_NUM_LENGTH=20</code> */
    public static final int REG_NUM_LENGTH = 20;

    // Номер полиса
    @Column(name = "REG_NUM", length = REG_NUM_LENGTH, unique = true)
    @Max(REG_NUM_LENGTH)
    private String regNum;

    // Время бронирования полиса
    @Column(name = "BOOK_TIME")
    private DateTime bookTime;

    // Время реализации полиса
    @Column(name = "ISSUE_DATE")
    private DateTime issueDate;

    /**
     * <p>Getter for the field <code>regNum</code>.</p>
     *
     * @return the regNum
     */
    public String getRegNum() {
        return regNum;
    }

    /**
     * <p>Setter for the field <code>regNum</code>.</p>
     *
     * @param regNum the regNum to set
     */
    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    /**
     * <p>Getter for the field <code>bookTime</code>.</p>
     *
     * @return the bookTime
     */
    public DateTime getBookTime() {
        return bookTime;
    }

    /**
     * <p>Setter for the field <code>bookTime</code>.</p>
     *
     * @param bookTime the bookTime to set
     */
    public void setBookTime(DateTime bookTime) {
        this.bookTime = bookTime;
    }

    /**
     * <p>Getter for the field <code>issueDate</code>.</p>
     *
     * @return the issueDate
     */
    public DateTime getIssueDate() {
        return issueDate;
    }

    /**
     * <p>Setter for the field <code>issueDate</code>.</p>
     *
     * @param issueDate the issueDate to set
     */
    public void setIssueDate(DateTime issueDate) {
        this.issueDate = issueDate;
    }
}
