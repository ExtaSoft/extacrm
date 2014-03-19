/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Insurance;
import ru.extas.server.InsuranceCalculator;
import ru.extas.web.commons.*;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * @author Valery Orlov
 */
public class InsuranceGrid extends ExtaGrid {

	private static final long serialVersionUID = -2317741378090152128L;
	private final static Logger logger = LoggerFactory.getLogger(InsuranceGrid.class);
	private InsuranceDataDecl dataDecl;

	public InsuranceGrid() {
	}

	@Override
	protected GridDataDecl createDataDecl() {
		if (dataDecl == null)
			dataDecl = new InsuranceDataDecl();
		return dataDecl;
	}

	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<Insurance> container = new ExtaDataContainer<>(Insurance.class);
		container.addNestedContainerProperty("client.name");
		container.addNestedContainerProperty("client.birthday");
		container.addNestedContainerProperty("client.phone");
		container.addNestedContainerProperty("dealer.name");
		return container;
	}

	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		actions.add(new UIAction("Новый", "Ввод нового полиса страхования", "icon-doc-new") {

			@Override
			public void fire(Object itemId) {
				final BeanItem<Insurance> newObj = new BeanItem<>(new Insurance());

				final InsuranceEditForm editWin = new InsuranceEditForm("Новый полис", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainer();
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке полис страхования", "icon-edit-3") {
			@Override
			public void fire(final Object itemId) {
				final BeanItem<Insurance> curObj = new GridItem<>(table.getItem(itemId));

				final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", curObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							refreshContainerItem(itemId);
						}
					}
				});
				editWin.showModal();
			}
		});

		actions.add(new ItemAction("Пролонгация", "Пролонгировать выделенный в списке полис страхования", "icon-clock") {
			@Override
			public void fire(Object itemId) {
                final BeanItem<Insurance> curItem = new GridItem<>(table.getItem(itemId));
                Insurance oldIns = curItem.getBean();

                Insurance insurance = new Insurance();
                // Копируем все необходимые данные из истекшего(истекающего) договора
                insurance.setClient(oldIns.getClient());
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

                final InsuranceEditForm editWin = new InsuranceEditForm("Пролонгация полиса", new BeanItem<>(insurance));
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            refreshContainer();
                        }
                    }
                });
                editWin.showModal();
			}
		});

		actions.add(new ItemAction("Печать", "Создать печатное представление полиса страхования", "icon-print-2") {
			@Override
			public void fire(Object itemId) {
				printPolicy(itemId, true);
			}
		});

		actions.add(new ItemAction("Печать без подложки", "Создать печатное представление полиса страхования без подложки", "icon-print-2") {
			@Override
			public void fire(Object itemId) {
				printPolicy(itemId, false);
			}
		});

		actions.add(new ItemAction("Печать счета", "Создать печатную форму счета на оплату страховки", "icon-print-2") {
			@Override
			public void fire(Object itemId) {
				printInvoice(itemId);
			}
		});

		actions.add(new UIAction("Экспорт", "Экспорт содержимого таблицы в CSV файл", "icon-grid") {
			@Override
			public void fire(Object itemId) {
				exportTableData();
			}
		});

		return actions;
	}

	private void exportTableData() {

		try {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			CsvUtil.containerToCsv(container, createDataDecl(), getLocale(), out);
			new DownloadFileWindow(out.toByteArray(), "PropertyInsurances.csv").showModal();
		} catch (IOException e) {
			logger.error("Export to CSV error", e);
			throw Throwables.propagate(e);
		}
	}

	private void printPolicy(Object itemId, boolean withMat) {

		final Insurance insurance = extractBean(table.getItem(itemId));
		checkNotNull(insurance, "Нечего печатать", "Нет выбранной записи.");

		try {
			final InputStream in = getClass().getResourceAsStream("/reports/insurance/PropertyInsuranceTemplate.jasper");

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(in);

			final Map<String, Object> params = newHashMap();
			params.put("ins", insurance);
			params.put("withMat", withMat);
			params.put("periodOfCover",
					insurance.getCoverTime() == null || insurance.getCoverTime() == Insurance.PeriodOfCover.YEAR
							? "12 месяцев" : "6 месяцев");
			NumberFormat format = NumberFormat.getInstance(lookup(Locale.class));
			format.setMinimumFractionDigits(2);
			format.setMaximumFractionDigits(2);
			params.put("moneyFormatter", format);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));


			final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outDoc);

			final String clientName = insurance.getClient().getName();
			final String policyNum = insurance.getRegNum();
			final String policyFileName = MessageFormat.format("Полис {0} {1} {2}.pdf", policyNum, clientName, new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));

			new DownloadFileWindow(outDoc.toByteArray(), policyFileName).showModal();

		} catch (JRException e) {
			logger.error("Print policy error", e);
			throw Throwables.propagate(e);
		}
	}

	private void printInvoice(Object itemId) {

		final Insurance insurance = extractBean(table.getItem(itemId));
		checkNotNull(insurance, "Нечего печатать", "Нет выбранной записи.");

		try {
			final InputStream in = getClass().getResourceAsStream("/reports/insurance/InsuranceInvoiceTemplate.jasper");

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(in);

			final Map<String, Object> params = newHashMap();
			params.put("ins", insurance);
			NumberFormat format = NumberFormat.getInstance(lookup(Locale.class));
			format.setMinimumFractionDigits(2);
			format.setMaximumFractionDigits(2);
			params.put("moneyFormatter", format);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));


			final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outDoc);

			final String clientName = insurance.getClient().getName();
			final String regNum = insurance.getRegNum();
			final String invoiceFileName = MessageFormat.format("Счет {0} {1} {2}.pdf", regNum, clientName, new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()));

			new DownloadFileWindow(outDoc.toByteArray(), invoiceFileName).showModal();
		} catch (JRException e) {
			logger.error("Print policy error", e);
			throw Throwables.propagate(e);
		}
	}
}
