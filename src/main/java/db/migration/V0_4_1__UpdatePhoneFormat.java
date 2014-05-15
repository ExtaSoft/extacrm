package db.migration;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * <p>V0_4_1__UpdatePhoneFormat class.</p>
 *
 * @author Valery Orlov
 *         Date: 17.04.2014
 *         Time: 21:32
 * @version $Id: $Id
 * @since 0.4.2
 */
public class V0_4_1__UpdatePhoneFormat implements SpringJdbcMigration {

    /** {@inheritDoc} */
    @Override
    public void migrate(final JdbcTemplate jdbcTemplate) throws Exception {

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String country = "RU";

        // Contacts
        List<Contact> contacts = jdbcTemplate.query("SELECT ID, CELL_PHONE AS PHONE FROM CONTACT", new ContactRowMapper());
        convertPhones(phoneUtil, country, contacts);
        jdbcTemplate.batchUpdate("UPDATE CONTACT set CELL_PHONE = ? where ID = ?",
                Lists.transform(contacts, new Function<Contact, Object[]>() {
                    @Override
                    public Object[] apply(Contact input) {
                        return new Object[]{input.getPhone(), input.getId()};
                    }
                })
        );
    }

    /**
     * <p>convertPhones.</p>
     *
     * @param phoneUtil a {@link com.google.i18n.phonenumbers.PhoneNumberUtil} object.
     * @param country a {@link java.lang.String} object.
     * @param contacts a {@link java.util.List} object.
     */
    protected void convertPhones(PhoneNumberUtil phoneUtil, String country, List<Contact> contacts) {
        for (Contact contact : contacts) {
            if (!isNullOrEmpty(contact.getPhone())) {
                try {
                    Phonenumber.PhoneNumber phone = phoneUtil.parse(contact.getPhone(), country);
                    contact.setPhone(phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                } catch (NumberParseException e) {
                }
            }
        }
    }

    private class Contact {
        private String id;
        private String phone;

        private Contact(String id, String phone) {
            this.id = id;
            this.phone = phone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    private class ContactRowMapper implements RowMapper<Contact> {

        public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contact cnt = new Contact(rs.getString("ID"), rs.getString("PHONE"));
            return cnt;
        }
    }
}
