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

    /**
     * Определяет является ли переданный сотрудник, сотрудником Экстрим Ассистанс
     * @param employee проверяемый сотрудник
     * @return true если сотрудник Экстрим Ассистанс, иначе false
     */
    boolean isEAEmployee(Employee employee);

    /**
     * Определяет является ли переданный сотрудник, сотрудником банка
     * @param employee проверяемый сотрудник
     * @return true если сотрудник банка, иначе false
     */
    boolean isBankEmployee(Employee employee);

    /**
     * Определяет является ли переданный сотрудник, сотрудником дилера
     * @param employee проверяемый сотрудник
     * @return true если сотрудник дилера, иначе false
     */
    boolean isDealerEmployee(Employee employee);

    /**
     * Определяет является ли переданный сотрудник, сотрудником колл-центра
     * @param employee проверяемый сотрудник
     * @return true если сотрудник колл-центра, иначе false
     */
    boolean isCallcenterEmployee(Employee employee);

}
