package ru.extas.model.insurance;

import ru.extas.model.common.FileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Файлы привязанные к полису страхования.
 *
 * @author Valery Orlov
 *         Date: 11.07.2014
 *         Time: 12:44
 * @version $Id: $Id
 * @since 0.5.0
 */
@Entity
@Table(name = "INSURANCE_FILE")
public class InsuranceFileContainer extends FileContainer {

}
