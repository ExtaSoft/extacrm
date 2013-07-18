/**
 * 
 */
package ru.extas.web.insurance;

import static com.google.common.base.Preconditions.checkNotNull;
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
import ru.extas.web.commons.ExportTableDownloader;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.OnDemandFileDownloader;
import ru.extas.web.commons.OnDemandFileDownloader.OnDemandStreamResource;

import com.google.common.base.Throwables;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
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
 * @author Valery Orlov
 * 
 */
public class InsuranceGrid extends CustomComponent {

	private static final long serialVersionUID = -2317741378090152128L;

	private final Logger logger = LoggerFactory.getLogger(InsuranceGrid.class);

	private final Table table = new Table();

	public InsuranceGrid() {

		final CssLayout panel = new CssLayout();
		panel.addStyleName("layout-panel");
		panel.setSizeFull();

		// Запрос данных
		final InsuranceRepository insuranceRepository = lookup(InsuranceRepository.class);
		final Collection<Insurance> insurances = insuranceRepository.loadAll();
		final BeanItemContainer<Insurance> beans = new BeanItemContainer<Insurance>(Insurance.class);
		beans.addNestedContainerProperty("client.name");
		beans.addAll(insurances);

		final HorizontalLayout commandBar = new HorizontalLayout();
		commandBar.addStyleName("configure");
		commandBar.setSpacing(true);

		final Button newPolyceBtn = new Button("Новый", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final Insurance newObj = new Insurance();

				final InsuranceEditForm editWin = new InsuranceEditForm("Новый полис", newObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
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

		final Button editPolyceBtn = new Button("Изменить", new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				// Взять текущий полис из грида
				final Insurance selObj = checkNotNull((Insurance)table.getValue(), "No selected row");

				final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", selObj);
				editWin.addCloseListener(new CloseListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(final CloseEvent e) {
						if (editWin.isSaved()) {
							// TODO: Избавиться от дорогой операции обновления
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
		editPolyceBtn.setEnabled(false);
		commandBar.addComponent(editPolyceBtn);

		// PopupButton popupButton = new PopupButton("Action");
		// HorizontalLayout popupLayout = new HorizontalLayout();
		// popupButton.setContent(popupLayout); // Set popup content
		// Button modifyButton = new Button("Modify");
		// modifyButton.setIcon(new
		// ThemeResource("../runo/icons/16/document-txt.png"));
		// popupLayout.addComponent(modifyButton);
		// Button addButton = new Button("Add");
		// addButton.setIcon(new
		// ThemeResource("../runo/icons/16/document-add.png"));
		// popupLayout.addComponent(addButton);
		// Button deleteButton = new Button("Delete");
		// deleteButton.setIcon(new
		// ThemeResource("../runo/icons/16/document-delete.png"));
		// commandBar.addComponent(popupButton);
		// popupLayout.addComponent(deleteButton);

		final Button printPolyceMatBtn = new Button("Печать");
		printPolyceMatBtn.addStyleName("icon-print-2");
		printPolyceMatBtn.setDescription("Создать печатное представление полиса страхования");
		printPolyceMatBtn.setEnabled(false);
		createPolicyDownloader(true).extend(printPolyceMatBtn);
		commandBar.addComponent(printPolyceMatBtn);

		// TODO Заметить на раскрывающуюся кнопку
		final Button printPolyceBtn = new Button("Печать без подложки");
		printPolyceBtn.addStyleName("icon-print-2");
		printPolyceBtn.setDescription("Создать печатное представление полиса страхования без подложки");
		printPolyceBtn.setEnabled(false);
		createPolicyDownloader(false).extend(printPolyceBtn);
		commandBar.addComponent(printPolyceBtn);

		final Button exportBtn = new Button("Экспорт");
		exportBtn.addStyleName("icon-table");
		exportBtn.setDescription("Экспорт содержимого таблицы в CSV файл");
		new ExportTableDownloader(table, "PropertyInsurances.csv").extend(exportBtn);
		commandBar.addComponent(exportBtn);

		panel.addComponent(commandBar);

		table.setContainerDataSource(beans);
		table.setSizeFull();

		// Обеспечиваем корректную работу кнопок зависящих от выбранной записи
		table.setImmediate(true);
		table.setValue(table.getItem(table.firstItemId()));
		table.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				final boolean enableBtb = event.getProperty().getValue() != null;
				editPolyceBtn.setEnabled(enableBtb);
				printPolyceBtn.setEnabled(enableBtb);
				printPolyceMatBtn.setEnabled(enableBtb);
			}
		});

		final GridDataDecl ds = new InsuranceDataDecl();
		ds.initTableColumns(table);

		panel.addComponent(table);

		setCompositionRoot(panel);
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

	private OnDemandFileDownloader createPolicyDownloader(final boolean withMat) {
		return new OnDemandFileDownloader(new OnDemandStreamResource() {
			private static final long serialVersionUID = 1L;

			@Override
			public InputStream getStream() {
				// Взять текущий полис из грида
				final Insurance insurance = (Insurance)table.getValue();
				checkNotNull(insurance, "Нечего печатать", "Нет выбранной записи.");
				try {
					// 1) Load Docx file by filling Velocity template engine and
					// cache
					// it to the registry
					final InputStream in = getClass().getResourceAsStream(
							withMat ? "/reports/insurance/PropertyInsuranceTemplateWhitMat.docx"
									: "/reports/insurance/PropertyInsuranceTemplate.docx");
					final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
							TemplateEngineKind.Freemarker);

					// 2) Create context Java model
					final IContext context = report.createContext();
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
					throw Throwables.propagate(e);
				}
			}

			@Override
			public String getFilename() {
				// Взять текущий полис из грида
				final Insurance insurance = (Insurance)table.getValue();
				final String clientName = insurance.getClient().getName();
				final String policyNum = insurance.getRegNum();
				try {
					final String fileName = MessageFormat.format("Полис.{0}.{1}.docx", policyNum, clientName)
							.replaceAll(" ", ".");
					return URLEncoder.encode(fileName, "UTF-8");
				} catch (final UnsupportedEncodingException e) {
					logger.error("Print polycy error", e);
				}
				return "UnknownFileName.doc";
			}
		});
	}

}
