package ru.extas.web.contacts.legalentity;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.product.Product;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.container.ExtaBeanContainer;
import ru.extas.web.product.ProductDataDecl;
import ru.extas.web.product.ProductSelectWindow;

import java.util.ArrayList;
import java.util.List;

import static ru.extas.web.commons.TableUtils.fullInitTable;

/**
 * Обеспечивает редактирование списка продуктов внутри юридического лица
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 16:40
 * @version $Id: $Id
 * @since 0.3
 */
public class LegalProductsField extends CustomField<List> {

    private Table table;
    private ExtaBeanContainer<Product> container;

    /**
     * <p>Constructor for LegalProductsField.</p>
     */
    public LegalProductsField() {
        setBuffered(true);
        setRequiredError("Необходимо выбрать хотябы один продукт!");
        setWidth(600, Unit.PIXELS);
        setHeight(300, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSizeFull();
        fieldLayout.setSpacing(true);
        fieldLayout.setMargin(new MarginInfo(true, false, true, false));

        if (!isReadOnly()) {
            final MenuBar commandBar = new MenuBar();
            commandBar.setAutoOpen(true);
            commandBar.addStyleName(ExtaTheme.GRID_TOOLBAR);
            commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
//            commandBar.focus();

            final MenuBar.MenuItem addProdBtn = commandBar.addItem("Добавить", event -> {

                final ProductSelectWindow selectWindow = new ProductSelectWindow("Выберите продукт");
                selectWindow.addCloseListener(e -> {
                    if (selectWindow.isSelectPressed()) {
                        container.addAll(selectWindow.getSelected());
                        setValue(container.getItemIds());
                        NotificationUtil.showSuccess("Продукт добавлен");
                    }
                });
                selectWindow.showModal();
            });
            addProdBtn.setDescription("Добавить продукт в список доступных данному юридическому лицу");
            addProdBtn.setIcon(Fontello.DOC_NEW);

            final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
                if (table.getValue() != null) {
                    container.removeItem(table.getValue());
                    setValue(container.getItemIds());
                }
            });
            delProdBtn.setDescription("Удалить продукт из списка доступных данному юридическому лицу");
            delProdBtn.setIcon(Fontello.TRASH);

            fieldLayout.addComponent(commandBar);
        }

        table = new Table();
        table.setSizeFull();
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        final Property dataSource = getPropertyDataSource();
        final List<Product> list = dataSource != null ? (List<Product>) dataSource.getValue() : new ArrayList<>();
        container = new ExtaBeanContainer<>(Product.class);
        if (list != null) {
            for (final Product item : list) {
                container.addBean(item);
            }
        }
        container.addNestedContainerProperty("vendor.name");
        table.setContainerDataSource(container);

        final GridDataDecl dataDecl = new ProductDataDecl();
        fullInitTable(table, dataDecl);

        fieldLayout.addComponent(table);
        fieldLayout.setExpandRatio(table, 1);

        return fieldLayout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends List> getType() {
        return List.class;
    }
}
