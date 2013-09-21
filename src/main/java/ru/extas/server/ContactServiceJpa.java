/**
 *
 */
package ru.extas.server;

import com.google.appengine.api.datastore.*;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.*;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * JPA имплементация службы управления контактами
 *
 * @author Valery Orlov
 */
public class ContactServiceJpa implements ContactService {

    private final Logger logger = LoggerFactory.getLogger(ContactServiceJpa.class);
    @Inject
    private Provider<EntityManager> em;

    /*
     * (non-Javadoc)
     *
     * @see ru.extas.server.ContactService#loadAll()
     */
    @Transactional
    @Override
    public Collection<Contact> loadContacts() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ru.extas.server.ContactService#persistContact(ru.extas.model.Contact)
     */
    @Transactional
    @Override
    public void persistContact(Contact contact) {
        checkNotNull(contact, "Can't persist NULL-value contact!!!");
        checkNotNull(contact.getName(), "Can't persist contact with null name!!!");
        logger.debug("Persisting contact with name {}...", contact.getName());
        if (contact.getId() == null)
            em.get().persist(contact);
        else
            em.get().merge(contact);
    }

    @Transactional
    @Override
    public void updateMissingType() {
        // 1. Контакты
        exportContacts();
        // 2. Пользователи
        exportUsers();
        // 3. Бланки
        exportBSO();
        // 4. Страховки
        exportInsurances();

    }

    private void fillCommons(AbstractExtaObject obj, Entity entity) {
        obj.setId(KeyFactory.keyToString(entity.getKey()));
        final String createdAt = (String) entity.getProperty("createdAt");
        if (createdAt != null)
            obj.setCreatedAt(DateTime.parse(createdAt));
        obj.setCreatedBy((String) entity.getProperty("createdBy"));
        final String modifiedAt = (String) entity.getProperty("modifiedAt");
        if (modifiedAt != null)
            obj.setModifiedAt(DateTime.parse(modifiedAt));
        obj.setModifiedBy((String) entity.getProperty("modifiedBy"));
    }

    private void exportInsurances() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        final EntityManager entityManager = em.get();

        Query q = new Query("Insurance");
        PreparedQuery pq = datastore.prepare(q);

        for (Entity result : pq.asIterable()) {
            Insurance insurance = new Insurance();
            fillCommons(insurance, result);
            insurance.setDate(LocalDate.parse((String) result.getProperty("date")));
            insurance.setEndDate(LocalDate.parse((String) result.getProperty("endDate")));
            insurance.setA7Num((String) result.getProperty("a7Num"));
            insurance.setMotorBrand((String) result.getProperty("motorBrand"));
            insurance.setMotorModel((String) result.getProperty("motorModel"));
            insurance.setMotorType((String) result.getProperty("motorType"));
            insurance.setPaymentDate(LocalDate.parse((String) result.getProperty("paymentDate")));
            insurance.setPremium(BigDecimal.valueOf((Double) result.getProperty("premium")));
            insurance.setRegNum((String) result.getProperty("regNum"));
            insurance.setRiskSum(BigDecimal.valueOf((Double) result.getProperty("riskSum")));
            insurance.setStartDate(LocalDate.parse((String) result.getProperty("startDate")));
            insurance.setPointOfSale((String) result.getProperty("pointOfSale"));

            insurance.setClient(entityManager.find(Person.class, KeyFactory.keyToString((Key) result.getProperty("client_key_OID"))));

            entityManager.persist(insurance);
        }
    }

    private void exportBSO() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        final EntityManager entityManager = em.get();

        Query q = new Query("Policy");
        PreparedQuery pq = datastore.prepare(q);

        for (Entity result : pq.asIterable()) {
            Policy policy = new Policy();
            fillCommons(policy, result);
            final String bookTime = (String) result.getProperty("bookTime");
            if (bookTime != null)
                policy.setBookTime(DateTime.parse(bookTime));
            final String issueDate = (String) result.getProperty("issueDate");
            if (issueDate != null)
                policy.setIssueDate(DateTime.parse(issueDate));
            policy.setRegNum((String) result.getProperty("regNum"));

            entityManager.persist(policy);
        }
    }

    private void exportUsers() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        final EntityManager entityManager = em.get();

        Query q = new Query("UserProfile");
        PreparedQuery pq = datastore.prepare(q);

        for (Entity result : pq.asIterable()) {
            UserProfile user = new UserProfile();
            fillCommons(user, result);
            user.setBlocked((Boolean) result.getProperty("blocked"));
            user.setChangePassword((Boolean) result.getProperty("changePassword"));
            user.setLogin((String) result.getProperty("login"));
            user.setPassword((String) result.getProperty("password"));
            user.setPasswordSalt((String) result.getProperty("passwordSalt"));
            user.setRole(UserRole.valueOf((String) result.getProperty("role")));
            user.setContact(entityManager.find(Person.class, KeyFactory.keyToString((Key) result.getProperty("contact_key_OID"))));

            entityManager.persist(user);
        }
    }

    private void exportContacts() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query("Contact");
        PreparedQuery pq = datastore.prepare(q);

        for (Entity result : pq.asIterable()) {
            Person person = new Person();
            fillCommons(person, result);
            person.setEmail((String) result.getProperty("email"));
            person.setName((String) result.getProperty("name"));
            final String strBirthday = (String) result.getProperty("birthday");
            final LocalDate birthday = strBirthday == null ? null : LocalDate.parse(strBirthday);
            person.setBirthday(birthday);
            person.setCellPhone((String) result.getProperty("cellPhone"));
            final String strSex = (String) result.getProperty("sex");
            final Person.Sex sex = strSex == null ? null : Person.Sex.valueOf(strSex);
            person.setSex(sex);

            person.setActualAddress(new AddressInfo());
            person.getActualAddress().setCity((String) result.getProperty("city"));
            person.getActualAddress().setPostIndex((String) result.getProperty("postIndex"));
            person.getActualAddress().setRegion((String) result.getProperty("region"));
            person.getActualAddress().setStreetBld((String) result.getProperty("streetBld"));

            em.get().persist(person);
        }
    }

}
