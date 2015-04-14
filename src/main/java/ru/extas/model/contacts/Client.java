package ru.extas.model.contacts;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Абстрактный клиент. Юр. или Физ. лицо
 *
 * @author Valery Orlov
 *         Date: 02.12.2014
 *         Time: 14:24
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "CLIENT", indexes = {@Index(columnList = "NAME")})
public abstract class Client extends Contact {

    // Дополнительный мобильный телефон
    @Column(name = "SECOND_PHONE", length = PHONE_LINGHT)
    @Size(max = PHONE_LINGHT)
    private String secondPhone;

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(final String secondPhone) {
        this.secondPhone = secondPhone;
    }
}
