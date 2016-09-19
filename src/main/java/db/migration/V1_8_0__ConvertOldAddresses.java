package db.migration;

import com.google.common.collect.ImmutableMap;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import ru.extas.model.common.Address;
import ru.extas.server.common.AddressAccessService;
import ru.extas.server.common.AddressAccessServiceImpl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Iterables.getFirst;

/**
 * Избавляемся от старых адресов
 * <p>
 * Created by valery on 23.03.16.
 */
public class V1_8_0__ConvertOldAddresses implements SpringJdbcMigration {

    private final AddressAccessService addressService = new AddressAccessServiceImpl();

    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedTemplate;

    /**
     * Executes this migration. The execution will automatically take place within a transaction, when the underlying
     * database supports it.
     *
     * @param jdbcTemplate The jdbcTemplate to use to execute statements.
     * @throws Exception when the migration failed.
     */
    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {
        this.template = jdbcTemplate;
        this.namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        // прежде всего дообновляем базу
        // Надо в сущ. адресах найти и дописать данные с типом (регион с типом и т.п.)
        updateAddressesWithComplexData();

        // Обновляем регионы
        updateRegions("COMPANY", "REGION");
        updateRegions("LEAD", "REGION");
        updateRegions("LEAD", "CONTACT_REGION");
        updateRegions("SALE", "REGION");
        updateRegions("SECURITY_RULE_REGION", "REGIONS");
        updateRegions("USER_GROUP_REGION", "PERMITREGIONS");
        updateRegions("USER_PROFILE_REGION", "PERMITREGIONS");

        // Переносим старые адреса в новые
        // Почтовые адреса юридических лиц
        convertOldAddress("LEGAL_ENTITY", "POST_ADDRESS", "PST_REGION", "PST_CITY", "PST_STREET_BLD");
        // Адреса сотрудников
        convertOldAddress("EMPLOYEE", "ADDRESS_ID", "REGION", "CITY", "STREET_BLD");


        // Удаляем старые данные об адресах
        template.execute(
                "ALTER TABLE LEGAL_ENTITY \n" +
                        "DROP COLUMN `PST_STREET_BLD`,\n" +
                        "DROP COLUMN `PST_REGION`,\n" +
                        "DROP COLUMN `PST_REALTY_KIND`,\n" +
                        "DROP COLUMN `PST_POST_INDEX`,\n" +
                        "DROP COLUMN `PST_PERIOD_OF_RESIDENCE`,\n" +
                        "DROP COLUMN PST_CITY");
        template.execute(
                "ALTER TABLE `EMPLOYEE` \n" +
                        "DROP COLUMN `STREET_BLD`,\n" +
                        "DROP COLUMN `REGION`,\n" +
                        "DROP COLUMN `REALTY_KIND`,\n" +
                        "DROP COLUMN `POST_INDEX`,\n" +
                        "DROP COLUMN `PERIOD_OF_RESIDENCE`,\n" +
                        "DROP COLUMN `CITY`\n");
        template.execute(
                "ALTER TABLE `PERSON` \n" +
                        "DROP COLUMN `ACT_STREET_BLD`,\n" +
                        "DROP COLUMN `ACT_REGION`,\n" +
                        "DROP COLUMN `ACT_REALTY_KIND`,\n" +
                        "DROP COLUMN `ACT_POST_INDEX`,\n" +
                        "DROP COLUMN `ACT_PERIOD_OF_RESIDENCE`,\n" +
                        "DROP COLUMN `ACT_CITY`\n");
        template.execute(
                "ALTER TABLE CLIENT \n" +
                        "DROP COLUMN `STREET_BLD`,\n" +
                        "DROP COLUMN `REGION`,\n" +
                        "DROP COLUMN `REALTY_KIND`,\n" +
                        "DROP COLUMN `POST_INDEX`,\n" +
                        "DROP COLUMN `PERIOD_OF_RESIDENCE`,\n" +
                        "DROP COLUMN `CITY`\n");
    }

