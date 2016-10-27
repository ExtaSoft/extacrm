package ru.extas.model.contacts;

import ru.extas.model.common.OwnedFileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * файл привязанный к физ. лицу
 *
 * @author Valery Orlov
 *         Date: 11.07.2014
 *         Time: 12:28
 * @version $Id: $Id
 * @since 0.5.0
 */
@Entity
@Table(name = "PERSON_FILE")
public class PersonFileContainer extends OwnedFileContainer {

}
