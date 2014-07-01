package ru.extas.web.users;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import ru.extas.model.security.*;
import ru.extas.web.commons.*;
import ru.extas.web.commons.window.AbstractEditForm;
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
 */
public class ExtaPermissionField extends CustomField<Set> {

    private UserGroup group;
    private UserProfile profile;

    private ExtaPermissionField() {
        setBuffered(true);
        addStyleName("inline-base-view");
        setSizeFull();
        setWidth(600, Unit.PIXELS);
    }

    public ExtaPermissionField(UserGroup group) {
        this();
        this.group = group;
    }

    public ExtaPermissionField(UserProfile profile) {
        this();
        this.profile = profile;
    }

    @Override
    protected Component initContent() {

        ExtaGrid grid = new ExtaGrid() {
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

                actions.add(new UIAction("Новое", "Ввод нового правила доступа в систему", "icon-doc-new") {
                    @Override
                    public void fire(Object itemId) {
                        final ExtaPermission entity = new ExtaPermission();
                        if (group != null)
                            entity.setGroup(group);
                        else
                            entity.setUser(profile);
                        final BeanItem<ExtaPermission> newObj = new BeanItem<>(entity);

                        final ExtaPermissionEditForm editWin = new ExtaPermissionEditForm("Ввод нового правила доступа в систему", newObj) {
                            @Override
                            protected void saveObject(final ExtaPermission obj) {
                                ((RefreshBeanContainer<ExtaPermission>) container).addBean(obj);
                                ExtaPermissionField.this.setValue(newHashSet(((RefreshBeanContainer<ExtaPermission>) container).getItemIds()));
                            }
                        };
                        editWin.showModal();
                    }
                });

                actions.add(new DefaultAction("Изменить", "Редактирование правила доступа", "icon-edit-3") {
                    @Override
                    public void fire(final Object itemId) {
                        final BeanItem<ExtaPermission> beanItem = new GridItem<>(table.getItem(itemId));
                        final ExtaPermissionEditForm editWin = new ExtaPermissionEditForm("Редактирование правила доступа", beanItem) {
                            @Override
                            protected void saveObject(final ExtaPermission obj) {
                                HashSet<ExtaPermission> fieldValue = newHashSet(((RefreshBeanContainer<ExtaPermission>) container).getItemIds());
                                ExtaPermissionField.this.setValue(null);
                                ExtaPermissionField.this.setValue(fieldValue);
                                refreshContainerItem(itemId);
                            }
                        };
                        editWin.showModal();
                    }
                });
                actions.add(new ItemAction("Удалить", "Удалить правило доступа", "icon-trash") {
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

    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

    private abstract class ExtaPermissionEditForm extends AbstractEditForm<ExtaPermission> {

        @PropertyId("domain")
        private ComboBox domainField;

        @PropertyId("actions")
        private OptionGroup actionsField;

        @PropertyId("target")
        private OptionGroup targetField;

        protected ExtaPermissionEditForm(String caption, BeanItem<ExtaPermission> beanItem) {
            super(caption, beanItem);
        }

        @Override
        protected void initObject(ExtaPermission obj) {

        }

        @Override
        protected void checkBeforeSave(ExtaPermission obj) {

        }

        @Override
        protected ComponentContainer createEditFields(ExtaPermission obj) {
            final FormLayout form = new FormLayout();

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
            actionsField.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    Collection<SecureAction> selected = (Collection<SecureAction>) event.getProperty().getValue();
                    if(selected.contains(SecureAction.ALL)) {
                        actionsField.setValue(EnumSet.of(SecureAction.ALL));
                        for(SecureAction action : EnumSet.complementOf(EnumSet.of(SecureAction.ALL)))
                            actionsField.setItemEnabled(action, false);
                    } else {
                        for(SecureAction action : EnumSet.allOf(SecureAction.class))
                            actionsField.setItemEnabled(action, true);
                    }
                }
            });
            ComponentUtil.fillSelectByEnum(actionsField, SecureAction.class);
            form.addComponent(actionsField);

            return form;
        }
    }
}
