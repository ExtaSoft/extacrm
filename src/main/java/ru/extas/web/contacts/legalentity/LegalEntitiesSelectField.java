package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Реализует ввод/редактирование списка юридических лиц
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 20:03
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalEntitiesSelectField extends CustomField<Set> {

    private SupplierSer<Company> companySupplier;

    /**
     * <p>Constructor for LegalEntitiesSelectField.</p>
     *
     * @param company a {@link ru.extas.model.contacts.Company} object.
     */
    public LegalEntitiesSelectField() {
        setBuffered(true);
        setRequiredError("Юридическое лицо обязательно для заполнения!");
        setWidth(600, Unit.PIXELS);
        setHeight(300, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final LegalEntitiesGrid grid = new LegalEntitiesGrid() {
            @Override
            protected Container createContainer() {
                final Set<LegalEntity> list = getValue() != null ? getValue() : newHashSet();
                final ExtaBeanContainer<LegalEntity> itemContainer = new ExtaBeanContainer<>(LegalEntity.class);
                if (list != null) {
                    for (final LegalEntity item : list) {
                        itemContainer.addBean(item);
                    }
                }
                itemContainer.addNestedContainerProperty("regAddress.region");
                itemContainer.addNestedContainerProperty("company.name");
                return itemContainer;
            }

            @Override
            protected List<UIAction> createActions() {
                final List<UIAction> actions = newArrayList();

                actions.add(new UIAction("Добавить", "Выбрать юридическое лицо осуществляющуе деятельность на торговой точке", Fontello.DOC_NEW) {
                    @Override
                    public void fire(final Set itemIds) {
                        final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберите юридическое лицо");
                        selectWindow.addCloseListener(e -> {
                            if (selectWindow.isSelectPressed()) {
                                ((ExtaBeanContainer<LegalEntity>) container).addAll(selectWindow.getSelected());
                                setValue(newHashSet(((ExtaBeanContainer<LegalEntity>) container).getItemIds()));
                                NotificationUtil.showSuccess("Юридическое лицо добавлено");
                            }
                        });
                        selectWindow.setCompanySupplier(companySupplier);
                        selectWindow.showModal();
                    }
                });

                actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", Fontello.EDIT_3) {
                    @Override
                    public void fire(final Set itemIds) {
                        final LegalEntity bean = getFirstEntity(itemIds);
                        final LegalEntityEditForm editWin = new LegalEntityEditForm(bean);
                        editWin.addCloseFormListener(e -> {
                            if(editWin.isSaved()) {
                                setValue(newHashSet(((ExtaBeanContainer<LegalEntity>) container).getItemIds()));
                                fireValueChange(false);
                            }
                        });
                        FormUtils.showModalWin(editWin);
                    }
                });
                actions.add(new ItemAction("Удалить", "Удалить юр.лицо из компании", Fontello.TRASH) {
                    @Override
                    public void fire(final Set itemIds) {
                        itemIds.forEach(id -> container.removeItem(id));
                        setValue(newHashSet(((ExtaBeanContainer<LegalEntity>) container).getItemIds()));
                    }
                });
                return actions;
            }
        };
        grid.setCompanySupplier(companySupplier);
        grid.setSizeFull();

        return grid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

    public SupplierSer<Company> getCompanySupplier() {
        return companySupplier;
    }

    public void setCompanySupplier(final SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
