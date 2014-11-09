package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.Company;

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
public interface CompanyRepository extends JpaRepository<Company, String>, CompanyService {

    @Query("SELECT c FROM Company c WHERE c.id = '598EB4D6-BC0A-4A2F-B445-52BEF27AC873'")
    Company findOurCompany();
}
