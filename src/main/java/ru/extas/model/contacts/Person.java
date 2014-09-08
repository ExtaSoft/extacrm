package ru.extas.model.contacts;

import org.joda.time.LocalDate;
import ru.extas.model.common.FileContainer;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Данные контакта - физ. лица
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@DiscriminatorValue("PERSON")
@Table(name = "PERSON")
public class Person extends Contact {

    private static final long serialVersionUID = -7891940552175345858L;

    // Дата рождения
    private LocalDate birthday;

    @Column(name = "BIRTH_PLACE", length = 60)
    @Max(60)
    private String birthPlace;

    @Column(length = 60)
    @Max(60)
    private String citizenship;

    @Column(name = "IS_CHANGE_NAME")
    private boolean changeName;

    @Column(name = "EX_NAME", length = 25)
    private String exName;

    @Column(name = "CHANGE_NAME_DATE")
    private LocalDate changeNameDate;

    // Пол
    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Sex sex;

    // Домашний телефон
    // Телефон
    @Column(name = "HOME_PHONE", length = PHONE_LINGHT)
    @Max(PHONE_LINGHT)
    private String homePhone;

    // Рабочий телефон
    // Телефон
    @Column(name = "WORK_PHONE", length = PHONE_LINGHT)
    @Max(PHONE_LINGHT)
    private String workPhone;

    @Column(name = "REG_N_ACT_IS_SAME")
    private boolean regNactIsSame;

