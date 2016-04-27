/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.data.Container;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tepi.filtertable.FilterGenerator;
import ru.extas.model.insurance.Insurance;
import ru.extas.model.security.ExtaDomain;
import ru.extas.server.insurance.InsuranceCalculator;
import ru.extas.web.commons.*;
import ru.extas.web.commons.component.PhoneFilterGenerator;
import ru.extas.web.commons.container.ExtaDbContainer;
import ru.extas.web.commons.container.SecuredDataContainer;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.gwt.thirdparty.guava.common.collect.Maps.newHashMap;
import static ru.extas.server.ServiceLocator.lookup;

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        if (dataDecl == null)
            dataDecl = new InsuranceDataDecl();
        return dataDecl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final ExtaDbContainer<Insurance> container = SecuredDataContainer.create(Insurance.class, ExtaDomain.INSURANCE_PROP);
        container.addNestedContainerProperty("dealer.name");
        container.addNestedContainerProperty("client.name");
        container.addNestedContainerProperty("client.phone");
        container.sort(new Object[]{"createdDate"}, new boolean[]{false});
        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        final List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод нового полиса страхования"));
        actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке полис страхования"));
        actions.add(new EditObjectAction("Пролонгация", "Пролонгировать выделенный в списке полис страхования", Fontello.CLOCK) {
            @Override
            public void fire(final Set itemIds) {
                final Insurance oldIns = getFirstEntity(itemIds);

                final Insurance insurance = createEntity();
                // Копируем все необходимые данные из истекшего(истекающего) договора
                insurance.setClient(oldIns.getClient());
                insurance.setBeneficiary(oldIns.getBeneficiary());
                insurance.setUsedMotor(true);
                insurance.setMotorType(oldIns.getMotorType());
                insurance.setMotorBrand(oldIns.getMotorBrand());
                insurance.setMotorModel(oldIns.getMotorModel());
                insurance.setMotorVin(oldIns.getMotorVin());
                insurance.setCoverTime(oldIns.getCoverTime());
                // СС сумма при пролонгации = СС в первоначальном полисе
                insurance.setRiskSum(oldIns.getRiskSum());
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
                    public void fire(final Set itemIds) {
                        printPolicy(getFirstRefreshedEntity(itemIds), true);
                    }
                });

                group.add(new ItemAction("Печать без подложки", "Создать печатное представление полиса страхования без подложки", Fontello.PRINT_2) {
                    @Override
                    public void fire(final Set itemIds) {
                        printPolicy(getFirstRefreshedEntity(itemIds), false);
                    }
                });

                group.add(new ItemAction("Печать счета", "Создать печатную форму счета на оплату страховки", Fontello.PRINT_2) {
                    @Override
                    public void fire(final Set itemIds) {
                        printInvoice(getFirstRefreshedEntity(itemIds));
                    }
                });
                return group;
            }
        });

        return actions;
    }

    private void printPolicy(final Insurance insurance, final boolean withMat) {

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

            final String clientName = insurance.getClient().getName();
            final String policyNum = insurance.getRegNum();
            final String policyFileName = MessageFormat.format("Полис {0} {1} {2}.pdf", policyNum, clientName, new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));

            new DownloadFileWindow(outDoc.toByteArray(), policyFileName).showModal();

        } catch (final JRException e) {
            logger.error("Print policy error", e);
            throw Throwables.propagate(e);
        }
    }

    private void printInvoice(final Insurance insurance) {

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

            final String clientName = insurance.getClient().getName();
            final String regNum = insurance.getRegNum();
            final String invoiceFileName = MessageFormat.format("Счет {0} {1} {2}.pdf", regNum, clientName, new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));

            new DownloadFileWindow(outDoc.toByteArray(), invoiceFileName).showModal();
        } catch (final JRException e) {
            logger.error("Print policy error", e);
            throw Throwables.propagate(e);
        }
    }

    @Override
    protected FilterGenerator createFilterGenerator() {
        return new PhoneFilterGenerator("client.phone");
    }
}
