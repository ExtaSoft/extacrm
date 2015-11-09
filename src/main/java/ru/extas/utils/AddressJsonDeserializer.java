package ru.extas.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.extas.model.common.Address;

import java.io.IOException;

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

                .postalCode(data.get("postal_code").asText())
                .postalBox(data.get("postal_box").asText())

                .country(data.get("country").asText())

                .regionTypeFull(data.get("region_type_full").asText())
                .regionType(data.get("region_type").asText())
                .region(data.get("region").asText())

                .areaTypeFull(data.get("area_type_full").asText())
                .areaType(data.get("area_type").asText())
                .area(data.get("area").asText())

                .cityTypeFull(data.get("city_type_full").asText())
                .cityType(data.get("city_type").asText())
                .city(data.get("city").asText())
                .cityDistrict(data.get("city_district").asText())

                .settlementTypeFull(data.get("settlement_type_full").asText())
                .settlementType(data.get("settlement_type").asText())
                .settlement(data.get("settlement").asText())

                .streetTypeFull(data.get("street_type_full").asText())
                .streetType(data.get("street_type").asText())
                .street(data.get("street").asText())

                .houseTypeFull(data.get("house_type_full").asText())
                .houseType(data.get("house_type").asText())
                .house(data.get("house").asText())

                .blockTypeFull(data.get("block_type_full").asText())
                .blockType(data.get("block_type").asText())
                .block(data.get("block_type").asText())

                .flat(data.get("flat").asText())

                .fiasId(data.get("fias_id").asText())
                .fiasLevel(data.get("fias_level").asText())
                .kladrId(data.get("kladr_id").asText())

                .capitalMarker(data.get("capital_marker").asText())
                .okato(data.get("okato").asText())
                .oktmo(data.get("oktmo").asText())
                .timezone(data.get("timezone").asText())
                .geoLat(data.get("geo_lat").asText())
                .geoLon(data.get("geo_lon").asText())
                .unparsedParts(data.get("unparsed_parts").asText())

                .qcComplete(data.get("qc_complete").asText())
                .qcHouse(data.get("qc_house").asText())
                .qcGeo(data.get("qc_geo").asText())
                .qc(data.get("qc").asText())

                .build();
        return address;
    }
}
