/**
 *
 */
package ru.extas.model.common;

import org.eclipse.persistence.annotations.UuidGenerator;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;

import javax.persistence.*;
import javax.validation.constraints.Max;

/**
 * Базовый класс для всех приввелегий сущностей.
 * Имплементирует все кроме ссылок на объект к которому относится привилегия.
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@UuidGenerator(name = "system-uuid")
public abstract class AbstractPrivilege extends ChangeMarkedObject {

    // Id объекта определяется в дочерних классах. Делая выборку из журнала по этому полю мы можем получить список
    // всех пользователей которые имеют к нему доступ как к “собственному”, “корпоративному” или “объекту ТТ”.

    // Id пользователя - определяет пользователя которому доступен данный объект.
    // Выборка из журнала по данному полю даст нам полный список “Собственных объектов” пользователя.
    @OneToOne
    private Person user;

    // Уровень доступа пользователя. Определяет уровень доступа пользователя к объекту “Просмотр/редактирование”.
    @Column(name = "ACTIONS", length = 50)
    @Max(50)
    private String actions;

    // Id Торговой точки к которой приписан пользователь.
    // Связывает данный объект с областью видимости “Объекты торговой точки”.
    // Делая выборку из журнала, по этому полю, можно получить все “Собственные” объекты
    // всех пользователей определенной торговой точки.
    @OneToOne
    private SalePoint salePoint;

    // Id  Компании пользователя.
    // Связывает данный объект с областью видимости “Корпоративные” объекты.
    // Делая выборку из журнала, по этому полю, можно получить все “Собственные” объекты
    // всех пользователей определенной компании.
    @OneToOne
    private Company company;

    protected AbstractPrivilege() {
        super();
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public SalePoint getSalePoint() {
        return salePoint;
    }

    public void setSalePoint(SalePoint salePoint) {
        this.salePoint = salePoint;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
