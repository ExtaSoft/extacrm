package ru.extas.model.contacts;

import ru.extas.model.common.OwnedFileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * файл привязанный к сотруднику
 *
 * @author Valery Orlov
 *         Date: 24.10.2014
 *         Time: 18:25
 */
@Entity
@Table(name = "EMPLOYEE_FILE")
public class EmployeeFile extends OwnedFileContainer {
}
