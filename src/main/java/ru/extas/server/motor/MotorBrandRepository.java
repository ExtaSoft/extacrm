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
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface MotorBrandRepository extends JpaRepository<MotorBrand, String> {

    @Query("SELECT b.name FROM MotorBrand b")
    List<String> loadAllNames();

    @Query("SELECT b.name FROM MotorBrand b, b.brandTypes t WHERE t.name = :type")
    List<String> loadNamesByType(@Param("type") String type);
}
