package ru.extas.server.contacts;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.contacts.LegalEntity;

/**
 * Интерфейс работы с репозиторием юр. лиц
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 23:52
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface LegalEntityRepository extends JpaRepository<LegalEntity, String>, LegalEntityService {
}
