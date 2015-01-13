package ru.extas.server.product;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.extas.model.sale.ProdCredit;

import java.math.BigDecimal;
import java.util.List;

/**
 * Интерфейс доступа к базе продуктов "Кредит"
 *
 * @author Valery Orlov
 *         Date: 16.01.14
 *         Time: 18:22
 * @version $Id: $Id
 * @since 0.3
 */
@Repository
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public interface ProdCreditRepository extends JpaRepository<ProdCredit, String> {

	/**
	 * <p>findByActiveOrderByNameAsc.</p>
	 *
	 * @param active a boolean.
	 * @return a {@link java.util.List} object.
	 */
	List<ProdCredit> findByActiveOrderByNameAsc(boolean active);

	/**
	 * Получает минимально возможный срок кредита среди всех активных продуктов
	 *
	 * @return минимальный срок или null, если нет активных продкутов
	 */
	@Query("SELECT min(p.minPeriod) FROM ProdCredit p WHERE p.active = true")
	Integer getMinPeriod();

	/**
	 * Получает максимально возможный срок кредита среди всех активных продуктов
	 *
	 * @return максимальный срок или null, если нет активных продуктов
	 */
	@Query("SELECT max(p.maxPeriod) FROM ProdCredit p WHERE p.active = true")
	Integer getMaxPeriod();

	/**
	 * Получает минимальный размер шага с которым изменяется срок кредитов среди всех активных продуктов
	 *
	 * @return минимальный шаг или null, когда нет активных продуктов
	 */
	@Query("SELECT min(p.step) FROM ProdCredit p WHERE p.active = true")
	Integer getPeriodMinStep();

	/**
	 * Найти подходящие под параметры кредитные продукты
	 *
	 * @param price       цена товара
	 * @param downPayment первоначальный платеж (руб.)
	 * @param period      срок кредита (месяцы)
	 * @return список подходящих кредитных продуктов
	 */
	@Query("SELECT p FROM ProdCredit p WHERE p.active = true")
	List<ProdCredit>  findSuitableProducts(BigDecimal price, BigDecimal downPayment, int period);

}
