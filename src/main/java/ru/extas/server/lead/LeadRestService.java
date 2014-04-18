package ru.extas.server.lead;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.lead.Lead;
import ru.extas.server.contacts.SalePointRepository;
import ru.extas.server.references.SupplementService;
import ru.extas.server.users.UserManagementService;
import ru.extas.web.commons.HelpContent;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Предоставляет точку доступа для ввода лида
 *
 * @author Valery Orlov
 *         Date: 18.03.14
 *         Time: 18:48
 * @version $Id: $Id
 * @since 0.3
 */
@Controller
@RequestMapping("/service/lead")
public class LeadRestService {

    private Logger logger = LoggerFactory.getLogger(LeadRestService.class);

    @Inject private LeadRepository leadRepository;
    @Inject private SalePointRepository salePointRepository;
    @Inject private UserManagementService userService;
    @Inject private SupplementService supplementService;

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
        private BigDecimal price;

        // Регион покупки.
        private String delerRegion;

        // Мотосалон.
        private String dealer;

        // Идентификатор мотосалона.
        private String dealerId;

        // Комментарий.
        private String comment;

        public RestLead() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getClientRegion() {
            return clientRegion;
        }

        public void setClientRegion(String clientRegion) {
            this.clientRegion = clientRegion;
        }

        public String getMotorType() {
            return motorType;
        }

        public void setMotorType(String motorType) {
            this.motorType = motorType;
        }

        public String getMotorBrand() {
            return motorBrand;
        }

        public void setMotorBrand(String motorBrand) {
            this.motorBrand = motorBrand;
        }

        public String getMotorModel() {
            return motorModel;
        }

        public void setMotorModel(String motorModel) {
            this.motorModel = motorModel;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getDelerRegion() {
            return delerRegion;
        }

        public void setDelerRegion(String delerRegion) {
            this.delerRegion = delerRegion;
        }

        public String getDealer() {
            return dealer;
        }

        public void setDealer(String dealer) {
            this.dealer = dealer;
        }

        public String getDealerId() {
            return dealerId;
        }

        public void setDealerId(String dealerId) {
            this.dealerId = dealerId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
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
    public HttpEntity<String> info() throws IOException {
        String help = HelpContent.loadMarkDown("/help/rest/leads.textile");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/html; charset=utf-8");
        return new HttpEntity(help, headers);
    }

    /**
     * <p>Создает новый объект.</p>
     *
     * @param lead a {@link ru.extas.server.lead.LeadRestService.RestLead} object.
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public void newLead(@RequestBody RestLead lead) {
        Lead newLead = new Lead();
        newLead.setStatus(Lead.Status.NEW);

        // Проверяем входные данные и копируем их в лид:
        // Имя клиента.
        if(isNullOrEmpty(lead.getName()))
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        newLead.setContactName(lead.getName());

        // Телефон
        if(isNullOrEmpty(lead.getPhone()))
            throw new IllegalArgumentException("Телефон клиента не может быть пустым");
        newLead.setContactPhone(lead.getPhone());

        // E-mail
        newLead.setContactEmail(lead.getEmail());

        // Регион проживания.
        if(isNullOrEmpty(lead.getClientRegion()))
            throw new IllegalArgumentException("Регион проживания клиента не может быть пустым");
        else if(!supplementService.loadRegions().contains(lead.getClientRegion()))
            throw new IllegalArgumentException("Неверный регион проживания клиента");
        newLead.setContactRegion(lead.getClientRegion());

        // Тип техники.
        if(isNullOrEmpty(lead.getMotorType()))
            throw new IllegalArgumentException("Тип техники не может быть пустым");
        else if(!supplementService.loadMotorTypes().contains(lead.getMotorType()))
            throw new IllegalArgumentException("Неверный тип техники");
        newLead.setMotorType(lead.getMotorType());

        // Марка техники.
        if(isNullOrEmpty(lead.getMotorBrand()))
            throw new IllegalArgumentException("Марка техники не может быть пустой");
        else if(!supplementService.loadMotorBrands().contains(lead.getMotorBrand()))
            throw new IllegalArgumentException("Неверная марка техники");
        newLead.setMotorBrand(lead.getMotorBrand());

        // Модель техники.
        if(isNullOrEmpty(lead.getMotorModel()))
            throw new IllegalArgumentException("Модель техники не может быть пустой");
        newLead.setMotorModel(lead.getMotorModel());

        // Цена техники.
        if(lead.getPrice() == null)
            throw new IllegalArgumentException("Цена техники не может быть пустой");
        newLead.setMotorPrice(lead.getPrice());

        // Регион покупки.
        if(isNullOrEmpty(lead.getDelerRegion()))
            throw new IllegalArgumentException("Регион покупки не может быть пустым");
        else if(!supplementService.loadRegions().contains(lead.getDelerRegion()))
            throw new IllegalArgumentException("Неверный регион покупки");
        newLead.setRegion(lead.getDelerRegion());

        // Мотосалон (название или id).
        if(!isNullOrEmpty(lead.getDealerId())) {
            // Найти точку продаж по Id
            SalePoint salePoint = salePointRepository.findOne(lead.getDealerId());
            if(salePoint == null)
                throw new IllegalArgumentException("Id торговой точки не действителен (не найден)");
            else
                newLead.setVendor(salePoint);
        }
        newLead.setPointOfSale(lead.getDealer());
        if(newLead.getPointOfSale() == null && newLead.getVendor() == null)
            throw new IllegalArgumentException("Id торговой точки или ее название должно быть задано");

        // Комментарий.
        newLead.setComment(lead.getComment());

        // Определить потенциального пользователя
        Person user = null;
        if(newLead.getVendor() != null) {
            Set<Person> employees = newLead.getVendor().getEmployees();
            if(!isEmpty(employees))
                user = employees.iterator().next();
        }
        if(user == null)
            user = userService.findUserContactByLogin("admin");

        leadRepository.permitAndSave(newLead, user);
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
