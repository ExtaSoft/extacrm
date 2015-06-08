package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Pageable;
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
    @Query("select distinct(s) from SalePoint s join s.curatorsGroup.curators c where c = :employee")
    List<SalePoint> findByCurator(@Param("employee") Employee employee);

    /**
     * Возвращает бренды доступные на данной торговой точке
     *
     * @param salePoint торговая точка для которой ищутся брэнды
     * @return список найденных брэндов
     */
    @Query("select b from SalePoint s join s.legalEntities e join e.motorBrands b where s = :salePoint")
    List<String> findSalePointBrands(@Param("salePoint") SalePoint salePoint);

    /**
     * Возвращает число актуальных (не архивных и доступных извне) торговых точек
     *
     * @return колличество торговых точек
     */
    @Query("select count(s) from SalePoint s where (s.archived = false and s.apiExpose = true)")
    long countActual();

    /**
     * Возвращает количество торговых точек работаюцих с указанными брендами
     *
     * @param brands срисок брендов
     * @return число торговых точек
     */
    @Query("select count(distinct s) from SalePoint s  join s.legalEntities e join e.motorBrands b " +
            "where (s.archived = false and s.apiExpose = true) and b in :brands")
    long countActualByBrand(@Param("brands") List<String> brands);

    /**
     * Возвращает количество торговых точек в указанных регионах, работающих с указанными брендами техники
     *
     * @param region список регионов торговой техники
     * @param brands срисок брендов
     * @return число торговых точек
     */
    @Query("select count(distinct s) from SalePoint s  join s.legalEntities e join e.motorBrands b " +
            "where (s.archived = false and s.apiExpose = true) and s.regAddress.region in :regions and b in :brands")
    long countActualByRegionAndBrand(@Param("regions") List<String> regions,
                                     @Param("brands") List<String> brands);

    /**
     * <p>countActualByRegion.</p>
     *
     * @param region a {@link String} object.
     * @return a long.
     */
    @Query("select count(s) from SalePoint s where (s.archived = false and s.apiExpose = true) and s.regAddress.region in :regions")
    long countActualByRegion(@Param("regions") List<String> regions);

    /**
     * <p>findByRegion.</p>
     *
     * @param region a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    @Query("select s from SalePoint s where s.regAddress.region = :region")
    List<SalePoint> findByRegion(@Param("region") String region);

    /**
     * Возвращает актуальные (не архивные и доступные извне) торговые точеки
     *
     * @return список торговых точек
     * @param pageable
     */
    @Query("select s from SalePoint s where (s.archived = false and s.apiExpose = true) order by s.name asc")
    List<SalePoint> findActual(Pageable pageable);

    /**
     * Возвращает список актуальных торговых точек, работающих в указанных регионах
     *
     * @param region список целевых регионов
     * @param pageable
     * @return
     */
    @Query("select s from SalePoint s " +
            "where (s.archived = false and s.apiExpose = true) and s.regAddress.region in :regions " +
            "order by s.name asc")
    List<SalePoint> findActualByRegion(@Param("regions") List<String> region, Pageable pageable);

    /**
     * Возвращает список торговых точек работаюцих с указанными брендами
     *
     * @param brands срисок брендов
     * @param pageable
     * @return список торговых точек
     */
    @Query("select distinct s from SalePoint s  join s.legalEntities e join e.motorBrands b " +
            "where (s.archived = false and s.apiExpose = true) and b in :brands " +
            "order by s.name asc")
    List<SalePoint> findActualByBrand(@Param("brands") List<String> brands, Pageable pageable);

    /**
     * Возвращает список торговых точек в указанных регионах, работающих с указанными брендами техники
     *
     * @param region список регионов
     * @param brands срисок брендов
     * @param pageable
     * @return список торговых точек
     */
    @Query("select distinct s from SalePoint s  join s.legalEntities e join e.motorBrands b " +
            "where (s.archived = false and s.apiExpose = true) and s.regAddress.region in :regions and b in :brands " +
            "order by s.name asc")
    List<SalePoint> findActualByRegionAndBrand(@Param("regions") List<String> regions,
                                               @Param("brands") List<String> brands, Pageable pageable);
}
