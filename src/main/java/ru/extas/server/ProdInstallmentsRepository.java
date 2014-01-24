package ru.extas.server;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.extas.model.ProdInstallments;

import java.util.List;

/**
 * Интерфейс доступа к базе продуктов "Рассрочка"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProdInstallmentsRepository extends CrudRepository<ProdInstallments, String> {

	List<ProdInstallments> findByActive(boolean active);

}
