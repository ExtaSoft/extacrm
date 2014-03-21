package ru.extas.web.contacts;

import ru.extas.model.Contact;

/**
 * Выбор из общего списка контактов (юрики и физики)
 * <p/>
 * Date: 12.09.13
 * Time: 12:14
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class ContactSelect extends AbstractContactSelect<Contact> {
    /**
     * <p>Constructor for ContactSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     */
    public ContactSelect(final String caption) {
        super(caption, Contact.class);
    }

    /**
     * <p>Constructor for ContactSelect.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     */
    public ContactSelect(final String caption, final String description) {
        super(caption, description, Contact.class);
    }
}
