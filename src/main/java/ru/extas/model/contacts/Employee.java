package ru.extas.model.contacts;

import org.joda.time.LocalDate;
import ru.extas.model.common.FileContainer;
import ru.extas.model.security.UserProfile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Сотрудник компании, торговой точки или юр. лица
 *
 * @author Valery Orlov
 *         Date: 16.10.2014
 *         Time: 21:42
 */
@Entity
@Table(name = "EMPLOYEE", indexes = {@Index(columnList = "NAME")})
public class Employee extends Contact {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    private Company company;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "SALE_POINT_ID", referencedColumnName = "ID")
    private SalePoint workPlace;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "LEGAL_ENTITY_ID", referencedColumnName = "ID")
    private LegalEntity legalWorkPlace;

    @OneToOne(mappedBy = "employee", cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private UserProfile userProfile;

    // Дата рождения
    private LocalDate birthday;

    @Column(name = "BIRTH_PLACE", length = Person.BIRTH_PLACE_LENGTH)
    @Size(max = Person.BIRTH_PLACE_LENGTH)
    private String birthPlace;

    // Домашний телефон
    // Телефон
    @Column(name = "HOME_PHONE", length = PHONE_LINGHT)
    @Size(max = PHONE_LINGHT)
    private String homePhone;

    // Рабочий телефон
    // Телефон
    @Column(name = "WORK_PHONE", length = PHONE_LINGHT)
    @Size(max = PHONE_LINGHT)
    private String workPhone;

    // Должность
    @Column(name = "JOB_POSITION", length = 50)
    @Size(max = 50)
    private String jobPosition;

    // Департамент
    @Column(name = "JOB_DEPARTMENT", length = 50)
    @Size(max = 50)
    private String jobDepartment;

    // Паспортные данные:
    // номер
    @Column(name = "PASS_NUM", length = 30)
    @Size(max = 30)
    private String passNum;

    // дата выдачи
    @Column(name = "PASS_ISSUE_DATE")
    private LocalDate passIssueDate;

    // кем выдан
    @Column(name = "PASS_ISSUED_BY", length = 255)
    @Size(max = 255)
    private String passIssuedBy;

    // код подразделения
    @Column(name = "PASS_ISSUED_BY_NUM", length = 10)
    @Size(max = 10)
    private String passIssuedByNum;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = FileContainer.OWNER_ID_COLUMN)
    @OrderBy("name ASC")
    private List<EmployeeFile> files = newArrayList();


    public List<EmployeeFile> getFiles() {
        return files;
    }

    public void setFiles(List<EmployeeFile> files) {
        this.files = files;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(final String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(final String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(final String workPhone) {
        this.workPhone = workPhone;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(final String jobPosition) {
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        this.company = company;
        if(company != null)
            company.getEmployees().add(this);
    }

    public SalePoint getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(final SalePoint workPlace) {
        this.workPlace = workPlace;
    }

    public LegalEntity getLegalWorkPlace() {
        return legalWorkPlace;
    }

    public void setLegalWorkPlace(final LegalEntity legalWorkPlace) {
        this.legalWorkPlace = legalWorkPlace;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(final UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
