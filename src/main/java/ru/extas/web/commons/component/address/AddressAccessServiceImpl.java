package ru.extas.web.commons.component.address;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import ru.extas.model.common.Address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sandarkin
 */
public class AddressAccessServiceImpl implements AddressAccessService {

    public AddressAccessServiceImpl() {
    }

    @Override
    public List<Address> filterAddresses(String filterPrefix) {
        if ("".equals(filterPrefix) || filterPrefix == null) {
            return Collections.emptyList();
        }
        List<Address> result = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        AddressReq req = new AddressReq(filterPrefix, 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Token f3f022fd40bceb758e0dc1abb4dd23561e81114f");

        HttpEntity<AddressReq> request = new HttpEntity<>(req, headers);

        AddressSuggestions suggestions = restTemplate.postForObject("https://dadata.ru/api/v2/suggest/address",
                request, AddressSuggestions.class);

        return suggestions.getSuggestions();
    }

    @Getter
    @Setter
    public class AddressReq {
        private String query;
        private Integer count;

        public AddressReq(String query, Integer count) {
            this.query = query;
            this.count = count;
        }

    }

}
