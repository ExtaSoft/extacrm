package ru.extas.server.common;

import ru.extas.model.common.ArchivedObject;

import java.util.Collection;
import java.util.Set;

/**
 * Сервис работы с архивом
 *
 * @author Valery Orlov
 *         Date: 23.11.2014
 *         Time: 18:45
 */
public interface ArchiveService {

    /**
     * Отправляет объект в архив
     *
     * @param entity архивируемый объект
     * @param <TEntity> тип архивируемого объекта
     * @return архивированный объект
     */
    <TEntity extends ArchivedObject> TEntity archive(TEntity entity);

    /**
     * Отправляет объект в архив
     *
     * @param <TEntity> тип архивируемого объекта
     * @param entities коллекция архивируемых объектов
     * @return архивированные объекты
     */
    <TEntity extends ArchivedObject> Set<TEntity> archive(Set<TEntity> entities);

    /**
     * Извлекает объект из архива
     *
     * @param entity извлекаемый архивный объект
     * @param <TEntity> тип архивного объекта
     * @return разархивированный объект
     */
    <TEntity extends ArchivedObject> TEntity extract(TEntity entity);

    /**
     * Извлекает объекты из архива
     *
     * @param <TEntity> тип архивного объекта
     * @param entities коллекция извлекаемых архивных объектов
     * @return разархивированные объекты
     */
    <TEntity extends ArchivedObject> Set<TEntity> extract(Set<TEntity> entities);

}
