package ru.extas.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.extas.model.common.Address;

import java.io.IOException;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Десирализатор для dadata адресов.
 *
 * @author sandarkin
 * @version 2.0
 * @since 2.0
 */
public class AddressJsonDeserializer extends JsonDeserializer<Address> {
    @Override
    public Address deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        JsonNode data = node.get("data");
        Address address = Address.builder()
                .value(node.get("value").asText())
                .unrestrictedValue(node.get("unrestricted_value").asText())

                .postalCode(asText(data, "postal_code"))
                .postalBox(asText(data, "postal_box"))

                .country(asText(data, "country"))

                .regionFiasId(asText(data, "region_fias_id"))
                .regionKladrId(asText(data, "region_kladr_id"))
                .regionWithType(asText(data, "region_with_type"))
                .regionTypeFull(asText(data, "region_type_full"))
                .regionType(asText(data, "region_type"))
                .region(asText(data, "region"))

                .areaWithType(asText(data, "area_with_type"))
                .areaTypeFull(asText(data, "area_type_full"))
                .areaType(asText(data, "area_type"))
                .area(asText(data, "area"))

                .cityWithType(asText(data, "city_with_type"))
                .cityTypeFull(asText(data, "city_type_full"))
                .cityType(asText(data, "city_type"))
                .city(asText(data, "city"))
                .cityDistrict(asText(data, "city_district"))

                .settlementWithType(asText(data, "settlement_with_type"))
                .settlementTypeFull(asText(data, "settlement_type_full"))
                .settlementType(asText(data, "settlement_type"))
                .settlement(asText(data, "settlement"))

                .streetWithType(asText(data, "street_with_type"))
                .streetTypeFull(asText(data, "street_type_full"))
                .streetType(asText(data, "street_type"))
                .street(asText(data, "street"))

                .houseTypeFull(asText(data, "house_type_full"))
                .houseType(asText(data, "house_type"))
                .house(asText(data, "house"))

                .blockTypeFull(asText(data, "block_type_full"))
                .blockType(asText(data, "block_type"))
                .block(asText(data, "block_type"))

                .flat(asText(data, "flat"))

                .fiasId(asText(data, "fias_id"))
                .fiasLevel(asText(data, "fias_level"))
                .kladrId(asText(data, "kladr_id"))

                .capitalMarker(asText(data, "capital_marker"))
                .okato(asText(data, "okato"))
                .oktmo(asText(data, "oktmo"))
                .timezone(asText(data, "timezone"))
                .geoLat(asText(data, "geo_lat"))
                .geoLon(asText(data, "geo_lon"))
                .unparsedParts(asText(data, "unparsed_parts"))

                .qcComplete(asText(data, "qc_complete"))
                .qcHouse(asText(data, "qc_house"))
                .qcGeo(asText(data, "qc_geo"))
                .qc(asText(data, "qc"))

                .build();

        // Если заполнен населенный пункт, но нет данных о городе
        if (!isNullOrEmpty(address.getSettlementWithType()) &&
                isNullOrEmpty(address.getCityWithType())) {
            // Приравниваем населенный пункт к городу
            address.setCity(address.getSettlement());
            address.setCityType(address.getSettlementType());
            address.setCityTypeFull(address.getSettlementTypeFull());
            address.setCityWithType(address.getSettlementWithType());
        }

        return address;
    }

    private String asText(JsonNode data, String name) {
        if (data.get(name).isNull()) {
            return null;
        }
        return data.get(name).asText();
    }
}
