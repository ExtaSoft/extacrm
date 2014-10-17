package ru.extas.server.contacts;

import ru.extas.model.contacts.Employee;
import ru.extas.security.SecuredRepository;

/**
 * Определяет дополнительные методы работы с сотрудниками
 *
 * @author Valery Orlov
 *         Date: 03.04.2014
 *         Time: 11:57
 * @version $Id: $Id
 * @since 0.3.0
 */
public interface EmployeeService extends SecuredRepository<Employee> {

}
