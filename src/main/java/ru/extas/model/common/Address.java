package ru.extas.model.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.extas.model.contacts.Contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Бин расширенного адреса.
 * https://dadata.ru/api/clean/#response
 *
 * @author sandarkin
 * @since 2.0
 * @version 2.0
 */
@Builder
@Getter
@Setter
@Entity
@Table(name = "ADDRESS")
public class Address extends AuditedObject {

    /**
     * Адрес, введенный в ручном режиме или пришедший из автоматической актуализации.
     */
    @Column(name = "PLAIN")
    private String plain;

    /**
     * Адрес одной строкой (как показывается в списке подсказок).
     */
    @Column(name = "VALUE", length = 255)
    @Size(max = 255)
    private String value;

    /**
     * Адрес одной строкой (полный, от региона)
     */
    @Column(name = "UNRESTRICTED_VALUE", length = 500)
    @Size(max = 500)
    private String unrestrictedValue;

    /**
     * Индекс.
     */
    @Column(name = "POSTAL_CODE", length = 6)
    @Size(max = 6)
    private String postalCode;

    /**
     *
     */
    @Column(name = "COUNTRY", length = 120)
    @Size(max = 120)
    private String country;

    /**
     * Тип региона (сокращенный).
     */
    @Column(name = "REGION_TYPE", length = 10)
    @Size(max = 10)
    private String regionType;

