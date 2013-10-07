package ru.extas.model;

import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.Max;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 */
@Entity
@DiscriminatorValue("PERSON")
@Table(name = "PERSON")
public class Person extends Contact {

    private static final long serialVersionUID = -7891940552175345858L;

    // Дата рождения
    private LocalDate birthday;

    // Пол
    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Sex sex;

    // Должность
    @Enumerated(EnumType.STRING)
    @Column(name = "JOB_POSITION", length = 15)
    private Position jobPosition;

    // Департамент
    @Column(name = "JOB_DEPARTMENT", length = 50)
    @Max(50)
    private String jobDepartment;

    // Паспортные данные:
    // номер
    @Column(name = "PASS_NUM", length = 30)
    @Max(30)
    private String passNum;

    // дата выдачи
    @Column(name = "PASS_ISSUE_DATE")
    private LocalDate passIssueDate;

    // кем выдан
    @Column(name = "PASS_ISSUED_BY", length = 255)
    @Max(255)
    private String passIssuedBy;

    // код подразделения
    @Column(name = "PASS_ISSUED_BY_NUM", length = 10)
    @Max(10)
    private String passIssuedByNum;

    public Person() {
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