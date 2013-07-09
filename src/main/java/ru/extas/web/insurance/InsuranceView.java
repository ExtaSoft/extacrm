/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package ru.extas.web.insurance;

import static ru.extas.server.ServiceLocator.lookup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.extas.model.Insurance;
import ru.extas.server.InsuranceRepository;
import ru.extas.web.commons.ExtaAbstractView;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.OnDemandFileDownloader;
import ru.extas.web.commons.OnDemandFileDownloader.OnDemandStreamResource;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Раздел страхование
 * 
 * @author Valery Orlov
 * 
 */
public class InsuranceView extends ExtaAbstractView {

	private static final long serialVersionUID = -2524035728558575428L;
	private final Logger logger = LoggerFactory.getLogger(InsuranceView.class);

	private Table table;

	public InsuranceView() {
	}

	@Override
	protected Component getContent() {
		logger.info("Creating view content...");
		CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Запрос данных
		final InsuranceRepository insuranceRepository = lookup(InsuranceRepository.class);
		final Collection<Insurance> insurances = insuranceRepository.loadAll();
		final BeanItemContainer<Insurance> beans = new BeanItemContainer<Insurance>(Insurance.class);
		beans.addNestedContainerProperty("client.name");
		beans.addAll(insurances);

		HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		Button newPolyceBtn = new Button("Новый", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				final Insurance newObj = new Insurance();

				final InsuranceEditForm editWin = new InsuranceEditForm("Новый полис", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							beans.addBean(newObj);
							table.setValue(newObj);
							Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		newPolyceBtn.addStyleName("icon-doc-new");
		newPolyceBtn.setDescription("Ввод нового полиса страхования");
		commandBar.addComponent(newPolyceBtn);

		Button editPolyceBtn = new Button("Изменить", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// Взять текущий полис из грида
				final Insurance selObj = (Insurance) table.getValue();
				if (selObj == null) {
					Notification.show("Нечего редактировать", "Нет выбранной записи.", Type.WARNING_MESSAGE);
					return;
				}

				final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", selObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						if (editWin.isSaved()) {
							// FIXME: Избавиться от дорогой операции обновления
							table.refreshRowCache();
							Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
						}
					}
				});
				editWin.showModal();
			}
		});
		editPolyceBtn.addStyleName("icon-edit-3");
		editPolyceBtn.setDescription("Редактировать выделенный в списке полис страхования");
		commandBar.addComponent(editPolyceBtn);

		Button printPolyceBtn = new Button("Печать");
		printPolyceBtn.addStyleName("icon-print-2");
		printPolyceBtn.setDescription("Создать печатное представление полиса страхования");
		createPolicyDownloader().extend(printPolyceBtn);
		commandBar.addComponent(printPolyceBtn);

		panel.addComponent(commandBar);

		table = new Table("Страховки", beans);
		table.setImmediate(true);
		table.setSizeFull();

		GridDataDecl ds = new InsuranceDataDecl();
		ds.initTableColumns(table);

		panel.addComponent(table);
		return panel;
	}

	/**
	 * @return
	 */
	@Override
	protected Component getTitle() {
		final Component title = new Label("Страхование техники");
		title.setSizeUndefined();
		title.addStyleName("h1");
		return title;
	}

	// TODO: Предоставлять полис в формате PDF
	// private void printPolicy(Insurance insurance) {
	// // Формирование отчета
	// try {
	// Window window = new Window("Полис для");
	// window.setWidth("70%");
	// window.setHeight("80%");
	// window.setModal(true);
	// BrowserFrame e = new BrowserFrame();
	// e.setSizeFull();
	// window.center();
	// StreamResource resource = createPolicyResource(insurance);
	// e.setSource(resource);
	// window.setContent(e);
	// UI.getCurrent().addWindow(window);
	//
	// Notification.show("Полис напечатан", Type.TRAY_NOTIFICATION);
	//
	// } catch (Exception e) {
	// logger.error("Print polycy error", e);
	// Notification.show("Ошибка печати полиса", e.getMessage(),
	// Type.ERROR_MESSAGE);
	// }
	//
	// }

	/**
	 * @param insurance
	 * @return
	 * @throws XDocReportException
	 * @throws IOException
	 */
	private OnDemandFileDownloader createPolicyDownloader() {
		return new OnDemandFileDownloader(new OnDemandStreamResource() {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				// Взять текущий полис из грида
				final Insurance insurance = (Insurance) table.getValue();
				if (insurance == null) {
					Notification.show("Нечего печатать", "Нет выбранной записи.", Type.WARNING_MESSAGE);
					// FIXME: Нельзя возвращать null
					return null;
				}
				try {
					// 1) Load Docx file by filling Velocity template engine and
					// cache
					// it to the registry
					InputStream in = getClass().getResourceAsStream("/reports/insurance/PropertyInsuranceTemplate.docx");
					IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker);

					// 2) Create context Java model
					IContext context = report.createContext();
					context.put("ins", insurance);

					// 3) Generate report by merging Java model with the Docx
					final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
					report.process(context, outDoc);

					// Конвертируем в PDF
					// 1) Load DOCX into XWPFDocument
					// XWPFDocument document = new XWPFDocument(new
					// ByteArrayInputStream(outDoc.toByteArray()));
					//
					// // 2) Prepare Pdf options
					// PdfOptions options = PdfOptions.create();
					// options.fontEncoding("cp1251");
					// options.fontProvider(new ExtaFontProvider());
					//
					// // 3) Convert XWPFDocument to Pdf
					// // final ByteArrayOutputStream outPdf = new
					// // ByteArrayOutputStream(outDoc.size());
					// // PdfConverter.getInstance().convert(document, outPdf,
					// options);

					return new ByteArrayInputStream(outDoc.toByteArray());
				} catch (IOException | XDocReportException e) {
					logger.error("Print polycy error", e);
					Notification.show("Ошибка печати полиса", e.getMessage(), Type.ERROR_MESSAGE);
				}
				// FIXME: Нельзя возвращать null
				return null;
			}

			@Override
			public String getFilename() {
				// Взять текущий полис из грида
				final Insurance insurance = (Insurance) table.getValue();
				String clientName = insurance.getClient().getName();
				String policyNum = insurance.getRegNum();
				try {
					String fileName = MessageFormat.format("Полис.{0}.{1}.docx", policyNum, clientName).replaceAll(" ", ".");
					return URLEncoder.encode(fileName, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("Print polycy error", e);
				}
				return "UnknownFileName.doc";
			}
		});
	}
}
