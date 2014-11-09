package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

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
                final BeanItemContainer<LegalEntity> itemContainer = new BeanItemContainer<>(LegalEntity.class);
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
                    public void fire(final Object itemId) {
                        final LegalEntitySelectWindow selectWindow = new LegalEntitySelectWindow("Выберите юридическое лицо");
                        selectWindow.addCloseListener(e -> {
                            if (selectWindow.isSelectPressed()) {
                                ((BeanItemContainer<LegalEntity>) container).addBean(selectWindow.getSelected());
                                setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
                                NotificationUtil.showSuccess("Юридическое лицо добавлено");
                            }
                        });
                        selectWindow.setCompanySupplier(companySupplier);
                        selectWindow.showModal();
                    }
                });

                actions.add(new DefaultAction("Изменить", "Редактирование контактных данных", Fontello.EDIT_3) {
                    @Override
                    public void fire(final Object itemId) {
                        final LegalEntity bean = GridItem.extractBean(table.getItem(itemId));
                        final LegalEntityEditForm editWin = new LegalEntityEditForm(bean) {
                            @Override
                            protected LegalEntity saveObject(final LegalEntity obj) {
                                setValue(null, true); // Форсируем изменения
                                setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
                                return obj;
                            }
                        };
                        FormUtils.showModalWin(editWin);
                    }
                });
                actions.add(new ItemAction("Удалить", "Удалить юр.лицо из компании", Fontello.TRASH) {
                    @Override
                    public void fire(final Object itemId) {
                        container.removeItem(itemId);
                        setValue(newHashSet(((BeanItemContainer<LegalEntity>) container).getItemIds()));
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

    public void setCompanySupplier(SupplierSer<Company> companySupplier) {
        this.companySupplier = companySupplier;
    }
}
