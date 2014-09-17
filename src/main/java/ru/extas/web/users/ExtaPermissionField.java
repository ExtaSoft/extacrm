package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.security.*;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.util.ComponentUtil;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Ввод/редактирование списка прав доступа. Основной компонент - грид.
 *
 * @author Valery Orlov
 *         Date: 22.06.2014
 *         Time: 13:19
 * @version $Id: $Id
 * @since 0.5.0
 */
public class ExtaPermissionField extends CustomField<Set> {

    private UserGroup group;
    private UserProfile profile;

    private ExtaPermissionField() {
        setBuffered(true);
        setWidth(100, Unit.PERCENTAGE);
        setHeight(300, Unit.PIXELS);
    }

    /**
     * <p>Constructor for ExtaPermissionField.</p>
     *
     * @param group a {@link ru.extas.model.security.UserGroup} object.
     */
    public ExtaPermissionField(UserGroup group) {
        this();
        this.group = group;
    }

    /**
     * <p>Constructor for ExtaPermissionField.</p>
     *
     * @param profile a {@link ru.extas.model.security.UserProfile} object.
     */
    public ExtaPermissionField(UserProfile profile) {
        this();
        this.profile = profile;
    }

    /** {@inheritDoc} */
    @Override
    protected Component initContent() {

        ExtaGrid<ExtaPermission> grid = new ExtaGrid<ExtaPermission>(ExtaPermission.class) {

            @Override
            public ExtaEditForm<ExtaPermission> createEditForm(ExtaPermission extaPermission) {
                return new ExtaPermissionEditForm(extaPermission) {
                    @Override
                    protected ExtaPermission saveObject(final ExtaPermission obj) {
                        ((RefreshBeanContainer<ExtaPermission>) container).addBean(obj);
                        ExtaPermissionField.this.setValue(newHashSet(((RefreshBeanContainer<ExtaPermission>) container).getItemIds()));
                        return obj;
                    }
                };
            }

            @Override
            public ExtaPermission createEntity() {
                final ExtaPermission permission = super.createEntity();
                if (group != null)
                    permission.setGroup(group);
                else
                    permission.setUser(profile);
                return permission;
            }

            @Override
            protected GridDataDecl createDataDecl() {
                return new GridDataDecl() {
                    {
                        addMapping("domain", "Раздел");
                        addMapping("target", "Целевые объекты");
                        addMapping("actions", "Разрешенные действия");
                        super.addDefaultMappings();
                    }

                };
            }

            @Override
            protected Container createContainer() {
                final Property dataSource = getPropertyDataSource();
                final Set<ExtaPermission> set = dataSource != null ? (Set<ExtaPermission>) dataSource.getValue() : new HashSet<ExtaPermission>();
                RefreshBeanContainer<ExtaPermission> itemContainer = new RefreshBeanContainer<>(ExtaPermission.class);
                if (set != null) {
                    itemContainer.addAll(set);
                }
                itemContainer.sort(new Object[]{"domain"}, new boolean[]{true});
                return itemContainer;
            }

            @Override
            protected List<UIAction> createActions() {
                List<UIAction> actions = newArrayList();

                actions.add(new NewObjectAction("Новое", "Ввод нового правила доступа в систему"));
                actions.add(new EditObjectAction("Изменить", "Редактирование правила доступа"));
                actions.add(new ItemAction("Удалить", "Удалить правило доступа", Fontello.TRASH) {
                    @Override
                    public void fire(final Object itemId) {
                        container.removeItem(itemId);
                        ExtaPermissionField.this.setValue(newHashSet(((RefreshBeanContainer<ExtaPermission>) container).getItemIds()));
                    }
                });
                return actions;
            }
        };

        return grid;
    }

    /** {@inheritDoc} */
    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

    private abstract class ExtaPermissionEditForm extends ExtaEditForm<ExtaPermission> {

        @PropertyId("domain")
        private ComboBox domainField;

        @PropertyId("actions")
        private OptionGroup actionsField;

        @PropertyId("target")
        private OptionGroup targetField;

        public ExtaPermissionEditForm(ExtaPermission extaPermission) {
            super(extaPermission.isNew() ?
                            "Ввод нового правила доступа в систему" :
                            "Редактирование правила доступа",
                    new BeanItem<>(extaPermission));
        }

        @Override
        protected void initObject(ExtaPermission obj) {

        }

        @Override
        protected ComponentContainer createEditFields(ExtaPermission obj) {
            final FormLayout form = new ExtaFormLayout();

            domainField = new ComboBox("Раздел системы");
            domainField.setDescription("Выберете раздел системы к которой предоставляется доступ");
            domainField.setInputPrompt("Раздел...");
            domainField.setRequired(true);
            domainField.setWidth(25, Unit.EM);
            ComponentUtil.fillSelectByEnum(domainField, ExtaDomain.class);
            form.addComponent(domainField);

            targetField = new OptionGroup("Целевые объекты");
            targetField.setDescription("Укажите к каким объектам в рамках раздела предоставляется доступ");
            targetField.setNullSelectionAllowed(false);
            targetField.setRequired(true);
            targetField.setWidth(25, Unit.EM);
            ComponentUtil.fillSelectByEnum(targetField, SecureTarget.class);
            form.addComponent(targetField);

            actionsField = new OptionGroup("Разрешенные действия");
            actionsField.setDescription("Укажите набор разрешенных действий над целевыми объектами заданного раздела системы");
            actionsField.setMultiSelect(true);
            actionsField.setNullSelectionAllowed(true);
            actionsField.setRequired(true);
            actionsField.addValueChangeListener(event -> {
                Collection<SecureAction> selected = (Collection<SecureAction>) event.getProperty().getValue();
                if(selected.contains(SecureAction.ALL)) {
                    actionsField.setValue(EnumSet.of(SecureAction.ALL));
                    for(SecureAction action : EnumSet.complementOf(EnumSet.of(SecureAction.ALL)))
                        actionsField.setItemEnabled(action, false);
                } else {
                    for(SecureAction action : EnumSet.allOf(SecureAction.class))
                        actionsField.setItemEnabled(action, true);
                }
            });
            ComponentUtil.fillSelectByEnum(actionsField, SecureAction.class);
            form.addComponent(actionsField);

            return form;
        }
    }
}
