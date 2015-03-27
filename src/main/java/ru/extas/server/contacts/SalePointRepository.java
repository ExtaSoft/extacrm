package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.Employee;
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
    List<SalePoint> findByEmployee(@Param("employee") Employee employee);

    /**
     * Ищет торговые точки которые курируются указанным сотрудником
     *
     * @param employee куратор - сотрудник ЭА
     * @return список куририуемых торговых точек
     */
    @Query("select s from SalePoint s where s.curator = :employee")
    List<SalePoint> findByCurator(@Param("employee") Employee employee);

    /**
     * <p>countByRegion.</p>
     *
     * @param region a {@link java.lang.String} object.
     * @return a long.
     */
    @Query("select count(s) from SalePoint s where s.regAddress.region = :region")
    long countByRegion(@Param("region") String region);

    /**
     * <p>findByRegion.</p>
     *
     * @param region a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    @Query("select s from SalePoint s where s.regAddress.region = :region")
    List<SalePoint> findByRegion(@Param("region") String region);

    /**
     * Возвращает бренды доступные на данной торговой точке
     *
     * @param salePoint торговая точка для которой ищутся брэнды
     * @return список найденных брэндов
     */
    @Query("select b from SalePoint s join s.legalEntities e join e.motorBrands b where s = :salePoint")
    List<String> findSalePointBrands(@Param("salePoint") SalePoint salePoint);
}