    protected void convertOldAddress(final String table, final String addrIdColumn, final String region, final String city, final String street_bld) {
        final List<Map<String, Object>> postList = template.queryForList(
                "SELECT " +
                        "    ID," +
                        "    CONCAT(" + region + "," +
                        "            \" \"," +
                        "            " + city + "," +
                        "            \" \"," +
                        "            " + street_bld + ") addr " +
                        "FROM" +
                        "    " + table + " " +
                        "WHERE" +
                        "    " + region + " IS NOT NULL" +
                        "        OR " + city + " IS NOT NULL" +
                        "        OR " + street_bld + " IS NOT NULL");
        final List<Address> newAddrList = new ArrayList(128);
        final List<Map<String, Object>> idAddrList = new ArrayList(128);
        for (final Map<String, Object> map : postList) {
            final Address address = getFirst(addressService.filterAddresses((String) map.get("addr")), null);
            if (address != null) {
                final String leId = (String) map.get("ID");
                final String addrId = UUID.randomUUID().toString();
                address.setId(addrId);
                newAddrList.add(address);
                idAddrList.add(ImmutableMap.of("id", leId, "addr_id", addrId));
            }
        }
        insertNewAddresses(newAddrList);
        namedTemplate.batchUpdate(
                "UPDATE " + table + " SET " + addrIdColumn + " = :addr_id WHERE ID = :id",
                SqlParameterSourceUtils.createBatch(idAddrList.toArray(new Map[idAddrList.size()])));
    }

    private void insertNewAddresses(final List<Address> addrList) {
        namedTemplate.batchUpdate(
                "INSERT INTO ADDRESS" +
                        "(`ID`,\n" +
                        "`IS_ARCHIVED`,\n" +
                        "`AREA`,\n" +
                        "`AREA_TYPE`,\n" +
                        "`AREA_TYPE_FULL`,\n" +
                        "`BLOCK`," +
                        "`BLOCK_TYPE`,\n" +
                        "`BLOCK_TYPE_FULL`,\n" +
                        "`CAPITAL_MARKER`,\n" +
                        "`CITY`,\n" +
                        "`CITY_DISTRICT`,\n" +
                        "`CITY_TYPE`,\n" +
                        "`CITY_TYPE_FULL`,\n" +
                        "`COUNTRY`,\n" +
                        "`CREATED_BY`,\n" +
                        "`CREATED_AT`,\n" +
                        "`FIAS_ID`,\n" +
                        "`FIAS_LEVEL`,\n" +
                        "`FLAT`,\n" +
                        "`FLAT_PRICE`,\n" +
                        "`FLAT_TYPE`,\n" +
                        "`FLAT_TYPE_FULL`,\n" +
                        "`FLAT_AREA`,\n" +
                        "`GEO_LAT`,\n" +
                        "`GEO_LON`,\n" +
                        "`HOUSE`,\n" +
                        "`HOUSE_TYPE`,\n" +
                        "`HOUSE_TYPE_FULL`,\n" +
                        "`KLADR_ID`,\n" +
                        "`MODIFIED_BY`,\n" +
                        "`MODIFIED_AT`,\n" +
                        "`OKATO`,\n" +
                        "`OKTMO`,\n" +
                        "`PLAIN`,\n" +
                        "`POSTAL_BOX`,\n" +
                        "`POSTAL_CODE`,\n" +
                        "`QC`,\n" +
                        "`QC_COMPLETE`,\n" +
                        "`QC_GEO`,\n" +
                        "`QC_HOUSE`,\n" +
                        "`REGION`,\n" +
                        "`REGION_TYPE`,\n" +
                        "`REGION_TYPE_FULL`,\n" +
                        "`SETTLEMENT`,\n" +
                        "`SETTLEMENT_TYPE`,\n" +
                        "`SETTLEMENT_TYPE_FULL`,\n" +
                        "`SQUARE_METER_PRICE`,\n" +
                        "`STREET`,\n" +
                        "`STREET_TYPE`,\n" +
                        "`STREET_TYPE_FULL`,\n" +
                        "`TAX_OFFICE`,\n" +
                        "`TAX_OFFICE_LEGAL`,\n" +
                        "`TIMEZONE`,\n" +
                        "`UNPARSED_PARTS`,\n" +
                        "`UNRESTRICTED_VALUE`,\n" +
                        "`VALUE`,\n" +
                        "`VERSION`,\n" +
                        "`AREA_WITH_TYPE`,\n" +
                        "`CITY_WITH_TYPE`,\n" +
                        "`REGION_FIAS_ID`,\n" +
                        "`REGION_KLADR_ID`,\n" +
                        "`REGION_WITH_TYPE`,\n" +
                        "`SETTLEMENT_WITH_TYPE`,\n" +
                        "`STREET_WITH_TYPE`)\n" +
                        "VALUES\n" +
                        "(:id,\n" +
                        "0,\n" +
                        ":area,\n" +
                        ":areaType,\n" +
                        ":areaTypeFull,\n" +
                        ":block,\n" +
                        ":blockType,\n" +
                        ":blockTypeFull,\n" +
                        ":capitalMarker,\n" +
                        ":city,\n" +
                        ":cityDistrict,\n" +
                        ":cityType,\n" +
                        ":cityTypeFull,\n" +
                        ":country,\n" +
                        "\"admin\",\n" +
                        "current_timestamp(),\n" +
                        ":fiasId,\n" +
                        ":fiasLevel,\n" +
                        ":flat,\n" +
                        ":flatPrice,\n" +
                        ":flatType,\n" +
                        ":flatTypeFull,\n" +
                        ":flatArea,\n" +
                        ":geoLat,\n" +
                        ":geoLon,\n" +
                        ":house,\n" +
                        ":houseType,\n" +
                        ":houseTypeFull,\n" +
                        ":kladrId,\n" +
                        "\"admin\",\n" +
                        "current_timestamp(),\n" +
                        ":okato,\n" +
                        ":oktmo,\n" +
                        ":plain,\n" +
                        ":postalBox,\n" +
                        ":postalCode,\n" +
                        ":qc,\n" +
                        ":qcComplete,\n" +
                        ":qcGeo,\n" +
                        ":qcHouse,\n" +
                        ":region,\n" +
                        ":regionType,\n" +
                        ":regionTypeFull,\n" +
                        ":settlement,\n" +
                        ":settlementType,\n" +
                        ":settlementTypeFull,\n" +
                        ":squareMeterPrice,\n" +
                        ":street,\n" +
                        ":streetType,\n" +
                        ":streetTypeFull,\n" +
                        ":taxOffice,\n" +
                        ":taxOfficeLegal,\n" +
                        ":timezone,\n" +
                        ":unparsedParts,\n" +
                        ":unrestrictedValue,\n" +
                        ":value,\n" +
                        "1,\n" +
                        ":areaWithType,\n" +
                        ":cityWithType,\n" +
                        ":regionFiasId,\n" +
                        ":regionKladrId,\n" +
                        ":regionWithType,\n" +
                        ":settlementWithType,\n" +
                        ":streetWithType)\n",
                SqlParameterSourceUtils.createBatch(addrList.toArray()));
    }

