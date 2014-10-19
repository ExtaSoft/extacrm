package ru.extas.web.contacts;

import com.google.common.base.Joiner;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import ru.extas.model.contacts.AddressInfo;
import ru.extas.server.references.SupplementService;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.reference.CitySelect;
import ru.extas.web.reference.RegionSelect;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.UiUtils.showValidationError;

/**
 * Поле для компактного отображения и редактирования адреса
 *
 * @author Valery Orlov
 *         Date: 19.10.2014
 *         Time: 18:02
 */
public class AddressInfoField extends CustomField<AddressInfo> {

    private PopupView popupView;

    public AddressInfoField() {
        this("Адрес");
    }

    public AddressInfoField(String caption) {
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        popupView = new PopupView(new PopupAddressInfoContent());
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    @Override
    public Class<? extends AddressInfo> getType() {
        return AddressInfo.class;
    }

    private class PopupAddressInfoContent implements PopupView.Content {

        @PropertyId("region")
        private RegionSelect regRegionField;
        @PropertyId("city")
        private CitySelect regCityField;
        @PropertyId("postIndex")
        private EditField regPostIndexField;
        @PropertyId("streetBld")
        private TextArea regStreetBldField;


        @Override
        public String getMinimizedValueAsHTML() {
            final AddressInfo addressInfo = getValue();
            if (addressInfo != null && !addressInfo.isEmpty()) {
                List<String> fields = newArrayListWithCapacity(4);
                if (!isNullOrEmpty(addressInfo.getPostIndex()))
                    fields.add(addressInfo.getPostIndex());
                if (!isNullOrEmpty(addressInfo.getRegion()))
                    fields.add(addressInfo.getRegion());
                if (!isNullOrEmpty(addressInfo.getCity()))
                    fields.add(addressInfo.getCity());
                if (!isNullOrEmpty(addressInfo.getStreetBld()))
                    fields.add(addressInfo.getStreetBld());
                return Joiner.on(", ").join(fields);
            } else
                return "Нажмите для ввода адреса...";
        }

        @Override
        public Component getPopupComponent() {
            final FormLayout formLayout = new ExtaFormLayout();
            formLayout.setMargin(true);

            final AddressInfo addressInfo = getValue();

            regRegionField = new RegionSelect();
            regRegionField.setDescription("Укажите регион проживания");
            regRegionField.addValueChangeListener(event -> {
                final String newRegion = (String) event.getProperty().getValue();
                final String city = lookup(SupplementService.class).findCityByRegion(newRegion);
                if (city != null)
                    regCityField.setValue(city);
            });
            formLayout.addComponent(regRegionField);

            regCityField = new CitySelect();
            regCityField.setDescription("Введите город проживания контакта");
            if (addressInfo.getCity() != null)
                regCityField.addItem(addressInfo.getCity());
            regCityField.addValueChangeListener(event -> {
                final String newCity = (String) event.getProperty().getValue();
                final String region = lookup(SupplementService.class).findRegionByCity(newCity);
                if (region != null)
                    regRegionField.setValue(region);
            });
            formLayout.addComponent(regCityField);

            regPostIndexField = new EditField("Почтовый индекс");
            regPostIndexField.setColumns(8);
            regPostIndexField.setInputPrompt("Индекс");
            regPostIndexField.setNullRepresentation("");
            formLayout.addComponent(regPostIndexField);

            regStreetBldField = new TextArea("Адрес");
            regStreetBldField.setRows(2);
            regStreetBldField.setDescription("Почтовый адрес (улица, дом, корпус, ...)");
            regStreetBldField.setInputPrompt("Улица, Дом, Корпус и т.д.");
            regStreetBldField.setNullRepresentation("");
            formLayout.addComponent(regStreetBldField);

            final BeanFieldGroup<AddressInfo> fieldGroup = new BeanFieldGroup<>(AddressInfo.class);
            fieldGroup.setItemDataSource(new BeanItem<>(addressInfo));
            fieldGroup.setBuffered(true);
            fieldGroup.bindMemberFields(this);
            fieldGroup.setReadOnly(isReadOnly());

            final Button saveBtn = new Button("Сохранить");
            saveBtn.addStyleName(ExtaTheme.BUTTON_PRIMARY);
            saveBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
            saveBtn.setIcon(Fontello.OK);
            saveBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER, ShortcutAction.ModifierKey.CTRL);
            saveBtn.addClickListener(e -> {
                if (fieldGroup.isModified()) {
                    if (fieldGroup.isValid()) {
                        try {
                            fieldGroup.commit();
                            setValue(addressInfo);
                        } catch (final Throwable ex) {
                            NotificationUtil.showError("Невозможно сохранить изменения!", ex.getLocalizedMessage());
                            return;
                        }
                    } else
                        showValidationError("Невозможно сохранить изменения!", fieldGroup);
                }
                popupView.setPopupVisible(false);
            });
            if (!isReadOnly())
                formLayout.addComponent(saveBtn);

            return formLayout;
        }
    }
}
