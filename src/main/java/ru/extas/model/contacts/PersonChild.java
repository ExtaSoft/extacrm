package ru.extas.model.contacts;

import org.joda.time.LocalDate;
import ru.extas.model.common.IdentifiedObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Valery Orlov
 *         Date: 09.09.2014
 *         Time: 12:49
 */
@Entity
@Table(name = "PERSON_CHILD")
public class PersonChild extends IdentifiedObject {

    @Column(length = Person.NAME_LENGTH)
    @Size(max = Person.NAME_LENGTH)
    @NotNull
    private String name;

    // Дата рождения
    @NotNull
    private LocalDate birthday;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private Person parent;

    public PersonChild() {
    }

    public PersonChild(final Person person) {
        parent = person;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(final LocalDate birthday) {
        this.birthday = birthday;
    }

    public Person getParent() {
        return parent;
    }

    public void setParent(final Person parent) {
        this.parent = parent;
    }
}
