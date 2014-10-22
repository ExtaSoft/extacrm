package ru.extas.model.contacts;

import ru.extas.model.common.FileContainer;
import ru.extas.model.sale.ProdCredit;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Модель данных для юридического лица
 *
 * @author Valery Orlov
 *         Date: 10.02.14
 *         Time: 16:44
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "LEGAL_ENTITY", indexes = {@Index(columnList = "NAME")})
public class LegalEntity extends Contact{

    // Компания
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Company company;

    // ОГРН/ОГРИП
    @Column(name = "OGRN_OGRIP", length = 15)
    @Size(max = 15)
    private String ogrnOgrip;

    // ИНН
    @Column(name = "INN", length = 15)
    @Size(max = 15)
    private String inn;

    // КПП
    @Column(name = "KPP", length = 15)
    @Size(max = 15)
    private String kpp;

    // Расчетный счет в рублях
    @Column(name = "SETTLEMENT_ACCOUNT", length = 25)
    @Size(max = 25)
    private String settlementAccount;

    // Корреспондентский счет
    @Column(name = "LORO_ACCOUNT", length = 150)
    @Size(max = 150)
    private String loroAccount;

    // Полное наименование банка
    @Column(name = "BANK_NAME", length = NAME_LENGTH)
    @Size(max = NAME_LENGTH)
    private String bankName;

    // БИК банка
    @Column(name = "BIC", length = 15)
    @Size(max = 15)
    private String bic;

    // Фактический адрес совпадает с юридическим
    @Column(name = "REG_N_PST_IS_SAME")
    private boolean regNpstIsSame;

    // Почтовый адрес
    @Embedded()
    @AttributeOverrides({
            @AttributeOverride(name = "region", column = @Column(name = "PST_REGION")),
            @AttributeOverride(name = "city", column = @Column(name = "PST_CITY")),
            @AttributeOverride(name = "postIndex", column = @Column(name = "PST_POST_INDEX")),
            @AttributeOverride(name = "streetBld", column = @Column(name = "PST_STREET_BLD")),
            @AttributeOverride(name = "realtyKind", column = @Column(name = "PST_REALTY_KIND")),
            @AttributeOverride(name = "periodOfResidence", column = @Column(name = "PST_PERIOD_OF_RESIDENCE"))
    })
    @Valid
    private AddressInfo postAddress = new AddressInfo();

    // Генеральный директор
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Employee director;

    // Главный бухгалтер
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Employee accountant;

    // Банки и кредитные продукты
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "LEGAL_ENTITY_PROD_CREDIT",
            joinColumns = {@JoinColumn(name = "LEGAL_ENTITY_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "PROD_CREDIT_ID", referencedColumnName = "ID")})
    @OrderBy("name ASC")
    private List<ProdCredit> credProducts;

    // Дилерство
    @ElementCollection
    @CollectionTable(name = "LEGAL_ENTITY_MOTOR_BRAND")
    private Set<String> motorBrands;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = FileContainer.OWNER_ID_COLUMN)
    @OrderBy("name ASC")
    private List<LegalEntityFile> files = newArrayList();

    public boolean isRegNpstIsSame() {
        return regNpstIsSame;
    }

    public void setRegNpstIsSame(final boolean regNpstIsSame) {
        this.regNpstIsSame = regNpstIsSame;
    }

    public Employee getAccountant() {
        return accountant;
    }

    public void setAccountant(final Employee accountant) {
        this.accountant = accountant;
        if (accountant != null) {
            accountant.setLegalWorkPlace(this);
            accountant.setJobPosition("Главный бухгалтер");
        }
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(final String kpp) {
        this.kpp = kpp;
    }

    public String getSettlementAccount() {
        return settlementAccount;
    }

    public void setSettlementAccount(final String settlementAccount) {
        this.settlementAccount = settlementAccount;
    }

    public String getLoroAccount() {
        return loroAccount;
    }

    public void setLoroAccount(final String loroAccount) {
        this.loroAccount = loroAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(final String bik) {
        this.bic = bik;
    }

    public AddressInfo getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(final AddressInfo postAddress) {
        this.postAddress = postAddress;
    }

    /**
     * <p>Getter for the field <code>ogrnOgrip</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOgrnOgrip() {
        return ogrnOgrip;
    }

    /**
     * <p>Setter for the field <code>ogrnOgrip</code>.</p>
     *
     * @param ogrnOgrip a {@link java.lang.String} object.
     */
    public void setOgrnOgrip(final String ogrnOgrip) {
        this.ogrnOgrip = ogrnOgrip;
    }

    /**
     * <p>Getter for the field <code>inn</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInn() {
        return inn;
    }

    /**
     * <p>Setter for the field <code>inn</code>.</p>
     *
     * @param inn a {@link java.lang.String} object.
     */
    public void setInn(final String inn) {
        this.inn = inn;
    }

    /**
     * <p>Getter for the field <code>director</code>.</p>
     *
     * @return a {@link ru.extas.model.contacts.Person} object.
     */
    public Employee getDirector() {
        return director;
    }

    /**
     * <p>Setter for the field <code>director</code>.</p>
     *
     * @param director a {@link ru.extas.model.contacts.Person} object.
     */
    public void setDirector(final Employee director) {
        this.director = director;
        if(director != null){
            director.setLegalWorkPlace(this);
            director.setJobPosition("Генеральный директор");
        }
    }


    /**
     * <p>Getter for the field <code>credProducts</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ProdCredit> getCredProducts() {
        return credProducts;
    }

    /**
     * <p>Setter for the field <code>credProducts</code>.</p>
     *
     * @param credProducts a {@link java.util.List} object.
     */
    public void setCredProducts(final List<ProdCredit> credProducts) {
        this.credProducts = credProducts;
    }

    /**
     * <p>Getter for the field <code>motorBrands</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getMotorBrands() {
        return motorBrands;
    }

    /**
     * <p>Setter for the field <code>motorBrands</code>.</p>
     *
     * @param motorBrands a {@link java.util.Set} object.
     */
    public void setMotorBrands(final Set<String> motorBrands) {
        this.motorBrands = motorBrands;
    }

    /**
     * <p>Getter for the field <code>company</code>.</p>
     *
     * @return a {@link ru.extas.model.contacts.Company} object.
     */
    public Company getCompany() {
        return company;
    }

    /**
     * <p>Setter for the field <code>company</code>.</p>
     *
     * @param company a {@link ru.extas.model.contacts.Company} object.
     */
    public void setCompany(final Company company) {
        this.company = company;
        if(getDirector() != null && !getDirector().getCompany().equals(company))
            setDirector(null);
        if(getAccountant() != null && !getAccountant().getCompany().equals(company))
            setAccountant(null);

    }

    /**
     * <p>Getter for the field <code>files</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<LegalEntityFile> getFiles() {
        return files;
    }

    /**
     * <p>Setter for the field <code>files</code>.</p>
     *
     * @param files a {@link java.util.List} object.
     */
    public void setFiles(final List<LegalEntityFile> files) {
        this.files = files;
    }
}
