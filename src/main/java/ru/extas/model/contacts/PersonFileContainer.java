package ru.extas.model.contacts;

import ru.extas.model.common.FileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * файл привязанный к физ. лицу
 *
 * @author Valery Orlov
 *         Date: 11.07.2014
 *         Time: 12:28
 */
@Entity
@Table(name = "PERSON_FILE")
public class PersonFileContainer extends FileContainer {

}
