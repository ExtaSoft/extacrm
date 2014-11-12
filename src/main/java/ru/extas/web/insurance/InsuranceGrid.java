/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.addon.tableexport.CustomTableHolder;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.security.ExtaDomain;
import ru.extas.server.insurance.InsuranceCalculator;
import ru.extas.web.commons.*;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.gwt.thirdparty.guava.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * <p>InsuranceGrid class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class InsuranceGrid extends ExtaGrid<Insurance> {

    private static final long serialVersionUID = -2317741378090152128L;
    private final static Logger logger = LoggerFactory.getLogger(InsuranceGrid.class);
    private InsuranceDataDecl dataDecl;

    /**
     * <p>Constructor for InsuranceGrid.</p>
     */
    public InsuranceGrid() {
        super(Insurance.class);
    }

    @Override
    public ExtaEditForm<Insurance> createEditForm(final Insurance insurance, final boolean isInsert) {
        return new InsuranceEditForm(insurance);
    }

    /** {@inheritDoc} */
    @Override
    protected GridDataDecl createDataDecl() {
        if (dataDecl == null)
            dataDecl = new InsuranceDataDecl();
        return dataDecl;
    }

    /** {@inheritDoc} */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDataContainer<Insurance> container = new SecuredDataContainer<>(Insurance.class, ExtaDomain.INSURANCE_PROP);
        container.addNestedContainerProperty("dealer.name");
        container.addNestedContainerProperty("clientPP.name");
        container.addNestedContainerProperty("clientPP.phone");
        container.addNestedContainerProperty("clientPP.birthday");
        container.addNestedContainerProperty("clientLE.name");
        container.addNestedContainerProperty("clientLE.phone");
        container.sort(new Object[]{"createdDate"}, new boolean[]{false});
        return container;
    }

    /** {@inheritDoc} */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод нового полиса страхования"));
        actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке полис страхования"));
        actions.add(new EditObjectAction("Пролонгация", "Пролонгировать выделенный в списке полис страхования", Fontello.CLOCK) {
            @Override
            public void fire(final Object itemId) {
                final Insurance oldIns = GridItem.extractBean(table.getItem(itemId));

                final Insurance insurance = createEntity();
                // Копируем все необходимые данные из истекшего(истекающего) договора
                insurance.setClientPP(oldIns.getClientPP());
                insurance.setClientLE(oldIns.getClientLE());
                insurance.setBeneficiary(oldIns.getBeneficiary());
                insurance.setUsedMotor(true);
                insurance.setMotorType(oldIns.getMotorType());
                insurance.setMotorBrand(oldIns.getMotorBrand());
                insurance.setMotorModel(oldIns.getMotorModel());
                insurance.setMotorVin(oldIns.getMotorVin());
                insurance.setCoverTime(oldIns.getCoverTime());
                // СС сумма при пролонгации = СС в первоначальном полисе – 20%
                insurance.setRiskSum(oldIns.getRiskSum().multiply(BigDecimal.valueOf(8L, 1)));
                final InsuranceCalculator calc = lookup(InsuranceCalculator.class);
                final BigDecimal premium = calc.calcPropInsPremium(insurance);
                insurance.setPremium(premium);
                insurance.setDealer(oldIns.getDealer());
                insurance.setSaleNum(oldIns.getSaleNum());
                insurance.setSaleDate(oldIns.getSaleDate());

                doEditNewObject(insurance);
            }
        });

        actions.add(new UIActionGroup("Печать", "Создать печатное представление полиса страхования", Fontello.PRINT_2) {
            @Override
            protected List<UIAction> makeActionsGroup() {
                final List<UIAction> group = newArrayList();
                group.add(new ItemAction("Печать", "Создать печатное представление полиса страхования", Fontello.PRINT_2) {
                    @Override
                    public void fire(final Object itemId) {
                        printPolicy(itemId, true);
                    }
                });

                group.add(new ItemAction("Печать без подложки", "Создать печатное представление полиса страхования без подложки", Fontello.PRINT_2) {
                    @Override
                    public void fire(final Object itemId) {
                        printPolicy(itemId, false);
                    }
                });

                group.add(new ItemAction("Печать счета", "Создать печатную форму счета на оплату страховки", Fontello.PRINT_2) {
                    @Override
                    public void fire(final Object itemId) {
                        printInvoice(itemId);
                    }
                });
                return group;
            }
        });

        actions.add(new UIAction("Экспорт", "Экспорт содержимого таблицы в Excel файл", Fontello.GRID) {
            @Override
            public void fire(final Object itemId) {
                exportTableData();
            }
        });

        return actions;
    }

    private void exportTableData() {
        final CustomTableHolder tableHolder = new CustomTableHolder(table);
        final ExcelExport excelExport = new MyExcelExport(tableHolder);
        //excelExport.setExcelFormatOfProperty("date", "yyyy-MM-dd");
        excelExport.excludeCollapsedColumns();
        excelExport.setReportTitle("Имущественные страховки");
        final String fileName = MessageFormat.format("PropertyInsurances {0}.xls", new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));
        excelExport.setExportFileName(fileName);
        excelExport.export();
    }

    private void printPolicy(final Object itemId, final boolean withMat) {

        final Insurance insurance = extractBean(table.getItem(itemId));
        checkNotNull(insurance, "Нечего печатать", "Нет выбранной записи.");

        try {
            final InputStream in = getClass().getResourceAsStream("/reports/insurance/PropertyInsuranceTemplate.jasper");

            final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(in);

            final Map<String, Object> params = newHashMap();
            params.put("ins", insurance);
            params.put("withMat", withMat);
            params.put("periodOfCover",
                    insurance.getCoverTime() == null || insurance.getCoverTime() == Insurance.PeriodOfCover.YEAR
                            ? "12 месяцев" : "6 месяцев"
            );
            final NumberFormat format = NumberFormat.getInstance(lookup(Locale.class));
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);
            params.put("moneyFormatter", format);
            final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));


            final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outDoc);

            final String clientName = insurance.getClientName();
            final String policyNum = insurance.getRegNum();
            final String policyFileName = MessageFormat.format("Полис {0} {1} {2}.pdf", policyNum, clientName, new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));

            new DownloadFileWindow(outDoc.toByteArray(), policyFileName).showModal();

        } catch (final JRException e) {
            logger.error("Print policy error", e);
            throw Throwables.propagate(e);
        }
    }

    private void printInvoice(final Object itemId) {

        final Insurance insurance = extractBean(table.getItem(itemId));
        checkNotNull(insurance, "Нечего печатать", "Нет выбранной записи.");

        try {
            final InputStream in = getClass().getResourceAsStream("/reports/insurance/InsuranceInvoiceTemplate.jasper");

            final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(in);

            final Map<String, Object> params = newHashMap();
            params.put("ins", insurance);
            final NumberFormat format = NumberFormat.getInstance(lookup(Locale.class));
            format.setMinimumFractionDigits(2);
            format.setMaximumFractionDigits(2);
            params.put("moneyFormatter", format);
            final JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));


            final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outDoc);

            final String clientName = insurance.getClientName();
            final String regNum = insurance.getRegNum();
            final String invoiceFileName = MessageFormat.format("Счет {0} {1} {2}.pdf", regNum, clientName, new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));

            new DownloadFileWindow(outDoc.toByteArray(), invoiceFileName).showModal();
        } catch (final JRException e) {
            logger.error("Print policy error", e);
            throw Throwables.propagate(e);
        }
    }

}
