package ru.extas.model.contacts;

import ru.extas.model.common.AbstractPrivilege;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Привилегии доступа к физ. лицам
 *
 * @author Valery Orlov
 *         Date: 24.03.2014
 *         Time: 11:28
 */
@Entity
@Table(name = "PERSON_PRIVILEGE")
public class PersonPrivilege extends AbstractPrivilege {

    @OneToOne
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