    @Embedded()
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "ACT_REGION")),
            @AttributeOverride(name = "city", column = @Column(name = "ACT_CITY")),
            @AttributeOverride(name = "postIndex", column = @Column(name = "ACT_POST_INDEX")),
            @AttributeOverride(name = "streetBld", column = @Column(name = "ACT_STREET_BLD")),
            @AttributeOverride(name = "realtyKind", column = @Column(name = "ACT_REALTY_KIND")),
            @AttributeOverride(name = "periodOfResidence", column = @Column(name = "ACT_PERIOD_OF_RESIDENCE"))
    })
    private AddressInfo actualAddress;

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

    @ManyToMany(mappedBy = "employees", cascade = CascadeType.REFRESH)
    private Set<Company> employers;

    @ManyToMany(mappedBy = "employees", cascade = CascadeType.REFRESH)
    private Set<SalePoint> workPlaces;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = FileContainer.OWNER_ID_COLUMN)
    private List<PersonFileContainer> files = newArrayList();

    @Column(name = "DL_NUM", length = 30)
    @Max(30)
    private String dlNum;

    @Column(name = "DL_ISSUE_DATE")
    private LocalDate dlIssueDate;

    @Column(name = "DL_ISSUED_BY", length = 55)
    @Max(55)
    private String dlIssuedBy;

    @Column(name = "PERIOD_OF_DRIVING")
    private int periodOfDriving;

    @Column(name = "DRIVING_CATEGORIES")
    private String drivingCategories;

    /**
     * <p>Constructor for Person.</p>
     */
    public Person() {
    }

    /**
     * <p>Getter for the field <code>homePhone</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHomePhone() {
        return homePhone;
    }

    /**
     * <p>Setter for the field <code>homePhone</code>.</p>
     *
     * @param homePhone a {@link java.lang.String} object.
     */
    public void setHomePhone(final String homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * <p>Getter for the field <code>workPhone</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWorkPhone() {
        return workPhone;
    }

    /**
     * <p>Setter for the field <code>workPhone</code>.</p>
     *
     * @param workPhone a {@link java.lang.String} object.
     */
    public void setWorkPhone(final String workPhone) {
        this.workPhone = workPhone;
    }

    /**
     * <p>Getter for the field <code>jobPosition</code>.</p>
     *
     * @return a {@link ru.extas.model.contacts.Person.Position} object.
     */
    public Position getJobPosition() {
        return jobPosition;
    }

    /**
     * <p>Setter for the field <code>jobPosition</code>.</p>
     *
     * @param jobPosition a {@link ru.extas.model.contacts.Person.Position} object.
     */
    public void setJobPosition(final Position jobPosition) {
        this.jobPosition = jobPosition;
    }

    /**
     * <p>Getter for the field <code>jobDepartment</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getJobDepartment() {
        return jobDepartment;
    }

    /**
     * <p>Setter for the field <code>jobDepartment</code>.</p>
     *
     * @param jobDepartment a {@link java.lang.String} object.
     */
    public void setJobDepartment(final String jobDepartment) {
        this.jobDepartment = jobDepartment;
    }

    /**
     * <p>Getter for the field <code>passNum</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPassNum() {
        return passNum;
    }

    /**
     * <p>Setter for the field <code>passNum</code>.</p>
     *
     * @param passNum a {@link java.lang.String} object.
     */
    public void setPassNum(final String passNum) {
        this.passNum = passNum;
    }

    /**
     * <p>Getter for the field <code>passIssueDate</code>.</p>
     *
     * @return a {@link org.joda.time.LocalDate} object.
     */
    public LocalDate getPassIssueDate() {
        return passIssueDate;
    }

    /**
     * <p>Setter for the field <code>passIssueDate</code>.</p>
     *
     * @param passIssueDate a {@link org.joda.time.LocalDate} object.
     */
    public void setPassIssueDate(final LocalDate passIssueDate) {
        this.passIssueDate = passIssueDate;
    }

    /**
     * <p>Getter for the field <code>passIssuedBy</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPassIssuedBy() {
        return passIssuedBy;
    }

    /**
     * <p>Setter for the field <code>passIssuedBy</code>.</p>
     *
     * @param passIssuedBy a {@link java.lang.String} object.
     */
    public void setPassIssuedBy(final String passIssuedBy) {
        this.passIssuedBy = passIssuedBy;
    }

    /**
     * <p>Getter for the field <code>passIssuedByNum</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPassIssuedByNum() {
        return passIssuedByNum;
    }

    /**
     * <p>Setter for the field <code>passIssuedByNum</code>.</p>
     *
     * @param passIssuedByNum a {@link java.lang.String} object.
     */
    public void setPassIssuedByNum(final String passIssuedByNum) {
        this.passIssuedByNum = passIssuedByNum;
    }

    /**
     * <p>Getter for the field <code>birthday</code>.</p>
     *
     * @return a {@link org.joda.time.LocalDate} object.
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * <p>Setter for the field <code>birthday</code>.</p>
     *
     * @param birthday a {@link org.joda.time.LocalDate} object.
     */
    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * <p>Getter for the field <code>sex</code>.</p>
     *
     * @return a {@link ru.extas.model.contacts.Person.Sex} object.
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * <p>Setter for the field <code>sex</code>.</p>
     *
     * @param sex a {@link ru.extas.model.contacts.Person.Sex} object.
     */
    public void setSex(final Sex sex) {
        this.sex = sex;
    }

    public enum Sex {
        MALE, FEMALE
    }

    public enum Position {
        EMPLOYEE, DIRECTOR, ACCOUNTANT
    }

    /**
     * <p>Getter for the field <code>employers</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<Company> getEmployers() {
        return employers;
    }

    /**
     * <p>Setter for the field <code>employers</code>.</p>
     *
     * @param employers a {@link java.util.Set} object.
     */
    public void setEmployers(Set<Company> employers) {
        this.employers = employers;
    }

    /**
     * <p>Getter for the field <code>workPlaces</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<SalePoint> getWorkPlaces() {
        return workPlaces;
    }

    /**
     * <p>Setter for the field <code>workPlaces</code>.</p>
     *
     * @param workPlaces a {@link java.util.Set} object.
     */
    public void setWorkPlaces(Set<SalePoint> workPlaces) {
        this.workPlaces = workPlaces;
    }

    /**
     * <p>Getter for the field <code>files</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<PersonFileContainer> getFiles() {
        return files;
    }

    /**
     * <p>Setter for the field <code>files</code>.</p>
     *
     * @param files a {@link java.util.List} object.
     */
    public void setFiles(List<PersonFileContainer> files) {
        this.files = files;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public boolean isChangeName() {
        return changeName;
    }

    public void setChangeName(boolean changeName) {
        this.changeName = changeName;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public LocalDate getChangeNameDate() {
        return changeNameDate;
    }

    public void setChangeNameDate(LocalDate changeNameDate) {
        this.changeNameDate = changeNameDate;
    }

    public AddressInfo getActualAddress() {
        return actualAddress;
    }

    public void setActualAddress(AddressInfo actualAddress) {
        this.actualAddress = actualAddress;
    }

    public boolean isRegNactIsSame() {
        return regNactIsSame;
    }

    public void setRegNactIsSame(boolean regNactIsSame) {
        this.regNactIsSame = regNactIsSame;
    }

    public String getDlNum() {
        return dlNum;
    }

    public void setDlNum(String dlNum) {
        this.dlNum = dlNum;
    }

    public LocalDate getDlIssueDate() {
        return dlIssueDate;
    }

    public void setDlIssueDate(LocalDate dlIssueDate) {
        this.dlIssueDate = dlIssueDate;
    }

    public String getDlIssuedBy() {
        return dlIssuedBy;
    }

    public void setDlIssuedBy(String dlIssuedBy) {
        this.dlIssuedBy = dlIssuedBy;
    }

    public int getPeriodOfDriving() {
        return periodOfDriving;
    }

    public void setPeriodOfDriving(int periodOfDriving) {
        this.periodOfDriving = periodOfDriving;
    }

    public String getDrivingCategories() {
        return drivingCategories;
    }

    public void setDrivingCategories(String drivingCategories) {
        this.drivingCategories = drivingCategories;
    }
}
