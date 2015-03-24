package ru.extas.server.motor;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.extas.model.motor.MotorModel;

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
public interface MotorModelRepository extends JpaRepository<MotorModel, String> {

    @Query("SELECT m.name FROM MotorModel m WHERE m.type = :type AND m.brand = :brand ORDER BY m.name ASC")
    List<String> loadNamesByTypeAndBrand(@Param("type") String type, @Param("brand") String brand);

}
