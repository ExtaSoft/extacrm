package ru.extas.model.contacts;

import javax.persistence.*;

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
}
