package ru.extas.model.contacts;

import org.joda.time.LocalDate;
import ru.extas.model.common.IdentifiedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author Valery Orlov
 *         Date: 09.09.2014
 *         Time: 12:49
 */
@Entity
@Table(name = "PERSON_CHILD")
public class PersonChild extends IdentifiedObject {

    @Column(length = Person.NAME_LENGTH)
    @Max(Person.NAME_LENGTH)
    @NotNull
    private String name;

    // Дата рождения
    @NotNull
    private LocalDate birthday;

    @ManyToOne
    private Person parent;

    public PersonChild() {
    }

    public PersonChild(Person person) {
        parent = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Person getParent() {
        return parent;
    }

    public void setParent(Person parent) {
        this.parent = parent;
    }
}
