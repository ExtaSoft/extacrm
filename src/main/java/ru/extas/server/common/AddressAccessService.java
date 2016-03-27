package ru.extas.server.common;

import ru.extas.model.common.Address;

import java.util.Collection;
import java.util.List;

/**
 * Сервис предоставляющий данные об адресах через dadata.ru
 */
public interface AddressAccessService {

  /**
   * Фильтрует адреса в соответствии с введенными данными (дает подсказку по адресу)
   *
   * @param filterPrefix - часть вводимого адреса
   * @return список адресов соответствующих введенным данным
     */
  List<Address> filterAddresses(String filterPrefix);

    /**
     * Запрашивает список регионов
     *
     * @return список регионов
     */
    Collection<String> loadRegions();

    /**
     * Запрашивает города (только региональные центры)
     *
     * @return список городов
     */
    Collection<String> loadCities();

    /**
     * Ищет город (региональный центр) по названию региона
     *
     * @param region - название региона
     * @return найденный город или <code>null</code>
     */
    String findCityByRegion(String region);

    /**
     * Ищет региот по городу (региональному центру)
     *
     * @param city город (региональный центр)
     * @return найденный регион или <code>null</code>
     */
    String findRegionByCity(String city);

    /**
     * Поучить "чистое" (соответствующее справочнику) название региона.
     *
     * @param dirtyRegion черновое имя региона
     * @return "чистое" имя региона или null если регион не удалось квалифицировать.
     */
    String clarifyRegion(String dirtyRegion);

    /**
     * Получить список всех регионов (объектов)
     *
     * @return список регионов
     */
    List<Region> findAllRegions();
}
