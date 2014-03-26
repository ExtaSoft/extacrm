package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;

import java.util.List;

/**
 * Интерфейс работы с репозиторием компаний
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 23:52
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface CompanyRepository extends CrudRepository<Company, String> {

    /**
     * Ищет компании сотрудником которых является контакт
     *
     * @param employee сотрудник
     * @return список найденных компаний
     */
    @Query("select c from Company c, c.employees e where e = :employee")
    List<Company> findByEmployee(@Param("employee") Person employee);
}
