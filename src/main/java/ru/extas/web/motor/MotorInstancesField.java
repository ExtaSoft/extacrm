package ru.extas.web.motor;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang3.ArrayUtils;
import ru.extas.model.motor.MotorInstance;
import ru.extas.model.motor.MotorInstance_;
import ru.extas.utils.SupplierSer;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaCustomField;
import ru.extas.web.commons.container.ExtaBeanContainer;
import ru.extas.web.commons.converters.StringToMoneyConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * Поле редактирования списка техники в продаже и лиде
 * <p>
 * Created by valery on 19.09.16.
 */
public class MotorInstancesField extends ExtaCustomField<List> {

    private final SupplierSer<MotorInstance> newMotorSupplier;
    private ExtaBeanContainer<MotorInstance> container;
    private Grid grid;

    public MotorInstancesField(String caption, SupplierSer<MotorInstance> newMotorSupplier) {
        super(caption, "");

        this.newMotorSupplier = newMotorSupplier;
        setWidth(100, Unit.PERCENTAGE);
    }

    @Override
    protected Component initContent() {

        final List<MotorInstance> list = getValueList();
        container = new ExtaBeanContainer<>(MotorInstance.class, list);

        final VerticalLayout root = new VerticalLayout();
        root.setMargin(new MarginInfo(false, false, true, false));

        final MenuBar motorMenu = new MenuBar();
        motorMenu.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
        motorMenu.addStyleName(ExtaTheme.MENUBAR_SMALL);

        // Продукты доступные для добавления в данную продажу
        final MenuBar.MenuItem addMotorBtn = motorMenu.addItem("Добавить технику", FontAwesome.PLUS, item -> {
            final MotorInstance bean = newMotorSupplier.get();
            container.addBean(bean);
            grid.editItem(bean);
        });

        final MenuBar.MenuItem edMenuItem = motorMenu.addItem("Изменить", FontAwesome.EDIT, item -> {
            grid.editItem(grid.getSelectedRow());
        });
        edMenuItem.setEnabled(false);
        final MenuBar.MenuItem delMenuItem = motorMenu.addItem("Удалить", FontAwesome.TRASH_O, item -> {
            container.removeItem(grid.getSelectedRow());
            item.setEnabled(false);
            setValue(newArrayList(container.getItemIds()));
        });
        delMenuItem.setEnabled(false);
        root.addComponent(motorMenu);

        final String[] visibleProps = new String[]{
                MotorInstance_.type.getName(),
                MotorInstance_.brand.getName(),
                MotorInstance_.model.getName(),
                MotorInstance_.price.getName()
        };
        final GeneratedPropertyContainer gridContainer = new GeneratedPropertyContainer(container);
        gridContainer.getContainerPropertyIds().stream()
                .filter(p -> !ArrayUtils.contains(visibleProps, p))
                .forEach(p -> gridContainer.removeContainerProperty(p));

        grid = new Grid(gridContainer);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(container.size() == 0 ? 1 : container.size());
        grid.setEditorEnabled(true);
        grid.setEditorSaveCaption("Сохранить");
        grid.setEditorCancelCaption("Отмена");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setColumnOrder(visibleProps);

        final Grid.Column typeColumn = grid.getColumn(MotorInstance_.type.getName());
        typeColumn.setHeaderCaption("Тип");
        MotorTypeSelect motorTypeField = new MotorTypeSelect();
        motorTypeField.setRequired(true);
        typeColumn.setEditorField(motorTypeField);

        final Grid.Column brandColumn = grid.getColumn(MotorInstance_.brand.getName());
        brandColumn.setHeaderCaption("Бренд");
        MotorBrandSelect motorBrandField = new MotorBrandSelect();
        motorBrandField.setRequired(true);
        motorBrandField.linkToType(motorTypeField);
        brandColumn.setEditorField(motorBrandField);

        final Grid.Column modelColumn = grid.getColumn(MotorInstance_.model.getName());
        modelColumn.setHeaderCaption("Модель");
        MotorModelSelect motorModelField = new MotorModelSelect("Модель техники");
        motorModelField.setRequired(true);
        motorModelField.linkToTypeAndBrand(motorTypeField, motorBrandField);
        modelColumn.setEditorField(motorModelField);

        final Grid.Column priceColumn = grid.getColumn(MotorInstance_.price.getName());
        priceColumn.setHeaderCaption("Стоимость");
        EditField motorPriceField = new EditField("Цена техники");
        motorPriceField.setRequired(true);
        priceColumn.setEditorField(motorPriceField);

        final Grid.FooterRow footerRow = grid.prependFooterRow();
        final Grid.FooterCell footerCaption = footerRow.join(MotorInstance_.type.getName(), MotorInstance_.brand.getName(), MotorInstance_.model.getName());
//        footerCaption.setText("Итоговая стоимость");
        final Grid.FooterCell footerPrice = footerRow.getCell(MotorInstance_.price.getName());
        updatePriceFooter(footerPrice);

        grid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {

            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                setValue(newArrayList(container.getItemIds()));
            }
        });

        grid.addSelectionListener(e -> {
            edMenuItem.setEnabled(true);
            delMenuItem.setEnabled(true);
        });

        gridContainer.addItemSetChangeListener(e -> {
            grid.setHeightByRows(container.size() == 0 ? 1 : container.size());
            updatePriceFooter(footerPrice);
        });
        root.addComponent(grid);

        addReadOnlyStatusChangeListener(e -> {
            final boolean isRedOnly = isReadOnly();
//            addProductBtn.setVisible(!isRedOnly);
            grid.setReadOnly(isRedOnly);
            delMenuItem.setVisible(!isRedOnly);
        });

        addValueChangeListener(e -> {
            container.removeAllItems();
            final Collection<MotorInstance> value = (Collection<MotorInstance>) e.getProperty().getValue();
            if (value != null)
                container.addAll(value);
        });

        return root;
    }

    private void updatePriceFooter(Grid.FooterCell footerPrice) {
        footerPrice.setText(lookup(StringToMoneyConverter.class).convertToPresentation(getTotalPrice(), null));
    }

    private List<MotorInstance> getValueList() {
        return getValue() != null ? (List<MotorInstance>) getValue() : new ArrayList<>();
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    public List<String> getBrands() {
        return newArrayList(getValueList().stream().map(i -> i.getBrand()).iterator());
    }

    public BigDecimal getTotalPrice() {
        return getValueList().stream().map(e -> e.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
