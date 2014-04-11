package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;

import java.util.List;

/**
 * Интерфейс работы с репозиторием торговых точек
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 23:52
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface SalePointRepository extends JpaRepository<SalePoint, String>, SalePointService {

    /**
     * Ищет торговые точки сотрудником которых является контакт
     *
     * @param employee сотрудник
     * @return список найденных торговых точек
     */
    @Query("select s from SalePoint s, s.employees e where e = :employee")
    List<SalePoint> findByEmployee(@Param("employee") Person employee);

    @Query("select count(s) from SalePoint s where s.actualAddress.region = :region")
    long countByRegion(@Param("region") String region);

    @Query("select s from SalePoint s where s.actualAddress.region = :region")
    List<SalePoint> findByRegion(@Param("region") String region);
}
