package ru.extas.model.contacts;

import ru.extas.model.common.FileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Файл привязанный к юридическому лицу
 *
 * @author Valery Orlov
 *         Date: 11.07.2014
 *         Time: 12:40
 * @version $Id: $Id
 * @since 0.5.0
 */
@Entity
@Table(name = "LEGAL_ENTITY_FILE")
public class LegalEntityFile extends FileContainer {
}
