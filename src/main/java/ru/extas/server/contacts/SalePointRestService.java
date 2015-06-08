package ru.extas.server.contacts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.model.contacts.SalePoint;
import ru.extas.server.motor.MotorBrandRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.HelpContent;

import javax.inject.Inject;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Предоставляет данные о торговых точках
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 18:48
 * @version $Id: $Id
 * @since 0.3
 */
@RestController
@RequestMapping("/service/salepoint")
public class SalePointRestService {
    private final Logger logger = LoggerFactory.getLogger(SalePointRestService.class);

    @Inject
    private SalePointRepository repository;
    @Inject
    private SupplementService supplementService;
    @Inject
    private MotorBrandRepository brandRepository;

    public static class RestSalePoint {

        private final String id;
        private final String name;
        private final String phone;
        private String region;
        private String city;
        private String postIndex;
        private String streetBld;

        public RestSalePoint(final SalePoint point) {
            id = point.getId();
            name = point.getName();
            phone = point.getPhone();
            final AddressInfo address = point.getRegAddress();
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

        public String getPhone() {
            return phone;
        }
    }

    /**
     * <p>Общая информация о сервисе.</p>
     *
     * @return a {@link org.springframework.http.HttpEntity} object.
     * @throws java.io.IOException if any.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> info() throws IOException {

        final String help = HelpContent.loadMarkDown("/help/rest/salepoints.textile");

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity(help, headers, HttpStatus.OK);
    }

    /**
     * <p>Количество объектов</p>
     *
     * @param region a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.HttpEntity} object.
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET, headers = "charset=utf-8")
    public ResponseEntity<String> count(
            @RequestParam(value = "region", required = false) final List<String> region,
            @RequestParam(value = "brand", required = false) final List<String> brands) {
        final long count;
        if (isEmpty(region) && isEmpty(brands))
            count = repository.countActual();
        else if (isEmpty(brands)) {
            checkRegions(region);
            count = repository.countActualByRegion(region);
        } else if (isEmpty(region)) {
            checkBrands(brands);
            count = repository.countActualByBrand(brands);
        } else {
            checkRegions(region);
            checkBrands(brands);
            count = repository.countActualByRegionAndBrand(region, brands);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity(NumberFormat.getInstance().format(count), headers, HttpStatus.OK);
    }

    private void checkBrands(final List<String> brands) {
        final List<String> allBrands = brandRepository.loadAllNames();
        for (final String brand : brands) {
            if (!allBrands.contains(brand))
                throw new IllegalArgumentException(MessageFormat.format("Неверное наименование брэнда: ''{0}''", brand));
        }
    }

    private void checkRegions(final List<String> region) {
        final Collection<String> allRegions = supplementService.loadRegions();
        for (final String reg : region) {
            if (!allRegions.contains(reg))
                throw new IllegalArgumentException(MessageFormat.format("Неверное наименование региона: ''{0}''", reg));
        }
    }

    /**
     * <p>Список объектов.</p>
     *
     * @param region a {@link java.lang.String} object.
     * @return a {@link org.springframework.http.HttpEntity} object.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<RestSalePoint> list(
            @RequestParam(value = "region", required = false) final List<String> region,
            @RequestParam(value = "brand", required = false) final List<String> brands,
            @RequestParam(value = "page", required = false) final Integer page,
            @RequestParam(value = "size", required = false) final Integer size) {
        final List<RestSalePoint> result = newArrayList();
        final List<SalePoint> salePoints;

        Pageable pageable = null;
        if(page != null && size != null)
            pageable = new PageRequest(page, size);

        if (isEmpty(region) && isEmpty(brands))
            salePoints = repository.findActual(pageable);
        else if (isEmpty(brands)) {
            checkRegions(region);
            salePoints = repository.findActualByRegion(region, pageable);
        } else if (isEmpty(region)) {
            checkBrands(brands);
            salePoints = repository.findActualByBrand(brands, pageable);
        } else {
            checkRegions(region);
            checkBrands(brands);
            salePoints = repository.findActualByRegionAndBrand(region, brands, pageable);
        }
        result.addAll(salePoints.stream().map(RestSalePoint::new).collect(java.util.stream.Collectors.toList()));

        return result;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<String> handleIOException(final Throwable ex) {
        logger.error("Ошибка обработки REST-запроса", ex);
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity(ex.getMessage(), headers, HttpStatus.EXPECTATION_FAILED);
    }
}
