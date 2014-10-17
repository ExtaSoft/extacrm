package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Правила безопасности для конкретного объекта.
 * Включает в себя признаки определяющие срез объектов в соответствии с настройками безопасности:
 * <ul>
 * <li>Привязка к пользователю, с указанием уровня доступа. Определяет разрешения конкретных пользователей для доступа к объекту.
 * Уровни доступа: Владелец, Редактор, Читатель. Явное разрешение отменяет любые другие (протеворечащие) правила безопасности.</li>
 * <li>Привязка к региону. Определяет доступ к объекту в соответствии с разрешенными регионами.</li>
 * <li>Привязка к бренду. Определяет доступ к объекту в соответствии с разрешенными брендами.</li>
 * <li>Привязка к торговой точке. Определяет доступ к объекту в соответствии с целевой областью видимости "Объекты той же торговой точки".</li>
 * <li>Привязка к компании. Определяет доступ к объекту в соответствии с целевой областью видимости "Корпоративные объекты (той же компании)".</li>
 * </ul>
 *
 * @author Valery Orlov
 *         Date: 08.10.2014
 *         Time: 14:10
 */
@Entity
@Table(name = "SECURITY_RULE")
public class ObjectSecurityRule extends AuditedObject {

    // Привязка объекта к Торговой точке
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "SECURITY_RULE_SALE_POINT",
            joinColumns = {@JoinColumn(name = "SECURITY_RULE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "SALE_POINT_ID", referencedColumnName = "ID")} )
    private Set<SalePoint> salePoints = newHashSet();

    // Привязка объекта к Компании
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "SECURITY_RULE_COMPANY",
            joinColumns = {@JoinColumn(name = "SECURITY_RULE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")})
    private Set<Company> companies = newHashSet();

    // Привязка объекта к пользователям
    @OneToMany(mappedBy = "securityRule", orphanRemoval = true, cascade = CascadeType.ALL)
    @MapKey(name = "user")
    private Map<Employee, UserObjectAccess> users = newHashMap();

    // Привязка обхекта к регионам
    @ElementCollection
    @CollectionTable(name = "SECURITY_RULE_REGION",
            joinColumns = {@JoinColumn(name = "SECURITY_RULE_ID")},
            indexes = {
                    @Index(columnList = "SECURITY_RULE_ID, REGIONS")
            })
    private Set<String> regions = newHashSet();

    // Привязка объекта к брендам
    @ElementCollection
    @CollectionTable(name = "SECURITY_RULE_BRAND",
            joinColumns = {@JoinColumn(name = "SECURITY_RULE_ID")},
            indexes = {
                    @Index(columnList = "SECURITY_RULE_ID, BRANDS")
            })
    private Set<String> brands = newHashSet();


    public Set<SalePoint> getSalePoints() {
        return salePoints;
    }

    public void setSalePoints(final Set<SalePoint> salePoints) {
        this.salePoints = salePoints;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(final Set<Company> companies) {
        this.companies = companies;
    }

    public Map<Employee, UserObjectAccess> getUsers() {
        return users;
    }

    public void setUsers(final Map<Employee, UserObjectAccess> users) {
        this.users = users;
    }

    public Set<String> getRegions() {
        return regions;
    }

    public void setRegions(final Set<String> regions) {
        this.regions = regions;
    }

    public Set<String> getBrands() {
        return brands;
    }

    public void setBrands(final Set<String> brands) {
        this.brands = brands;
    }
}
