package ru.extas.server.lead;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.lead.Lead;

/**
 * Служба управления лидами
 *
 * @author Valery Orlov
 *         Date: 23.10.13
 *         Time: 22:52
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface LeadRepository extends JpaRepository<Lead, String>, LeadService {

}
