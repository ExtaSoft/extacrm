package ru.extas.server.motor;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.motor.MotorBrand;

import java.util.List;

/**
 * Created by Valery on 04.06.2014.
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface MotorBrandRepository extends JpaRepository<MotorBrand, String> {

    /**
     * <p>loadAllNames.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Query("SELECT b.name FROM MotorBrand b ORDER BY b.name ASC")
    List<String> loadAllNames();

    /**
     * <p>loadNamesByType.</p>
     *
     * @param type a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    @Query("SELECT b.name FROM MotorBrand b, b.brandTypes t WHERE t.name = :type ORDER BY b.name ASC")
    List<String> loadNamesByType(@Param("type") String type);
}
