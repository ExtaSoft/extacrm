package ru.extas.server.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.extas.model.common.Address;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.tryFind;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * @author sandarkin
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class AddressAccessServiceImpl implements AddressAccessService {

    public static final String DADATA_TOKEN = "Token cabb4f9a54337d80edb2b61bc375c3c8d3ff1c5f";
    public static final String DADATA_API_SUGGEST_ADDRESS = "https://dadata.ru/api/v2/suggest/address";

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
        headers.set("Authorization", DADATA_TOKEN);

        HttpEntity<AddressReq> request = new HttpEntity<>(req, headers);

        AddressSuggestions suggestions = restTemplate.postForObject(DADATA_API_SUGGEST_ADDRESS,
                request, AddressSuggestions.class);

        return suggestions.getSuggestions();
    }

//    /**
//     * Запрашивает список регионов
//     *
//     * @return список регионов
//     */
//    @Override
//    public Collection<String> loadRegions() {
//        List<Address> result = new ArrayList<>();
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        AddressReq req = new AddressReq("б", 100, "region", "region");
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.set("Authorization", DADATA_TOKEN);
//
//        HttpEntity<AddressReq> request = new HttpEntity<>(req, headers);
//
//        AddressSuggestions suggestions = restTemplate.postForObject(DADATA_API_SUGGEST_ADDRESS,
//                request, AddressSuggestions.class);
//
//        final List<Address> addresses = suggestions.getSuggestions();
//        return addresses.stream().map(a -> a.getRegion()).collect(Collectors.toList());
//    }

    @Getter
    @Setter
    public class AddressReq {
        private String query;
        private Integer count;

        private Valued fromBound;
        private Valued toBound;

        public AddressReq(String query, Integer count) {
            this.query = query;
            this.count = count;
        }

        public AddressReq(String query, Integer count, String fromBound, String toBound) {
            this(query, count);
            this.fromBound = new Valued(fromBound);
            this.toBound = new Valued(toBound);
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class Valued {
        private String value;
    }

    static private class RegionsFactory {
        static final List<Region> INSTANCE;

        static {
            INSTANCE = newArrayList(
                    new Region("d8327a56-80de-4df2-815c-4f6ab1224c50", "0100000000000", "респ Адыгея", "респ", "республика", "Адыгея", "Адыгея", "Майкоп"),
                    new Region("5c48611f-5de6-4771-9695-7e36a4e7529d", "0400000000000", "респ Алтай", "респ", "республика", "Алтай", "Алтай", "Горно-Алтайск"),
                    new Region("6f2cbfd8-692a-4ee4-9b16-067210bde3fc", "0200000000000", "респ Башкортостан", "респ", "республика", "Башкортостан", "Башкортостан", "Уфа"),
                    new Region("a84ebed3-153d-4ba9-8532-8bdf879e1f5a", "0300000000000", "респ Бурятия", "респ", "республика", "Бурятия", "Бурятия", "Улан-Удэ"),
                    new Region("0bb7fa19-736d-49cf-ad0e-9774c4dae09b", "0500000000000", "респ Дагестан", "респ", "республика", "Дагестан", "Дагестан", "Махачкала"),
                    new Region("b2d8cd20-cabc-4deb-afad-f3c4b4d55821", "0600000000000", "респ Ингушетия", "респ", "республика", "Ингушетия", "Ингушетия", "Магас"),
                    new Region("1781f74e-be4a-4697-9c6b-493057c94818", "0700000000000", "Кабардино-Балкарская респ", "респ", "республика", "Кабардино-Балкарская", "Кабардино-Балкария", "Нальчик"),
                    new Region("491cde9d-9d76-4591-ab46-ea93c079e686", "0800000000000", "респ Калмыкия", "респ", "республика", "Калмыкия", "Калмыкия", "Элиста"),
                    new Region("61b95807-388a-4cb1-9bee-889f7cf811c8", "0900000000000", "Карачаево-Черкесская респ", "респ", "республика", "Карачаево-Черкесская", "Карачаево-Черкесия", "Черкесск"),
                    new Region("248d8071-06e1-425e-a1cf-d1ff4c4a14a8", "1000000000000", "респ Карелия", "респ", "республика", "Карелия", "Карелия", "Петрозаводск"),
                    new Region("c20180d9-ad9c-46d1-9eff-d60bc424592a", "1100000000000", "респ Коми", "респ", "республика", "Коми", "Коми", "Сыктывкар"),
                    new Region("de2cbfdf-9662-44a4-a4a4-8ad237ae4a3e", "1200000000000", "респ Марий Эл", "респ", "республика", "Марий Эл", "Марий Эл", "Йошкар-Ола"),
                    new Region("37a0c60a-9240-48b5-a87f-0d8c86cdb6e1", "1300000000000", "респ Мордовия", "респ", "республика", "Мордовия", "Мордовия", "Саранск"),
                    new Region("c225d3db-1db6-4063-ace0-b3fe9ea3805f", "1400000000000", "респ Саха /Якутия/", "респ", "республика", "Саха /Якутия/", "Саха (Якутия)", "Якутск"),
                    new Region("de459e9c-2933-4923-83d1-9c64cfd7a817", "1500000000000", "респ Северная Осетия - Алания", "респ", "республика", "Северная Осетия - Алания", "Северная Осетия — Алания", "Владикавказ"),
                    new Region("0c089b04-099e-4e0e-955a-6bf1ce525f1a", "1600000000000", "респ Татарстан", "респ", "республика", "Татарстан", "Татарстан", "Казань"),
                    new Region("026bc56f-3731-48e9-8245-655331f596c0", "1700000000000", "респ Тыва", "респ", "республика", "Тыва", "Тыва (Тува)", "Кызыл"),
                    new Region("52618b9c-bcbb-47e7-8957-95c63f0b17cc", "1800000000000", "Удмуртская респ", "респ", "республика", "Удмуртская", "Удмуртия", "Ижевск"),
                    new Region("8d3f1d35-f0f4-41b5-b5b7-e7cadf3e7bd7", "1900000000000", "респ Хакасия", "респ", "республика", "Хакасия", "Хакасия", "Абакан"),
                    new Region("de67dc49-b9ba-48a3-a4cc-c2ebfeca6c5e", "2000000000000", "Чеченская респ", "респ", "республика", "Чеченская", "Чечня", "Грозный"),
                    new Region("878fc621-3708-46c7-a97f-5a13a4176b3e", "2100000000000", "Чувашская республика - Чувашия", "Чувашия", "Чувашия", "Чувашская республика", "Чувашия", "Чебоксары"),
                    new Region("8276c6a1-1a86-4f0d-8920-aba34d4cc34a", "2200000000000", "Алтайский край", "край", "край", "Алтайский", "Алтайский край", "Барнаул"),
                    new Region("d00e1013-16bd-4c09-b3d5-3cb09fc54bd8", "2300000000000", "Краснодарский край", "край", "край", "Краснодарский", "Краснодарский край", "Краснодар"),
                    new Region("db9c4f8b-b706-40e2-b2b4-d31b98dcd3d1", "2400000000000", "Красноярский край", "край", "край", "Красноярский", "Красноярский край", "Красноярск"),
                    new Region("43909681-d6e1-432d-b61f-ddac393cb5da", "2500000000000", "Приморский край", "край", "край", "Приморский", "Приморский край", "Владивосток"),
                    new Region("327a060b-878c-4fb4-8dc4-d5595871a3d8", "2600000000000", "Ставропольский край", "край", "край", "Ставропольский", "Ставропольский край", "Ставрополь"),
                    new Region("7d468b39-1afa-41ec-8c4f-97a8603cb3d4", "2700000000000", "Хабаровский край", "край", "край", "Хабаровский", "Хабаровский край", "Хабаровск"),
                    new Region("844a80d6-5e31-4017-b422-4d9c01e9942c", "2800000000000", "Амурская обл", "обл", "область", "Амурская", "Амурская область", "Благовещенск"),
                    new Region("294277aa-e25d-428c-95ad-46719c4ddb44", "2900000000000", "Архангельская обл", "обл", "область", "Архангельская", "Архангельская область", "Архангельск"),
                    new Region("83009239-25cb-4561-af8e-7ee111b1cb73", "3000000000000", "Астраханская обл", "обл", "область", "Астраханская", "Астраханская область", "Астрахань"),
                    new Region("639efe9d-3fc8-4438-8e70-ec4f2321f2a7", "3100000000000", "Белгородская обл", "обл", "область", "Белгородская", "Белгородская область", "Белгород"),
                    new Region("f5807226-8be0-4ea8-91fc-39d053aec1e2", "3200000000000", "Брянская обл", "обл", "область", "Брянская", "Брянская область", "Брянск"),
                    new Region("b8837188-39ee-4ff9-bc91-fcc9ed451bb3", "3300000000000", "Владимирская обл", "обл", "область", "Владимирская", "Владимирская область", "Владимир"),
                    new Region("da051ec8-da2e-4a66-b542-473b8d221ab4", "3400000000000", "Волгоградская обл", "обл", "область", "Волгоградская", "Волгоградская область", "Волгоград"),
                    new Region("ed36085a-b2f5-454f-b9a9-1c9a678ee618", "3500000000000", "Вологодская обл", "обл", "область", "Вологодская", "Вологодская область", "Вологда"),
                    new Region("b756fe6b-bbd3-44d5-9302-5bfcc740f46e", "3600000000000", "Воронежская обл", "обл", "область", "Воронежская", "Воронежская область", "Воронеж"),
                    new Region("0824434f-4098-4467-af72-d4f702fed335", "3700000000000", "Ивановская обл", "обл", "область", "Ивановская", "Ивановская область", "Иваново"),
                    new Region("6466c988-7ce3-45e5-8b97-90ae16cb1249", "3800000000000", "Иркутская обл", "обл", "область", "Иркутская", "Иркутская область", "Иркутск"),
                    new Region("90c7181e-724f-41b3-b6c6-bd3ec7ae3f30", "3900000000000", "Калининградская обл", "обл", "область", "Калининградская", "Калининградская область", "Калининград"),
                    new Region("18133adf-90c2-438e-88c4-62c41656de70", "4000000000000", "Калужская обл", "обл", "область", "Калужская", "Калужская область", "Калуга"),
                    new Region("d02f30fc-83bf-4c0f-ac2b-5729a866a207", "4100000000000", "Камчатский край", "край", "край", "Камчатский", "Камчатский край", "Петропавловск-Камчатский"),
                    new Region("393aeccb-89ef-4a7e-ae42-08d5cebc2e30", "4200000000000", "Кемеровская обл", "обл", "область", "Кемеровская", "Кемеровская область", "Кемерово"),
                    new Region("0b940b96-103f-4248-850c-26b6c7296728", "4300000000000", "Кировская обл", "обл", "область", "Кировская", "Кировская область", "Киров"),
                    new Region("15784a67-8cea-425b-834a-6afe0e3ed61c", "4400000000000", "Костромская обл", "обл", "область", "Костромская", "Костромская область", "Кострома"),
                    new Region("4a3d970f-520e-46b9-b16c-50d4ca7535a8", "4500000000000", "Курганская обл", "обл", "область", "Курганская", "Курганская область", "Курган"),
                    new Region("ee594d5e-30a9-40dc-b9f2-0add1be44ba1", "4600000000000", "Курская обл", "обл", "область", "Курская", "Курская область", "Курск"),
                    new Region("6d1ebb35-70c6-4129-bd55-da3969658f5d", "4700000000000", "Ленинградская обл", "обл", "область", "Ленинградская", "Ленинградская область", "Пушкин"),
                    new Region("1490490e-49c5-421c-9572-5673ba5d80c8", "4800000000000", "Липецкая обл", "обл", "область", "Липецкая", "Липецкая область", "Липецк"),
                    new Region("9c05e812-8679-4710-b8cb-5e8bd43cdf48", "4900000000000", "Магаданская обл", "обл", "область", "Магаданская", "Магаданская область", "Магадан"),
                    new Region("29251dcf-00a1-4e34-98d4-5c47484a36d4", "5000000000000", "Московская обл", "обл", "область", "Московская", "Московская область", "Красногорск"),
                    new Region("1c727518-c96a-4f34-9ae6-fd510da3be03", "5100000000000", "Мурманская обл", "обл", "область", "Мурманская", "Мурманская область", "Мурманск"),
                    new Region("88cd27e2-6a8a-4421-9718-719a28a0a088", "5200000000000", "Нижегородская обл", "обл", "область", "Нижегородская", "Нижегородская область", "Нижний Новгород"),
                    new Region("e5a84b81-8ea1-49e3-b3c4-0528651be129", "5300000000000", "Новгородская обл", "обл", "область", "Новгородская", "Новгородская область", "Великий Новгород"),
                    new Region("1ac46b49-3209-4814-b7bf-a509ea1aecd9", "5400000000000", "Новосибирская обл", "обл", "область", "Новосибирская", "Новосибирская область", "Новосибирск"),
                    new Region("05426864-466d-41a3-82c4-11e61cdc98ce", "5500000000000", "Омская обл", "обл", "область", "Омская", "Омская область", "Омск"),
                    new Region("8bcec9d6-05bc-4e53-b45c-ba0c6f3a5c44", "5600000000000", "Оренбургская обл", "обл", "область", "Оренбургская", "Оренбургская область", "Оренбург"),
                    new Region("5e465691-de23-4c4e-9f46-f35a125b5970", "5700000000000", "Орловская обл", "обл", "область", "Орловская", "Орловская область", "Орёл"),
                    new Region("c99e7924-0428-4107-a302-4fd7c0cca3ff", "5800000000000", "Пензенская обл", "обл", "область", "Пензенская", "Пензенская область", "Пенза"),
                    new Region("4f8b1a21-e4bb-422f-9087-d3cbf4bebc14", "5900000000000", "Пермский край", "край", "край", "Пермский", "Пермский край", "Пермь"),
                    new Region("f6e148a1-c9d0-4141-a608-93e3bd95e6c4", "6000000000000", "Псковская обл", "обл", "область", "Псковская", "Псковская область", "Псков"),
                    new Region("f10763dc-63e3-48db-83e1-9c566fe3092b", "6100000000000", "Ростовская обл", "обл", "область", "Ростовская", "Ростовская область", "Ростов-на-Дону"),
                    new Region("963073ee-4dfc-48bd-9a70-d2dfc6bd1f31", "6200000000000", "Рязанская обл", "обл", "область", "Рязанская", "Рязанская область", "Рязань"),
                    new Region("df3d7359-afa9-4aaa-8ff9-197e73906b1c", "6300000000000", "Самарская обл", "обл", "область", "Самарская", "Самарская область", "Самара"),
                    new Region("df594e0e-a935-4664-9d26-0bae13f904fe", "6400000000000", "Саратовская обл", "обл", "область", "Саратовская", "Саратовская область", "Саратов"),
                    new Region("aea6280f-4648-460f-b8be-c2bc18923191", "6500000000000", "Сахалинская обл", "обл", "область", "Сахалинская", "Сахалинская область", "Южно-Сахалинск"),
                    new Region("92b30014-4d52-4e2e-892d-928142b924bf", "6600000000000", "Свердловская обл", "обл", "область", "Свердловская", "Свердловская область", "Екатеринбург"),
                    new Region("e8502180-6d08-431b-83ea-c7038f0df905", "6700000000000", "Смоленская обл", "обл", "область", "Смоленская", "Смоленская область", "Смоленск"),
                    new Region("a9a71961-9363-44ba-91b5-ddf0463aebc2", "6800000000000", "Тамбовская обл", "обл", "область", "Тамбовская", "Тамбовская область", "Тамбов"),
                    new Region("61723327-1c20-42fe-8dfa-402638d9b396", "6900000000000", "Тверская обл", "обл", "область", "Тверская", "Тверская область", "Тверь"),
                    new Region("889b1f3a-98aa-40fc-9d3d-0f41192758ab", "7000000000000", "Томская обл", "обл", "область", "Томская", "Томская область", "Томск"),
                    new Region("d028ec4f-f6da-4843-ada6-b68b3e0efa3d", "7100000000000", "Тульская обл", "обл", "область", "Тульская", "Тульская область", "Тула"),
                    new Region("54049357-326d-4b8f-b224-3c6dc25d6dd3", "7200000000000", "Тюменская обл", "обл", "область", "Тюменская", "Тюменская область", "Тюмень"),
                    new Region("fee76045-fe22-43a4-ad58-ad99e903bd58", "7300000000000", "Ульяновская обл", "обл", "область", "Ульяновская", "Ульяновская область", "Ульяновск"),
                    new Region("27eb7c10-a234-44da-a59c-8b1f864966de", "7400000000000", "Челябинская обл", "обл", "область", "Челябинская", "Челябинская область", "Челябинск"),
                    new Region("b6ba5716-eb48-401b-8443-b197c9578734", "7500000000000", "Забайкальский край", "край", "край", "Забайкальский", "Забайкальский край", "Чита"),
                    new Region("a84b2ef4-db03-474b-b552-6229e801ae9b", "7600000000000", "Ярославская обл", "обл", "область", "Ярославская", "Ярославская область", "Ярославль"),
                    new Region("0c5b2444-70a0-4932-980c-b4dc0d3f02b5", "7700000000000", "г Москва", "г", "город", "Москва", "Москва", "Москва"),
                    new Region("c2deb16a-0330-4f05-821f-1d09c93331e6", "7800000000000", "г Санкт-Петербург", "г", "город", "Санкт-Петербург", "Санкт-Петербург", "Санкт-Петербург"),
                    new Region("1b507b09-48c9-434f-bf6f-65066211c73e", "7900000000000", "Еврейская аобл", "аобл", "автономная область", "Еврейская", "Еврейская АО", "Биробиджан"),
                    new Region("89db3198-6803-4106-9463-cbf781eff0b8", "8300000000000", "Ненецкий ао", "ао", "автономный округ", "Ненецкий", "Ненецкий АО", "Нарьян-Мар"),
                    new Region("d66e5325-3a25-4d29-ba86-4ca351d9704b", "8600000000000", "Ханты-Мансийский Автономный округ - Югра", "ао", "автономный округ", "Ханты-Мансийский Автономный округ - Югра", "Ханты-Мансийский АО — Югра", "Ханты-Мансийск"),
                    new Region("f136159b-404a-4f1f-8d8d-d169e1374d5c", "8700000000000", "Чукотский ао", "ао", "автономный округ", "Чукотский", "Чукотский АО", "Анадырь"),
                    new Region("826fa834-3ee8-404f-bdbc-13a5221cfb6e", "8900000000000", "Ямало-Ненецкий ао", "ао", "автономный округ", "Ямало-Ненецкий", "Ямало-Ненецкий АО", "Салехард"),
                    new Region("bd8e6511-e4b9-4841-90de-6bbc231a789e", "9100000000000", "респ Крым", "респ", "республика", "Крым", "Крым", "Симферополь"),
                    new Region("6fdecb78-893a-4e3f-a5ba-aa062459463b", "9200000000000", "г Севастополь", "г", "город", "Севастополь", "Севастополь", "Севастополь")
            );
        }
    }

    private List<Region> getRegionCapitals() {
        return RegionsFactory.INSTANCE;
    }

    @Override
    public Collection<String> loadRegions() {
        return getRegionCapitals().stream().map(a -> a.getNameWithType()).collect(Collectors.toList());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> loadCities() {
        return getRegionCapitals().stream().map(a -> a.getCapital()).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String findCityByRegion(final String region) {
        return getRegionCapitals().stream()
                .filter(r -> r.getNameWithType().equalsIgnoreCase(region))
                .findFirst().map(r -> r.getCapital()).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String findRegionByCity(final String city) {

        return getRegionCapitals().stream()
                .filter(r -> r.getCapital().equalsIgnoreCase(city))
                .findFirst().map(r -> r.getNameWithType()).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String clarifyRegion(String dirtyClientRegion) {
        dirtyClientRegion = dirtyClientRegion.trim();
        final Collection<String> regions = loadRegions();
        final String finalDirtyClientRegion = dirtyClientRegion;
        return tryFind(regions, input -> containsIgnoreCase(input, finalDirtyClientRegion)).orNull();
    }

}
