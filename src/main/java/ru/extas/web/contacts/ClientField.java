package ru.extas.web.contacts;

import com.vaadin.ui.*;
import ru.extas.model.contacts.Client;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.contacts.legalentity.LegalEntityField;
import ru.extas.web.contacts.person.PersonField;

/**
 * Воле выбора/ввода клиента: Физ. или Юр.лицо
 *
 * @author Valery Orlov
 *         Date: 02.12.2014
 *         Time: 18:26
 */
public class ClientField extends CustomField<Client> {

    private PopupView popupView;
    private PopupClientContent entityContent;

    public ClientField(final String caption) {
        this(caption, "");
    }

    public ClientField(final String caption, final String description) {
        setCaption(caption);
        setDescription(description);
        setBuffered(true);
    }

    @Override
    protected Component initContent() {
        entityContent = new PopupClientContent();
        popupView = new PopupView(entityContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    @Override
    public Class<? extends Client> getType() {
//        if (getValue() != null && getValue() instanceof LegalEntity)
//            return LegalEntity.class;
//        else
//            return Person.class;
        return Client.class;
    }

    private class PopupClientContent implements PopupView.Content {
        @Override
        public String getMinimizedValueAsHTML() {
            final Client client = getValue();
            if (client != null)
                return client.getName();
            else
                return "Нажмите для выбора или ввода...";
        }

        @Override
        public Component getPopupComponent() {
            final VerticalLayout layout = new VerticalLayout();
            layout.setSizeUndefined();
            layout.setMargin(true);

            final boolean isLegalEntity = getValue() != null && getValue() instanceof LegalEntity;
            final CheckBox isLegalEntityField = new CheckBox("Клиент Юр.лицо", isLegalEntity);
            isLegalEntityField.setDescription("Отметте флаг, если клиент является юр.лицом");
            isLegalEntityField.addValueChangeListener(event -> {
                final Boolean isLE = isLegalEntityField.getValue();
                if (getValue() != null)
                    if (!isLE && getValue() instanceof LegalEntity
                            || isLE && getValue() instanceof Person)
                        setValue(null);
                makePopup(layout, isLE);
            });
            layout.addComponent(isLegalEntityField);

            makePopup(layout, isLegalEntity);

            return layout;
        }

        private void makePopup(final VerticalLayout layout, final boolean isLegalEntity) {
            if (layout.getComponentCount() == 2)
                layout.removeComponent(layout.getComponent(1));

            if (isLegalEntity) {
                layout.addComponent(new LegalEntityField.PopupForm(ClientField.this, popupView, null));
            } else {
                layout.addComponent(new PersonField.PopupForm(ClientField.this, popupView));
            }
        }
    }
}
