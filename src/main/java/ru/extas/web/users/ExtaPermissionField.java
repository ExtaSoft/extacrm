package ru.extas.web.users;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.*;
import ru.extas.model.security.*;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.container.RefreshBeanContainer;
import ru.extas.web.util.ComponentUtil;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.server.ServiceLocator.lookup;

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
    public ExtaPermissionField(final UserGroup group) {
        this();
        this.group = group;
    }

    /**
     * <p>Constructor for ExtaPermissionField.</p>
     *
     * @param profile a {@link ru.extas.model.security.UserProfile} object.
     */
    public ExtaPermissionField(final UserProfile profile) {
        this();
        this.profile = profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {

        final ExtaGrid<ExtaPermission> grid = new ExtaGrid<ExtaPermission>(ExtaPermission.class) {

            @Override
            public ExtaEditForm<ExtaPermission> createEditForm(final ExtaPermission extaPermission, final boolean isInsert) {
                return new ExtaPermissionEditForm(extaPermission) {
                    @Override
                    protected ExtaPermission saveEntity(final ExtaPermission permission) {
                        if (isInsert)
                            ((RefreshBeanContainer<ExtaPermission>) container).addBean(permission);
                        else
                            setValue(null, true); // Форсируем изменения
                        setValue(newHashSet(((RefreshBeanContainer<ExtaPermission>) container).getItemIds()));
                        return permission;
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
                        addMapping("actions", "Разрешенные действия", new ComponentColumnGenerator() {
                            @Override
                            public Object generateCell(Object columnId, Item item, Object itemId) {
                                ExtaPermission permission = GridItem.extractBean(item);
                                final StringToSecureActionConverter cnv = lookup(StringToSecureActionConverter.class);
                                return new Label(
                                        Joiner.on(", ")
                                                .join(permission.getActions().stream()
                                                        .map(a -> cnv.convertToPresentation(a, String.class, null))
                                                        .iterator()));
                            }

                            @Override
                            public Class<?> getType() {
                                return String.class;
                            }
                        });
                        super.addDefaultMappings();
                    }

                };
            }

            @Override
            protected Container createContainer() {
                final Set<ExtaPermission> set = getValue() != null ? getValue() : newHashSet();
                final RefreshBeanContainer<ExtaPermission> itemContainer = new RefreshBeanContainer<>(ExtaPermission.class);
                if (set != null) {
                    itemContainer.addAll(set);
                }
                itemContainer.sort(new Object[]{"domain"}, new boolean[]{true});
                return itemContainer;
            }

            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new NewObjectAction("Новое", "Ввод нового правила доступа в систему"));
                actions.add(new EditObjectAction("Изменить", "Редактирование правила доступа"));
                actions.add(new ItemAction("Удалить", "Удалить правило доступа", Fontello.TRASH) {
                    @Override
                    public void fire(final Set itemIds) {
                        itemIds.forEach(id -> container.removeItem(id));
                        ExtaPermissionField.this.setValue(newHashSet(((RefreshBeanContainer<ExtaPermission>) container).getItemIds()));
                    }
                });
                return actions;
            }
        };

        return grid;
    }

    /**
     * {@inheritDoc}
     */
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

        public ExtaPermissionEditForm(final ExtaPermission extaPermission) {
            super(extaPermission.isNew() ?
                            "Ввод нового правила доступа в систему" :
                            "Редактирование правила доступа",
                    extaPermission);

            setWinWidth(640, Unit.PIXELS);
            setWinHeight(430, Unit.PIXELS);
        }

        @Override
        protected void initEntity(final ExtaPermission permission) {

        }

        @Override
        protected ExtaPermission saveEntity(final ExtaPermission permission) {
            return permission;
        }

        @Override
        protected ComponentContainer createEditFields() {
            final FormLayout form = new ExtaFormLayout();
            form.setSizeFull();

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
                final Collection<SecureAction> selected = (Collection<SecureAction>) event.getProperty().getValue();
                if (selected.contains(SecureAction.ALL)) {
                    actionsField.setValue(EnumSet.of(SecureAction.ALL));
                    for (final SecureAction action : EnumSet.complementOf(EnumSet.of(SecureAction.ALL)))
                        actionsField.setItemEnabled(action, false);
                } else {
                    for (final SecureAction action : EnumSet.allOf(SecureAction.class))
                        actionsField.setItemEnabled(action, true);
                }
            });
            ComponentUtil.fillSelectByEnum(actionsField, SecureAction.class);
            form.addComponent(actionsField);

            return form;
        }
    }
}
