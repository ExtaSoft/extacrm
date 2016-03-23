package ru.extas.server.lead;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Strings;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.extas.model.contacts.Employee;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.CuratorsGroup;
import ru.extas.server.common.AddressAccessService;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.motor.MotorBrandRepository;
import ru.extas.server.motor.MotorTypeRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.HelpContent;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.System.lineSeparator;
import static ru.extas.model.common.ModelUtils.evictCache;

/**
 * Предоставляет точку доступа для ввода лида
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 18:48
 * @version $Id: $Id
 * @since 0.3
 */
@RestController
@RequestMapping("/service/lead")
public class LeadRestService {

    private final Logger logger = LoggerFactory.getLogger(LeadRestService.class);

    @Inject
    private LeadRepository leadRepository;
    @Inject
    private SalePointRepository salePointRepository;
    @Inject
    private UserManagementService userService;
    @Inject
    private AddressAccessService addressAccessService;
    @Inject
    private MotorBrandRepository motorBrandRepository;
    @Inject
    private MotorTypeRepository motorTypeRepository;
    @Inject
    private EntityManager entityManager;

    @JsonAutoDetect
    public static class RestLead {

        // Имя клиента.
        private String name;

        // Телефон
        private String phone;

        // E-mail
        private String email;

        // Регион проживания.
        private String clientRegion;

        // Тип техники.
        private String motorType;

        // Марка техники.
        private String motorBrand;

        // Модель техники.
        private String motorModel;

        // Цена техники.
        private String price;

        // Регион покупки.
        private String delerRegion;

        // Мотосалон.
        private String dealer;

        // Идентификатор мотосалона.
        private String dealerId;

        // Примечание.
        private String comment;

