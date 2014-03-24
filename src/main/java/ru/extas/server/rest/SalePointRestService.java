package ru.extas.server.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.SalePoint;
import ru.extas.server.SalePointRepository;

import javax.inject.Inject;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Предоставляет данные о торговых точках
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 18:48
 * @version $Id: $Id
 * @since 0.3
 */
@Controller
@RequestMapping("/service/salepoint")
public class SalePointRestService {

    @Inject
    private SalePointRepository repository;

    public class RestSalePoint {

        private final String id;
        private final String name;
        private String region;
        private String city;
        private String postIndex;
        private String streetBld;

        public RestSalePoint(SalePoint point) {
            id = point.getId();
            name = point.getName();
            AddressInfo address = point.getActualAddress();
            if (address != null) {
                region = address.getRegion();
                city = address.getCity();
                postIndex = address.getPostIndex();
                streetBld = address.getStreetBld();
            }
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public String getCity() {
            return city;
        }

        public String getPostIndex() {
            return postIndex;
        }

        public String getStreetBld() {
            return streetBld;
        }
    }

    public class Greeting {

        private final long id;
        private final String content;

        public Greeting(long id, String content) {
            this.id = id;
            this.content = content;
        }

        public long getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /**
     * <p>greeting.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link ru.extas.server.rest.SalePointRestService.Greeting} object.
     */
    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public
    @ResponseBody
    Greeting greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    // Общая информация о сервисе
    /**
     * <p>info.</p>
     *
     * @return a {@link org.springframework.http.HttpEntity} object.
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<String> info() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/plain; charset=utf-8");
        return new HttpEntity("Точка доступа к информации о торговых точках", headers);
    }

    // количество объектов
    /**
     * <p>count.</p>
     *
     * @return a {@link org.springframework.http.HttpEntity} object.
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public HttpEntity<String> count() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/plain; charset=utf-8");
        return new HttpEntity(NumberFormat.getInstance().format(repository.count()), headers);
    }

    // количество объектов
    /**
     * <p>list.</p>
     *
     * @return a {@link org.springframework.http.HttpEntity} object.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public HttpEntity<Iterable> list() {
        List<RestSalePoint> points = newArrayList();
        for (SalePoint point : repository.findAll()) {
            points.add(new RestSalePoint(point));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        return new HttpEntity<Iterable>(points, headers);
    }
}
