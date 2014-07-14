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
 *
 * @author Valery_2
 * @version $Id: $Id
 * @since 0.5.0
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface MotorTypeRepository  extends JpaRepository<MotorType, String> {

    /**
     * <p>loadAllNames.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Query("SELECT t.name FROM MotorType t ORDER BY t.name ASC")
    List<String> loadAllNames();

}
