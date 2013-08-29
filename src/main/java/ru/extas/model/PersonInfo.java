package ru.extas.model;

import org.joda.time.LocalDate;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.io.Serializable;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 */
@PersistenceCapable
@EmbeddedOnly
public class PersonInfo implements Serializable {

    // Дата рождения
    @Persistent
    private LocalDate birthday;
    // Пол
    @Persistent
    private Sex sex;
    // Должность
    @Persistent
    private Position jobPosition;
    // Департамент
    @Persistent
    private String jobDepartment;
    // Паспортные данные:
    // номер
    @Persistent
    private String passNum;
    // дата выдачи
    @Persistent
    private LocalDate passIssueDate;
    // кем выдан
    @Persistent
    private String passIssuedBy;
    // код подразделения
    @Persistent
    private String passIssuedByNum;


    public PersonInfo() {
    }

    public Position getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(final Position jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getJobDepartment() {
        return jobDepartment;
    }

    public void setJobDepartment(final String jobDepartment) {
        this.jobDepartment = jobDepartment;
    }

    public String getPassNum() {
        return passNum;
    }

    public void setPassNum(final String passNum) {
        this.passNum = passNum;
    }

    public LocalDate getPassIssueDate() {
        return passIssueDate;
    }

    public void setPassIssueDate(final LocalDate passIssueDate) {
        this.passIssueDate = passIssueDate;
    }

    public String getPassIssuedBy() {
        return passIssuedBy;
    }

    public void setPassIssuedBy(final String passIssuedBy) {
        this.passIssuedBy = passIssuedBy;
    }

    public String getPassIssuedByNum() {
        return passIssuedByNum;
    }

    public void setPassIssuedByNum(final String passIssuedByNum) {
        this.passIssuedByNum = passIssuedByNum;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(final Sex sex) {
        this.sex = sex;
    }

    public enum Sex {
        MALE, FEMALE
    }

    public enum Position {
        EMPLOYEE, DIRECTOR, ACCOUNTANT
    }
}