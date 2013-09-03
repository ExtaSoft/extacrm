/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Insurance;
import ru.extas.model.UserRole;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;
import ru.extas.web.commons.ExportTableDownloader;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.OnDemandFileDownloader;
import ru.extas.web.commons.OnDemandFileDownloader.OnDemandStreamResource;

import java.io.*;
import java.net.URLEncoder;
import java.text.MessageFormat;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Valery Orlov
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
        final LazyJdoContainer<Insurance> container = new LazyJdoContainer<>(Insurance.class, 50, null);
        container.addContainerProperty("client.name", String.class, null, true, false);
        container.addContainerProperty("client.personInfo.birthday", LocalDate.class, null, true, false);
        container.addContainerProperty("client.cellPhone", String.class, null, true, false);
        final Subject subject = SecurityUtils.getSubject();
        // пользователю доступны только собственные записи
        if (subject.hasRole(UserRole.USER.getName())) {
            container.addContainerFilter(new Compare.Equal("createdBy", subject.getPrincipal()));
        }

        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);

        final Button newPolicyBtn = new Button("Новый", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                final Object newObjId = container.addItem();
                final BeanItem<Insurance> newObj = (BeanItem<Insurance>) container.getItem(newObjId);

                final InsuranceEditForm editWin = new InsuranceEditForm("Новый полис", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.commit();
                            table.select(newObjId);
                            Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
                        } else {
                            container.discard();
                        }
                    }
                });
                editWin.showModal();
            }
        });
        newPolicyBtn.addStyleName("icon-doc-new");
        newPolicyBtn.setDescription("Ввод нового полиса страхования");
        commandBar.addComponent(newPolicyBtn);

        final Button editPolicyBtn = new Button("Изменить", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<Insurance> curObj = (BeanItem<Insurance>) table.getItem(curObjId);

                final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        editPolicyBtn.addStyleName("icon-edit-3");
        editPolicyBtn.setDescription("Редактировать выделенный в списке полис страхования");
        editPolicyBtn.setEnabled(false);
        commandBar.addComponent(editPolicyBtn);

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
        final Button printPolicyBan = new Button("Печать без подложки");
        printPolicyBan.addStyleName("icon-print-2");
        printPolicyBan.setDescription("Создать печатное представление полиса страхования без подложки");
        printPolicyBan.setEnabled(false);
        createPolicyDownloader(false).extend(printPolicyBan);
        commandBar.addComponent(printPolicyBan);

        final Button exportBtn = new Button("Экспорт");
        exportBtn.addStyleName("icon-table");
        exportBtn.setDescription("Экспорт содержимого таблицы в CSV файл");
        new ExportTableDownloader(table, "PropertyInsurances.csv").extend(exportBtn);
        commandBar.addComponent(exportBtn);

        panel.addComponent(commandBar);

        table.setContainerDataSource(container);
        table.setSizeFull();

        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.setImmediate(true);
        table.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final boolean enableBtb = event.getProperty().getValue() != null;
                editPolicyBtn.setEnabled(enableBtb);
                printPolicyBan.setEnabled(enableBtb);
                printPolyceMatBtn.setEnabled(enableBtb);
            }
        });
// if (table.size() > 0)
// table.select(table.firstItemId());

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
    // logger.error("Print policy error", e);
    // Notification.show("Ошибка печати полиса", e.getMessage(),
    // Type.ERROR_MESSAGE);
    // }
    //
    // }

    private OnDemandFileDownloader createPolicyDownloader(final boolean withMat) {
        return new OnDemandFileDownloader(new OnDemandStreamResource() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public InputStream getStream() {
                // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<Insurance> curObj = (BeanItem<Insurance>) table.getItem(curObjId);
                final Insurance insurance = curObj.getBean();
                checkNotNull(insurance, "Нечего печатать", "Нет выбранной записи.");
                try {
                    // 1) Load Docx file by filling Velocity template engine and
                    // cache it to the registry
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
                    logger.error("Print policy error", e);
                    throw Throwables.propagate(e);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public String getFilename() {
                // Взять текущий полис из грида
                // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<Insurance> curObj = (BeanItem<Insurance>) table.getItem(curObjId);
                final Insurance insurance = curObj.getBean();
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
