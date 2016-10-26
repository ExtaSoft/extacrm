package ru.extas.server.info;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.info.InfoFile;

/**
 * Управление ффинформационными материалами
 *
 * Created by valery on 24.10.16.
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface InfoFileRepository extends JpaRepository<InfoFile, String> {

}
