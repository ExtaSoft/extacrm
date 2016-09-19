package ru.extas.web.contacts;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import ru.extas.model.contacts.Client;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.contacts.legalentity.LegalEntityEditForm;
import ru.extas.web.contacts.person.PersonEditForm;

/**
 * Created by valery on 15.09.16.
 */
public class ClientColumnGenerator extends GridDataDecl.ComponentColumnGenerator {
    private final String clientPropId;

    public ClientColumnGenerator(String clientPropId) {
        this.clientPropId = clientPropId;
    }

    public ClientColumnGenerator() {
        this("client");
    }

    @Override
    public Object generateCell(Object columnId, Item item, Object itemId) {
        final Client client = (Client) item.getItemProperty(clientPropId).getValue();
        final Button link = new Button();
        link.addStyleName(ExtaTheme.BUTTON_LINK);
        if (client != null) {
            link.setCaption(client.getName());
            if (client instanceof LegalEntity) {
                link.addClickListener(event -> {
                    final LegalEntityEditForm editWin = new LegalEntityEditForm((LegalEntity) client);
//                    editWin.setReadOnly(true);
                    FormUtils.showModalWin(editWin);
                });
            } else {
                link.addClickListener(event -> {
                    final PersonEditForm editWin = new PersonEditForm((Person) client);
//                    editWin.setReadOnly(true);
                    FormUtils.showModalWin(editWin);
                });
            }
            return link;
        } else {
            return null;
        }
    }
}
