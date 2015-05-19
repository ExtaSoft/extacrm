package ru.extas.model.security;

import ru.extas.model.common.AuditedObject;
import ru.extas.model.contacts.Employee;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Группа кураторов
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
@Entity
@Table(name = "CURATORS_GROUP", indexes = {@Index(columnList = "name")})
public class CuratorsGroup extends AuditedObject {

    private static final long serialVersionUID = 4149728748291041330L;

    /**
     * Имя группы
     */
    @Column(length = 50)
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "CURATORS_GROUP_LINK",
            joinColumns = {@JoinColumn(name = "CURATOR_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")})
    @OrderBy("name ASC")
    private Set<Employee> curators = newHashSet();

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link String} object.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<Employee> getCurators() {
        return curators;
    }

    public void setCurators(Set<Employee> curators) {
        this.curators = curators;
    }

}