    /**
     * Тип региона.
     */
    @Column(name = "REGION_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String regionTypeFull;

    /**
     * Регион.
     */
    @Column(name = "REGION", length = 120)
    @Size(max = 120)
    private String region;

    /**
     * Тип района в регионе (сокращенный).
     */
    @Column(name = "AREA_TYPE", length = 10)
    @Size(max = 10)
    private String areaType;

    /**
     * Тип района в регионе.
     */
    @Column(name = "AREA_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String areaTypeFull;

    /**
     * Район в регионе.
     */
    @Column(name = "AREA", length = 120)
    @Size(max = 120)
    private String area;

    /**
     * Тип города (сокращенный).
     */
    @Column(name = "CITY_TYPE", length = 10)
    @Size(max = 10)
    private String cityType;

    /**
     * Тип города.
     */
    @Column(name = "CITY_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String cityTypeFull;

    /**
     * Город.
     */
    @Column(name = "CITY", length = 120)
    @Size(max = 120)
    private String city;

    /**
     * Тип населенного пункта (сокращенный).
     */
    @Column(name = "SETTLEMENT_TYPE", length = 10)
    @Size(max = 10)
    private String settlementType;

    /**
     * Тип населенного пункта.
     */
    @Column(name = "SETTLEMENT_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String settlementTypeFull;

    /**
     * Населенный пункт.
     */
    @Column(name = "SETTLEMENT", length = 120)
    @Size(max = 120)
    private String settlement;

    /**
     * Район города.
     */
    @Column(name = "CITY_DISTRICT", length = 120)
    @Size(max = 120)
    private String cityDistrict;

    /**
     * Тип улицы (сокращенный).
     */
    @Column(name = "STREET_TYPE", length = 10)
    @Size(max = 10)
    private String streetType;

    /**
     * Тип улицы.
     */
    @Column(name = "STREET_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String streetTypeFull;

    /**
     * Улица.
     */
    @Column(name = "STREET", length = 120)
    @Size(max = 120)
    private String street;

    /**
     * Тип дома (сокращенный).
     */
    @Column(name = "HOUSE_TYPE", length = 10)
    @Size(max = 10)
    private String house_type;

    /**
     * Тип дома.
     */
    @Column(name = "HOUSE_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String houseTypeFull;

    /**
     * Дом.
     */
    @Column(name = "HOUSE", length = 50)
    @Size(max = 50)
    private String house;

    /**
     * Тип корпуса/строения (сокращенный).
     */
    @Column(name = "BLOCK_TYPE", length = 10)
    @Size(max = 10)
    private String blockType;

    /**
     * Тип корпуса/строения.
     */
    @Column(name = "BLOCK_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String blockTypeFull;

    /**
     * Корпус/строение.
     */
    @Column(name = "CELL_PHONE", length = 50)
    @Size(max = 50)
    private String block;

    /**
     * Тип квартиры (сокращенный).
     */
    @Column(name = "FLAT_TYPE", length = 10)
    @Size(max = 10)
    private String flatType;

    /**
     * Тип квартиры.
     */
    @Column(name = "FLAT_TYPE_FULL", length = 50)
    @Size(max = 50)
    private String flatTypeFull;

    /**
     * Квартира.
     */
    @Column(name = "FLAT", length = 50)
    @Size(max = 50)
    private String flat;

    /**
     * Площадь квартиры.
     */
    @Column(name = "FLAT_AREA", length = 50)
    @Size(max = 50)
    private String flat_area;

    /**
     * Рыночная стоимость м2.
     */
    @Column(name = "SQUARE_METER_PRICE", length = 50)
    @Size(max = 50)
    private String squareMeterPrice;

    /**
     * Рыночная стоимость квартиры.
     */
    @Column(name = "FLAT_PRICE", length = 50)
    @Size(max = 50)
    private String flatPrice;

    /**
     * Абонентский ящик.
     */
    @Column(name = "POSTAL_BOX", length = 50)
    @Size(max = 50)
    private String postalBox;

    /**
     * Код ФИАС:
     *   HOUSE.HOUSEGUID, если дом найден в ФИАС по точному совпадению;
     *   HOUSEINT.HOUSEGUID, если дом найден в ФИАС как часть интервала;
     *   ADDROBJ.AOGUID в противном случае.
     */
    @Column(name = "FIAS_ID", length = 36)
    @Size(max = 36)
    private String fiasId;

    /**
     * Уровень детализации, до которого адрес найден в ФИАС:
     *   0 — страна;
     *   1 — регион;
     *   3 — район;
     *   4 — город;
     *   6 — населенный пункт;
     *   7 — улица;
     *   8 — дом;
     *   -1 — иностранный или пустой.
     */
    @Column(name = "FIAS_LEVEL", length = 2)
    @Size(max = 2)
    private String fiasLevel;

    /**
     * Код КЛАДР.
     */
    @Column(name = "KLADR_ID", length = 19)
    @Size(max = 19)
    private String kladrId;

    /**
     * Является ли город центром:
     *   1 — центр района (Московская обл, Одинцовский р-н, г Одинцово)
     *   2 — центр региона (Новосибирская обл, г Новосибирск);
     *   3 — центр района и региона (Костромская обл, Костромской р-н, г Кострома);
     *   0 — ни то, ни другое (Московская обл, г Балашиха).
     */
    @Column(name = "CAPITAL_MARKER", length = 1)
    @Size(max = 1)
    private String capitalMarker;

    /**
     * Код ОКАТО.
     */
    @Column(name = "OKATO", length = 11)
    @Size(max = 11)
    private String okato;

    /**
     * Код ОКТМО.
     */
    @Column(name = "OKTMO", length = 11)
    @Size(max = 11)
    private String oktmo;

    /**
     * Код ИФНС для физических лиц.
     */
    @Column(name = "TAX_OFFICE", length = 4)
    @Size(max = 4)
    private String taxOffice;

    /**
     * Код ИФНС для организаций (не заполняется).
     */
    @Column(name = "TAX_OFFICE_LEGAL", length = 4)
    @Size(max = 4)
    private String taxOfficeLegal;

    /**
     * Часовой пояс.
     */
    @Column(name = "TIMEZONE", length = 10)
    @Size(max = 10)
    private String timezone;

    /**
     * Координаты: широта.
     */
    @Column(name = "GEO_LAT", length = 10)
    @Size(max = 10)
    private String geoLat;

    /**
     * Координаты: долгота.
     */
    @Column(name = "GEO_LON", length = 10)
    @Size(max = 10)
    private String geoLon;

    /**
     * Код точности координат:
     *   0 — Точные координаты
     *   1 — Ближайший дом
     *   2 — Улица
     *   3 — Населенный пункт
     *   4 — Город
     *   5 — Координаты не определены
     */
    @Column(name = "QC_GEO", length = 5)
    @Size(max = 5)
    private String qcGeo;

    /**
     * Код полноты:
     *   0 — Пригоден для почтовой рассылки
     *   1 — Не пригоден, нет региона
     *   2 — Не пригоден, нет города
     *   3 — Не пригоден, нет улицы
     *   4 — Не пригоден, нет дома
     *   5 — Пригоден для юридических лиц или частных владений (нет квартиры)
     *   6 — Не пригоден
     *   7 — Иностранный адрес
     *   8 — До почтового отделения (абонентский ящик или адрес до востребования).
     *   9 — Подходит для писем, но не для курьерской доставки.
     *   10 — Пригоден, но низкая вероятность успешной доставки (дом не найден в ФИАС)
     */
    @Column(name = "QC_COMPLETE", length = 5)
    @Size(max = 5)
    private String qcComplete;

    /**
     * Код проверки дома:
     *   2 — Дом найден в ФИАС по точному совпадению (Вероятность доставки Высокая)
     *   3 — В ФИАС найден похожий дом; различие в литере, корпусе или строении (Вероятность доставки Средняя)
     *   4 — Дом найден в ФИАС по диапазону (Вероятность доставки Средняя)
     *   10 — Дом не найден в ФИАС (Вероятность доставки Низкая)
     */
    @Column(name = "QC_HOUSE", length = 5)
    @Size(max = 5)
    private String qcHouse;

    /**
     * Код качества:
     *   0 — Исходное значение распознано уверенно. Не требуется ручная проверка
     *   1 — Исходное значение распознано с допущениями или не распознано. Требуется ручная проверка
     *   2 — Исходное значение пустое или заведомо «мусорное»
     */
    @Column(name = "QC", length = 5)
    @Size(max = 5)
    private String qc;

    /**
     * Нераспознанная часть адреса.
     */
    @Column(name = "UNPARSED_PARTS", length = 250)
    @Size(max = 250)
    private String unparsedParts;

}