    protected void updateRegions(final String table, final String column) {
        namedTemplate.batchUpdate(
                MessageFormat.format("UPDATE {0} SET {1} = :nameWithType WHERE {1} = :oldName", table, column),
                SqlParameterSourceUtils.createBatch(addressService.findAllRegions().toArray()));
    }

    private void updateAddressesWithComplexData() {
        final List<Address> updAddrList = new ArrayList(3500);
        final List<Map<String, Object>> adrList = template.queryForList("SELECT ID, VALUE FROM ADDRESS");
        for (final Map<String, Object> map : adrList) {
            final Address address = getFirst(addressService.filterAddresses((String) map.get("VALUE")), null);
            if (address != null) {
                address.setId((String) map.get("ID"));
                updAddrList.add(address);
            }
        }

        namedTemplate.batchUpdate(
                "UPDATE ADDRESS " +
                        "SET " +
                        "AREA_WITH_TYPE = :areaWithType, " +
                        "CITY_WITH_TYPE = :cityWithType, " +
                        "REGION_FIAS_ID = :regionFiasId, " +
                        "REGION_KLADR_ID = :regionKladrId, " +
                        "REGION_WITH_TYPE = :regionWithType, " +
                        "SETTLEMENT_WITH_TYPE = :settlementWithType, " +
                        "STREET_WITH_TYPE = :streetWithType " +
                        "WHERE ID = :id",
                SqlParameterSourceUtils.createBatch(updAddrList.toArray()
                ));
    }
}