        public RestLead() {
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(final String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(final String email) {
            this.email = email;
        }

        public String getClientRegion() {
            return clientRegion;
        }

        public void setClientRegion(final String clientRegion) {
            this.clientRegion = clientRegion;
        }

        public String getMotorType() {
            return motorType;
        }

        public void setMotorType(final String motorType) {
            this.motorType = motorType;
        }

        public String getMotorBrand() {
            return motorBrand;
        }

        public void setMotorBrand(final String motorBrand) {
            this.motorBrand = motorBrand;
        }

        public String getMotorModel() {
            return motorModel;
        }

        public void setMotorModel(final String motorModel) {
            this.motorModel = motorModel;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(final String price) {
            this.price = price;
        }

        public String getDelerRegion() {
            return delerRegion;
        }

        public void setDelerRegion(final String delerRegion) {
            this.delerRegion = delerRegion;
        }

        public String getDealer() {
            return dealer;
        }

        public void setDealer(final String dealer) {
            this.dealer = dealer;
        }

        public String getDealerId() {
            return dealerId;
        }

        public void setDealerId(final String dealerId) {
            this.dealerId = dealerId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(final String comment) {
            this.comment = comment;
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
        final String help = HelpContent.loadMarkDown("/help/rest/leads.textile");

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity(help, headers, HttpStatus.OK);
    }

    /**
     * <p>Создает новый объект.</p>
     *
     * @param lead a {@link ru.extas.server.lead.LeadRestService.RestLead} object.
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void newLead(@RequestBody final RestLead lead) {
        Lead newLead = new Lead();
        newLead.setStatus(Lead.Status.NEW);

        final StringBuilder dirtyData = new StringBuilder();

        // Проверяем входные данные и копируем их в лид:
        // Имя клиента.
        if (isNullOrEmpty(lead.getName()))
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        newLead.setContactName(lead.getName());

        // Телефон
        if (isNullOrEmpty(lead.getPhone()))
            throw new IllegalArgumentException("Телефон клиента не может быть пустым");
        final String dirtyPhone = lead.getPhone();
        final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phone = null;
        try {
            phone = phoneUtil.parse(dirtyPhone, "RU");
        } catch (final NumberParseException e) {
        }
        if (phone != null) {
            final String clearPhone = phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            newLead.setContactPhone(clearPhone);
        } else {
            dirtyData.append("Телефон: ").append(dirtyPhone).append(lineSeparator());
        }

        // E-mail
        newLead.setContactEmail(
                Optional.ofNullable(lead.getEmail()).
                        map(em -> Strings.emptyToNull(em.trim())).orElse(null));

        // Регион проживания.
        final String dirtyClientRegion = lead.getClientRegion();
        if (!isNullOrEmpty(dirtyClientRegion)) {
            final String clearClientRegion = addressAccessService.clarifyRegion(dirtyClientRegion);
            if (isNullOrEmpty(clearClientRegion))
                dirtyData.append("Регион проживания: ").append(dirtyClientRegion).append(lineSeparator());
            else
                newLead.setContactRegion(clearClientRegion);
        }

        // Тип техники.
        final String dirtyMotorType = lead.getMotorType();
        if (!isNullOrEmpty(dirtyMotorType)) {
            final String clearMotorType = motorTypeRepository.clarifyType(dirtyMotorType);
            if (isNullOrEmpty(clearMotorType))
                dirtyData.append("Тип техники: ").append(dirtyMotorType).append(lineSeparator());
            else
                newLead.setMotorType(clearMotorType);
        }

        // Марка техники.
        final String dirtyBrand = lead.getMotorBrand();
        if (!isNullOrEmpty(dirtyBrand)) {
            final String clearBrand = motorBrandRepository.clarifyBrand(dirtyBrand);
            if (isNullOrEmpty(clearBrand))
                dirtyData.append("Марка техники: ").append(dirtyMotorType).append(lineSeparator());
            else
                newLead.setMotorBrand(clearBrand);
        }

        // Модель техники.
        newLead.setMotorModel(lead.getMotorModel());

        // Цена техники.
        if (!isNullOrEmpty(lead.getPrice())) {
            final String dirtyPrice = lead.getPrice().trim().replace(" ", "").replace("'", "");
            final DecimalFormat format = new DecimalFormat();
            format.setParseBigDecimal(true);
            format.setDecimalFormatSymbols(new DecimalFormatSymbols() {
                {
                    setDecimalSeparator(dirtyPrice.contains(".") ? '.' : ',');
                }
            });
            BigDecimal clearPrice = null;
            try {
                clearPrice = (BigDecimal) format.parse(dirtyPrice);
            } catch (final ParseException e) {
            }
            if (clearPrice == null)
                dirtyData.append("Цена техники: ").append(dirtyPrice).append(lineSeparator());
            else
                newLead.setMotorPrice(clearPrice);
        }

        // Регион покупки.
        final String dirtyDealRegion = lead.getDelerRegion();
        if (!isNullOrEmpty(dirtyDealRegion)) {
            final String clearDealRegion = addressAccessService.clarifyRegion(dirtyDealRegion);
            if (isNullOrEmpty(clearDealRegion))
                dirtyData.append("Регион покупки: ").append(dirtyDealRegion).append(lineSeparator());
            else
                newLead.setRegion(clearDealRegion);
        }

        // Мотосалон (название или id).
        if (!isNullOrEmpty(lead.getDealerId())) {
            // Найти точку продаж по Id
            final SalePoint salePoint = salePointRepository.findOne(lead.getDealerId());
            if (salePoint == null)
                throw new IllegalArgumentException("Id торговой точки не действителен (не найден)");
            else {
                newLead.setVendor(salePoint);
                newLead.setPointOfSale(salePoint.getName());
                if (salePoint.getRegAddress() != null)
                    newLead.setRegion(salePoint.getRegAddress().getRegion());
                // Ответственные по умолчанию
                final CuratorsGroup curatorsGroup = salePoint.getCuratorsGroup();
                if (curatorsGroup != null && !curatorsGroup.getCurators().isEmpty()) {
                    final Iterator<Employee> employeeIterator = curatorsGroup.getCurators().iterator();
                    newLead.setResponsible(employeeIterator.next());
                    if (employeeIterator.hasNext())
                        newLead.setResponsibleAssist(employeeIterator.next());
                }
            }
        }
        newLead.setPointOfSale(lead.getDealer());

        // Примечание.
        if (!isNullOrEmpty(lead.getComment()))
            dirtyData.append(lead.getComment());
        newLead.setComment(dirtyData.toString());

        newLead = leadRepository.secureSave(newLead);
        evictCache(entityManager, newLead);
    }

    /**
     * <p>handleIOException.</p>
     *
     * @param ex a {@link Throwable} object.
     * @return a {@link java.lang.String} object.
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<String> handleIOException(final Throwable ex) {
        logger.error("Ошибка обработки REST-запроса", ex);
        if (ex instanceof ConstraintViolationException) {
            final ConstraintViolationException ce = (ConstraintViolationException) ex;
            for (final ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                logger.error(violation.getMessage());
            }
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        return new ResponseEntity(ex.getMessage(), headers, HttpStatus.EXPECTATION_FAILED);
    }
}
