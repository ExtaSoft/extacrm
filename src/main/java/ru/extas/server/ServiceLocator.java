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
