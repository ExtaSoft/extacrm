/**
 *
 */
package ru.extas.server;

import ru.extas.guice.ExtasGuiceFilter;

/**
 * Поставщик служб приложения
 *
 * @author Valery Orlov
 */
public final class ServiceLocator {

    public static final String EXTACRM_JPA_UNIT = "extacrmJpaUnit";

    /**
     * Ищет подходящий экземпляр для интерфейса службы
     *
     * @param srvType Тип интерфейса службы
     * @return экземпляр интерфейса службы
     */
    public static <TServiceType> TServiceType lookup(Class<TServiceType> srvType) {
        return ExtasGuiceFilter.getInjector().getInstance(srvType);
    }
}
