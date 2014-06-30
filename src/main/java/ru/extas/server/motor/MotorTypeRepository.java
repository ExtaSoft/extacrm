package ru.extas.server.motor;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extas.model.motor.MotorType;

import java.util.List;

/**
 * Created by Valery on 04.06.2014.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface MotorTypeRepository  extends JpaRepository<MotorType, String> {

    @Query("SELECT t.name FROM MotorType t")
    List<String> loadAllNames();

}
