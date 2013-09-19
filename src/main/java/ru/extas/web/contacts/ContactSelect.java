package ru.extas.web.contacts;

import ru.extas.model.Contact;

/**
 * Выбор из общего списка контактов (юрики и физики)
 * <p/>
 * Date: 12.09.13
 * Time: 12:14
 *
 * @author Valery Orlov
 */
public class ContactSelect extends AbstractContactSelect<Contact> {
    public ContactSelect(final String caption) {
        super(caption, Contact.class);
    }

    public ContactSelect(final String caption, final String description) {
        super(caption, description, Contact.class);
    }
}
