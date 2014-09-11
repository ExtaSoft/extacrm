package ru.extas.model.contacts;

import org.joda.time.LocalDate;
import ru.extas.model.common.FileContainer;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
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
    public static final int BIRTH_PLACE_LENGTH = 60;
    public static final int CITIZENSHIP_LENGTH = 60;
    public static final int INN_LENGTH = 10;
    public static final int SCOPE_OF_ACTIVITY_LENGHT = 30;

    // Дата рождения
    private LocalDate birthday;

    @Column(name = "BIRTH_PLACE", length = BIRTH_PLACE_LENGTH)
    @Max(BIRTH_PLACE_LENGTH)
    private String birthPlace;

    @Column(length = CITIZENSHIP_LENGTH)
    @Max(CITIZENSHIP_LENGTH)
    private String citizenship;

    @Column(name = "IS_CHANGE_NAME")
    private boolean changeName;

    @Column(name = "EX_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
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
    private AddressInfo actualAddress = new AddressInfo();

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

    @Enumerated(EnumType.STRING)
    @Column(name = "MARITAL_STATUS", length = 15)
    private MaritalStatus maritalStatus;

    @Column(name = "HAS_MARRIAGE_СONTRACT")
    private boolean marriageСontract;

    @OneToMany(mappedBy = "parent", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonChild> children = newArrayList();

    @Column(name = "LIVING_TOGETHER")
    private int livingTogether;

    private int dependants;

    @Column(name = "SPOUSE_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String spouseName;

    @Column(name = "SPOUSE_BIRTHDAY")
    private LocalDate spouseBirthday;

    @Column(name = "SPOUSE_BIRTH_PLACE", length = BIRTH_PLACE_LENGTH)
    @Max(BIRTH_PLACE_LENGTH)
    private String spouseBirthPlace;

    @Column(length = CITIZENSHIP_LENGTH)
    @Max(CITIZENSHIP_LENGTH)
    private String spouseCitizenship;

    @Enumerated(EnumType.STRING)
    @Column(name = "EDU_KIND", length = 18)
    private EducationKind eduKind;

    @Column(name = "EDU_FINISH")
    private Integer eduFinish;

    @Column(name = "EDU_INST_NAME", length = 50)
    @Max(50)
    private String eduInstName;

    @Column(name = "EDU_INST_INN", length = INN_LENGTH)
    @Max(INN_LENGTH)
    private String eduInstINN;

    @Column(name = "SPECIALITY", length = 30)
    @Max(30)
    private String speciality;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_OF_EMPLOYMENT", length = 12)
    private TypeOfEmployment typeOfEmployment;

    @Column(name = "TEMP_JOB_PERIOD")
    private int tempJobPeriod;

    @Column(name = "PRACTICE_TYPE", length = 50)
    @Max(50)
    private String practiceType;

    @Column(name = "EMPLOYER_SCOPE", length = SCOPE_OF_ACTIVITY_LENGHT)
    @Max(SCOPE_OF_ACTIVITY_LENGHT)
    private String employerScope;

    @Column(name = "EMPLOYER_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String employerName;

    @Column(name = "EMPLOYER_INN", length = INN_LENGTH)
    @Max(INN_LENGTH)
    private String employerINN;

    @Column(name = "EMPLOYER_PHONE", length = PHONE_LINGHT)
    @Max(PHONE_LINGHT)
    private String employerPhone;

    @Column(name = "OFFICE_POSITION", length = 30)
    @Max(30)
    private String officePosition;

    @Column(name = "EMPLOYER_ADRESS")
    private String employerAdress;

    @Column(name = "EMPLOYER_WWW")
    private String employerWww;

    @Column(name = "EMPLOYER_DIRECTOR_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String employerDirectorName;

    @Column(name = "EMPLOYER_ACCOUNTANT_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String employerAccountantName;

    @Column(precision = 32, scale = 4)
    private BigDecimal salary;

    @Column(name = "LAST_EXPERIENCE")
    private int lastExperience;

    @Column(name = "GEN_EXPERIENCE")
    private int genExperience;

    @Column(name = "JOBS_FOR_3YEARS")
    private int jobsFor3years;

    @Column(name = "IS_BUSINESS_OWNER")
    private boolean businessOwner;

    @Column(name = "BUSINESS_SCOPE", length = SCOPE_OF_ACTIVITY_LENGHT)
    @Max(SCOPE_OF_ACTIVITY_LENGHT)
    private String businessScope;

    @Column(name = "BUSINESS_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String businessName;

    @Column(name = "BUSINESS_INN", length = INN_LENGTH)
    @Max(INN_LENGTH)
    private String businessINN;

    @Column(name = "BUSINESS_PHONE", length = PHONE_LINGHT)
    @Max(PHONE_LINGHT)
    private String businessPhone;

    @Column(name = "BUSINESS_ADRESS")
    private String businessAdress;

    @Column(name = "BUSINESS_PART", precision = 32, scale = 4)
    private BigDecimal businessPart;

    @Column(name = "BUSINESS_MEMB_EMP")
    private int businessMumbEmp;

    @Column(name = "BUSINESS_BALANCE", precision = 32, scale = 4)
    private BigDecimal businessBalance;

    @Column(name = "BUSINESS_YEARLY_SALES", precision = 32, scale = 4)
    private BigDecimal businessYearlySales;


    @Column(name = "HAS_ANOTHER_CREDIT")
    private boolean anotherCredit;

    @Column(name = "ANOTHER_CREDIT_BANK", length = 50)
    @Max(50)
    private String anotherCreditBank;

    @Column(name = "MARKETING_CHANNAL", length = 50)
    @Max(50)
    private String marketingChannel;

    @Column(name = "ACCOUNTING_4_PSYCHIATRIST")
    private boolean accounting4Psychiatrist;

    @Column(name = "HAS_CRIMINAL_LIABILITY")
    private boolean criminalLiability;

    @Column(name = "HAS_COLLATERAL_PROPERTY")
    private boolean collateralProperty;

    private boolean receivership;


    @Column(name = "CLOSE_RELATIVE_NAME", length = NAME_LENGTH)
    @Max(NAME_LENGTH)
    private String closeRelativeName;

    @Column(name = "CLOSE_RELATIVE_FILIATION", length = 15)
    @Max(15)
    private String closeRelativeFiliation;

    @Column(name = "CLOSE_RELATIVE_MOB_PHONE", length = PHONE_LINGHT)
    @Max(PHONE_LINGHT)
    private String closeRelativeMobPhone;

    @Column(name = "CLOSE_RELATIVE_HOME_PHONE", length = PHONE_LINGHT)
    @Max(PHONE_LINGHT)
    private String closeRelativeHomePhone;

    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonAuto> autos = newArrayList();

    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonRealty> realties = newArrayList();

    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonIncome> incomes = newArrayList();

    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PersonExpense> expenses = newArrayList();


    /**
     * <p>Constructor for Person.</p>
     */
    public Person() {
    }

    public List<PersonAuto> getAutos() {
        return autos;
    }

    public void setAutos(List<PersonAuto> autos) {
        this.autos = autos;
    }

    public List<PersonRealty> getRealties() {
        return realties;
    }

    public void setRealties(List<PersonRealty> realties) {
        this.realties = realties;
    }

    public List<PersonIncome> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<PersonIncome> incomes) {
        this.incomes = incomes;
    }

    public List<PersonExpense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<PersonExpense> expenses) {
        this.expenses = expenses;
    }

    public String getCloseRelativeName() {
        return closeRelativeName;
    }

    public void setCloseRelativeName(String closeRelativeName) {
        this.closeRelativeName = closeRelativeName;
    }

    public String getCloseRelativeFiliation() {
        return closeRelativeFiliation;
    }

    public void setCloseRelativeFiliation(String closeRelativeFiliation) {
        this.closeRelativeFiliation = closeRelativeFiliation;
    }

    public String getCloseRelativeMobPhone() {
        return closeRelativeMobPhone;
    }

    public void setCloseRelativeMobPhone(String closeRelativeMobPhone) {
        this.closeRelativeMobPhone = closeRelativeMobPhone;
    }

    public String getCloseRelativeHomePhone() {
        return closeRelativeHomePhone;
    }

    public void setCloseRelativeHomePhone(String closeRelativeHomePhone) {
        this.closeRelativeHomePhone = closeRelativeHomePhone;
    }

    public String getMarketingChannel() {
        return marketingChannel;
    }

    public void setMarketingChannel(String marketingChannel) {
        this.marketingChannel = marketingChannel;
    }

    public boolean isAccounting4Psychiatrist() {
        return accounting4Psychiatrist;
    }

    public void setAccounting4Psychiatrist(boolean accounting4Psychiatrist) {
        this.accounting4Psychiatrist = accounting4Psychiatrist;
    }

    public boolean isCriminalLiability() {
        return criminalLiability;
    }

    public void setCriminalLiability(boolean criminalLiability) {
        this.criminalLiability = criminalLiability;
    }

    public boolean isCollateralProperty() {
        return collateralProperty;
    }

    public void setCollateralProperty(boolean collateralProperty) {
        this.collateralProperty = collateralProperty;
    }

    public boolean isReceivership() {
        return receivership;
    }

    public void setReceivership(boolean receivership) {
        this.receivership = receivership;
    }

    public boolean isAnotherCredit() {
        return anotherCredit;
    }

    public void setAnotherCredit(boolean anotherCredit) {
        this.anotherCredit = anotherCredit;
    }

    public String getAnotherCreditBank() {
        return anotherCreditBank;
    }

    public void setAnotherCreditBank(String anotherCreditBank) {
        this.anotherCreditBank = anotherCreditBank;
    }

    public boolean isBusinessOwner() {
        return businessOwner;
    }

    public void setBusinessOwner(boolean businessOwner) {
        this.businessOwner = businessOwner;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessINN() {
        return businessINN;
    }

    public void setBusinessINN(String businessINN) {
        this.businessINN = businessINN;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessAdress() {
        return businessAdress;
    }

    public void setBusinessAdress(String businessAdress) {
        this.businessAdress = businessAdress;
    }

    public BigDecimal getBusinessPart() {
        return businessPart;
    }

    public void setBusinessPart(BigDecimal businessPart) {
        this.businessPart = businessPart;
    }

    public int getBusinessMumbEmp() {
        return businessMumbEmp;
    }

    public void setBusinessMumbEmp(int businessMumbEmp) {
        this.businessMumbEmp = businessMumbEmp;
    }

    public BigDecimal getBusinessBalance() {
        return businessBalance;
    }

    public void setBusinessBalance(BigDecimal businessBalance) {
        this.businessBalance = businessBalance;
    }

    public BigDecimal getBusinessYearlySales() {
        return businessYearlySales;
    }

    public void setBusinessYearlySales(BigDecimal businessYearlySales) {
        this.businessYearlySales = businessYearlySales;
    }

    public String getOfficePosition() {
        return officePosition;
    }

    public void setOfficePosition(String officePosition) {
        this.officePosition = officePosition;
    }

    public TypeOfEmployment getTypeOfEmployment() {
        return typeOfEmployment;
    }

    public void setTypeOfEmployment(TypeOfEmployment typeOfEmployment) {
        this.typeOfEmployment = typeOfEmployment;
    }

    public int getTempJobPeriod() {
        return tempJobPeriod;
    }

    public void setTempJobPeriod(int tempJobPeriod) {
        this.tempJobPeriod = tempJobPeriod;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public void setPracticeType(String practiceType) {
        this.practiceType = practiceType;
    }

    public String getEmployerScope() {
        return employerScope;
    }

    public void setEmployerScope(String employerScope) {
        this.employerScope = employerScope;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerINN() {
        return employerINN;
    }

    public void setEmployerINN(String employerINN) {
        this.employerINN = employerINN;
    }

    public String getEmployerPhone() {
        return employerPhone;
    }

    public void setEmployerPhone(String employerPhone) {
        this.employerPhone = employerPhone;
    }

    public String getEmployerAdress() {
        return employerAdress;
    }

    public void setEmployerAdress(String employerAdress) {
        this.employerAdress = employerAdress;
    }

    public String getEmployerWww() {
        return employerWww;
    }

    public void setEmployerWww(String employerWww) {
        this.employerWww = employerWww;
    }

    public String getEmployerDirectorName() {
        return employerDirectorName;
    }

    public void setEmployerDirectorName(String employerDirectorName) {
        this.employerDirectorName = employerDirectorName;
    }

    public String getEmployerAccountantName() {
        return employerAccountantName;
    }

    public void setEmployerAccountantName(String employerAccountantName) {
        this.employerAccountantName = employerAccountantName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public int getLastExperience() {
        return lastExperience;
    }

    public void setLastExperience(int lastExperience) {
        this.lastExperience = lastExperience;
    }

    public int getGenExperience() {
        return genExperience;
    }

    public void setGenExperience(int genExperience) {
        this.genExperience = genExperience;
    }

    public int getJobsFor3years() {
        return jobsFor3years;
    }

    public void setJobsFor3years(int jobsFor3years) {
        this.jobsFor3years = jobsFor3years;
    }

    public EducationKind getEduKind() {
        return eduKind;
    }

    public void setEduKind(EducationKind eduKind) {
        this.eduKind = eduKind;
    }

    public Integer getEduFinish() {
        return eduFinish;
    }

    public void setEduFinish(Integer eduFinish) {
        this.eduFinish = eduFinish;
    }

    public String getEduInstName() {
        return eduInstName;
    }

    public void setEduInstName(String eduInstName) {
        this.eduInstName = eduInstName;
    }

    public String getEduInstINN() {
        return eduInstINN;
    }

    public void setEduInstINN(String eduInstINN) {
        this.eduInstINN = eduInstINN;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public boolean isMarriageСontract() {
        return marriageСontract;
    }

    public void setMarriageСontract(boolean marriageСontract) {
        this.marriageСontract = marriageСontract;
    }

    public List<PersonChild> getChildren() {
        return children;
    }

    public void setChildren(List<PersonChild> personChildren) {
        this.children = personChildren;
    }

    public int getLivingTogether() {
        return livingTogether;
    }

    public void setLivingTogether(int livingTogether) {
        this.livingTogether = livingTogether;
    }

    public int getDependants() {
        return dependants;
    }

    public void setDependants(int dependants) {
        this.dependants = dependants;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public LocalDate getSpouseBirthday() {
        return spouseBirthday;
    }

    public void setSpouseBirthday(LocalDate spouseBirthday) {
        this.spouseBirthday = spouseBirthday;
    }

    public String getSpouseBirthPlace() {
        return spouseBirthPlace;
    }

    public void setSpouseBirthPlace(String spouseBirthPlace) {
        this.spouseBirthPlace = spouseBirthPlace;
    }

    public String getSpouseCitizenship() {
        return spouseCitizenship;
    }

    public void setSpouseCitizenship(String spouseCitizenship) {
        this.spouseCitizenship = spouseCitizenship;
    }
}
