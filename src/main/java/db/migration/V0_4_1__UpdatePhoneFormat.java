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

        final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        final String country = "RU";

        // Contacts
        final List<Contact> contacts = jdbcTemplate.query("SELECT ID, CELL_PHONE AS PHONE FROM CONTACT", new ContactRowMapper());
        convertPhones(phoneUtil, country, contacts);
        jdbcTemplate.batchUpdate("UPDATE CONTACT set CELL_PHONE = ? where ID = ?",
                Lists.transform(contacts, input -> new Object[]{input.getPhone(), input.getId()})
        );
    }

    /**
     * <p>convertPhones.</p>
     *
     * @param phoneUtil a {@link com.google.i18n.phonenumbers.PhoneNumberUtil} object.
     * @param country a {@link java.lang.String} object.
     * @param contacts a {@link java.util.List} object.
     */
    protected void convertPhones(final PhoneNumberUtil phoneUtil, final String country, final List<Contact> contacts) {
        contacts.stream().filter(contact -> !isNullOrEmpty(contact.getPhone())).forEach((final Contact contact) -> {
            try {
                final Phonenumber.PhoneNumber phone = phoneUtil.parse(contact.getPhone(), country);
                contact.setPhone(phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            } catch (final NumberParseException e) {
            }
        });
    }

    private class Contact {
        private String id;
        private String phone;

        private Contact(final String id, final String phone) {
            this.id = id;
            this.phone = phone;
        }

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(final String phone) {
            this.phone = phone;
        }
    }

    private class ContactRowMapper implements RowMapper<Contact> {

        public Contact mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final Contact cnt = new Contact(rs.getString("ID"), rs.getString("PHONE"));
            return cnt;
        }
    }
}
