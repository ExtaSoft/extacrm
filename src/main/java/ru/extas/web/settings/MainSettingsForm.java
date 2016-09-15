package ru.extas.web.settings;

import com.google.common.base.Splitter;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.fields.MCheckBox;
import org.vaadin.viritin.layouts.MMarginInfo;
import ru.extas.model.settings.SettingsInstance;
import ru.extas.server.settings.UserSettingsService;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;

import static com.google.common.collect.Iterables.getLast;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Форма с основными настройками
 * <p>
 * Created by valery on 15.09.16.
 */
public class MainSettingsForm extends ExtaEditForm<SettingsInstance> {

    @PropertyId("appTitle")
    private EditField appTitleField;
    @PropertyId("iconPath")
    private ComboBox iconPathField;
    @PropertyId("showSalePointIds")
    private MCheckBox isShowSalePointIdsField;
    @PropertyId("devServer")
    private MCheckBox isDevServerField;

    public MainSettingsForm() {
        super("Основные настройки", lookup(UserSettingsService.class).loadMainSettings());
        setSizeFull();
        setMargin(new MMarginInfo(false, false, true, false));
    }

    @Override
    protected void initEntity(SettingsInstance settingsInstance) {

    }

    @Override
    protected SettingsInstance saveEntity(SettingsInstance settingsInstance) {
        settingsInstance = lookup(UserSettingsService.class).saveMainSettings(settingsInstance);
        NotificationUtil.showSuccess("Настройки сохранены");
        return settingsInstance;
    }

    @Override
    protected ComponentContainer createEditFields() {
        final ExtaFormLayout form = new ExtaFormLayout();

        appTitleField = new EditField("Заголовок приложения");
        form.addComponent(appTitleField);

        iconPathField = new ComboBox("Иконка приложения");
        for (String icon : lookup(UserSettingsService.class).getFaviconPathList()) {
            iconPathField.addItem(icon);
            iconPathField.setItemIcon(icon, new ThemeResource(getLast(Splitter.on('/').split(icon))));
        }
        iconPathField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ICON_ONLY);
        iconPathField.setWidth(85, Unit.PIXELS);
        iconPathField.setTextInputAllowed(false);
        iconPathField.setNullSelectionAllowed(false);
        form.addComponent(iconPathField);

        isShowSalePointIdsField = new MCheckBox("Показывать раздел \"Идентификация\" в карточке торговой точки");
        form.addComponent(isShowSalePointIdsField);

        isDevServerField = new MCheckBox("Режим отладки");
        form.addComponent(isDevServerField);

        return form;
    }
}
