package ru.extas.web.sale;

import com.google.common.base.Joiner;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.UI;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;
import ru.extas.model.contacts.Employee;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.server.sale.SaleRepository;
import ru.extas.server.security.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.SecuredDataContainer;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * <p>SalesGrid class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 * @version $Id: $Id
 * @since 0.3
 */
public class SalesGrid extends ExtaGrid<Sale> {
    private static final long serialVersionUID = 4876073256421755574L;
    private final static Logger logger = LoggerFactory.getLogger(SalesGrid.class);
    private final ExtaDomain domain;
    private final boolean isMyOnly;

    /**
     * <p>Constructor for SalesGrid.</p>
     *
     * @param domain a {@link ExtaDomain} object.
     * @param isMyOnly
     */
    public SalesGrid(final ExtaDomain domain, final boolean isMyOnly) {
        super(Sale.class);
        this.domain = domain;
        this.isMyOnly = isMyOnly;
        setReadOnly(domain != ExtaDomain.SALES_OPENED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new SaleDataDecl(domain);
    }

    @Override
    public ExtaEditForm<Sale> createEditForm(final Sale sale, final boolean isInsert) {
        final SaleEditForm saleEditForm = new SaleEditForm(sale);
        saleEditForm.setReadOnly(domain != ExtaDomain.SALES_OPENED);
        return saleEditForm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initTable(final Mode mode) {
        super.initTable(mode);
        if (domain == ExtaDomain.SALES_CANCELED)
            table.setColumnCollapsed(Sale_.cancelReason.getName(), false);

        // Раскрашиваем "протухшие" продажи
        if (domain == ExtaDomain.SALES_OPENED) {
            final CustomTable.CellStyleGenerator defGen = table.getCellStyleGenerator();
            table.setCellStyleGenerator((source, itemId, propertyId) -> {
                String style = null;
                if (defGen != null) // Если уже есть генератор
                    style = defGen.getStyle(source, itemId, propertyId);
                if (style == null) {
                    final Sale sale = getEntity(itemId);
                    final DateTime curDate = DateTime.now(DateTimeZone.UTC);
                    final DateTime modifiedDate = sale.getLastModifiedDate();
                    if (modifiedDate.plus(Days.days(10)).isBeforeNow())
                        style = "highlight-red"; // Красненькие
                    else if (modifiedDate.plus(Days.days(5)).isBeforeNow())
                        style = "highlight-yellow"; // Желтенькие
                }
                return style;
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDbContainer<Sale> container = SecuredDataContainer.create(Sale.class, domain);
        container.addNestedContainerProperty("client.name");
        container.addNestedContainerProperty("client.phone");
        container.addNestedContainerProperty("dealer.name");
        container.addNestedContainerProperty("responsible.name");
        container.addNestedContainerProperty("responsibleAssist.name");
        container.addNestedContainerProperty("dealerManager.name");
        container.addContainerFilter(new Compare.Equal("status",
                domain == ExtaDomain.SALES_CANCELED ? Sale.Status.CANCELED :
                        domain == ExtaDomain.SALES_OPENED ? Sale.Status.NEW : Sale.Status.FINISHED));
        if (isMyOnly) {
            final Employee user = lookup(UserManagementService.class).getCurrentUserEmployee();
            container.addContainerFilter(
                    new Or(
                            new Compare.Equal("responsible", user),
                            new Compare.Equal("responsibleAssist", user)));
        }
        container.sort(new Object[]{Sale_.lastModifiedDate.getName()}, new boolean[]{domain == ExtaDomain.SALES_OPENED});
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        if (domain == ExtaDomain.SALES_OPENED)
            actions.add(new NewObjectAction("Новый", "Ввод новой продажи"));

        actions.add(new EditObjectAction(domain == ExtaDomain.SALES_OPENED ? "Изменить" : "Просмотреть", "Редактировать выделенную в списке продажу"));

        if (domain != ExtaDomain.SALES_OPENED)
            actions.add(new ItemAction("Возобновить", "Вернуть продажу в открытые, чтобы продолжить работу по ней", FontAwesome.UNDO) {
                @Override
                public void fire(final Set itemIds) {
                    final Set<Sale> sales = getEntities(itemIds);
                    final String numList = getSalesNumList(sales);
                    ConfirmDialog.show(UI.getCurrent(),
                            "Подтвердите действие...",
                            MessageFormat.format("Вы уверены, что хотите возобновить продажи № {0} и переместить их в открытые?",
                                    numList),
                            "Да", "Нет", () -> {
                                lookup(SaleRepository.class).reopenSales(sales);
                                refreshContainer();
                                NotificationUtil.showSuccess(MessageFormat.format("Продажи ({0}) успешно возобновлены", numList));
                            });
                }
            });

        if (domain == ExtaDomain.SALES_OPENED) {
            actions.add(new ItemAction("Завершить", "Успешное завершение продажи", FontAwesome.FLAG_CHECKERED) {
                @Override
                public void fire(final Set itemIds) {
                    refreshContainerItems(itemIds);
                    final Set<Sale> sales = getEntities(itemIds);
                    final String numList = getSalesNumList(sales);
                    ConfirmDialog.show(UI.getCurrent(),
                            "Подтвердите действие...",
                            MessageFormat.format("Вы уверены, что хотите завершить выбранные продажи № {0}?", numList),
                            "Да", "Нет", () -> {
                                lookup(SaleRepository.class).finishSales(sales);
                                refreshContainer();
                                NotificationUtil.showSuccess(MessageFormat.format("Продажи ({0}) успешно завершены", numList));
                            });
                }
            });

            actions.add(new ItemAction("Отменить", "Отмена продажи", Fontello.CANCEL) {
                @Override
                public void fire(final Set itemIds) {
                    refreshContainerItems(itemIds);
                    final Set<Sale> sales = getEntities(itemIds);
                    final String numList = getSalesNumList(sales);
                    final ConfirmSaleClosingWindow win = new ConfirmSaleClosingWindow();
                    win.setCaption(MessageFormat.format("Вы уверены, что хотите отменить выбранные продажи № {0}?", numList));
                    win.addCloseListener(e -> {
                        if (win.isOkPressed()) {
                            lookup(SaleRepository.class).cancelSales(sales, win.getReason());
                            refreshContainer();
                            NotificationUtil.showSuccess(MessageFormat.format("Продажи ({0}) отменены", numList));
                        }
                    });
                    win.showModal();
                }
            });
        }

//		actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса к которому привязана текущая продажа", Fontello.SITEMAP) {
//            @Override
//            public void fire(Object itemId) {
//                final Sale curObj = extractBean(table.getItem(itemId));
//
//                // Ищем процесс к которому привязана текущая продажа
//                RuntimeService runtimeService = lookup(RuntimeService.class);
//                ProcessInstance process =
//                        runtimeService.createProcessInstanceQuery()
//                                .includeProcessVariables()
//                                .variableValueEquals("sale", curObj)
//                                .singleResult();
//
//                if (process != null) {
//                    // Показать статус выполнения процесса
//                    BPStatusForm statusForm = new BPStatusForm(process.getProcessInstanceId());
//                    statusForm.showModal();
//                } else {
//                    NotificationUtil.showWarning("Нет бизнес процесса с которым связана текущая продажа.");
//                }
//            }
//        });

        return actions;
    }

    private String getSalesNumList(final Set<Sale> sales) {
        return Joiner.on(", ").join(sales.stream().map(s -> s.getNum()).toArray());
    }

}
