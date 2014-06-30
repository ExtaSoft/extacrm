package ru.extas.server.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.extas.server.motor.MotorBrandRepository;
import ru.extas.server.motor.MotorTypeRepository;
import ru.extas.web.commons.HelpContent;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;

/**
 * Точка доступа для получения справочной информации
 *
 * @author Valery Orlov
 *         Date: 09.04.2014
 *         Time: 18:48
 * @version $Id: $Id
 * @since 0.4.2
 */
@Controller
@RequestMapping("/service/ref")
public class ReferenceRestService {
    private Logger logger = LoggerFactory.getLogger(ReferenceRestService.class);

    @Inject private SupplementService supplementService;
    @Inject private MotorBrandRepository motorBrandRepository;
    @Inject private MotorTypeRepository motorTypeRepository;

    /**
     * <p>loadRegions.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public @ResponseBody Collection<String> loadRegions() {
        return supplementService.loadRegions();
    }

    /**
     * <p>loadMotorBrands.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    @RequestMapping(value = "/motor-brands", method = RequestMethod.GET)
    public @ResponseBody Collection<String> loadMotorBrands() {
        return motorBrandRepository.loadAllNames();
    }

    /**
     * <p>loadMotorTypes.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    @RequestMapping(value = "/motor-types", method = RequestMethod.GET)
    public @ResponseBody Collection<String> loadMotorTypes() {
        return motorTypeRepository.loadAllNames();
    }

    /**
     * <p>info.</p>
     *
     * @return a {@link org.springframework.http.HttpEntity} object.
     * @throws java.io.IOException if any.
     */
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<String> info() throws IOException {
        String help = HelpContent.loadMarkDown("/help/rest/references.textile");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        return new HttpEntity(help, headers);
    }

    /**
     * <p>handleIOException.</p>
     *
     * @param ex a {@link java.lang.Throwable} object.
     * @return a {@link java.lang.String} object.
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public String handleIOException(Throwable ex) {
        logger.error("Ошибка обработки запроса", ex);
        return ex.getMessage();
    }
}
