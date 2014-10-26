package ru.extas.utils;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Сериализуемый поставщик
 *
 * @author Valery Orlov
 *         Date: 23.10.2014
 *         Time: 17:18
 */
@FunctionalInterface
public interface SupplierSer<T> extends Supplier<T>, Serializable {
}
