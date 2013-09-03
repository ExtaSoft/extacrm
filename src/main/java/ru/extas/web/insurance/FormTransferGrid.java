/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.FormTransfer;
import ru.extas.model.PersonInfo;
import ru.extas.utils.ValueUtil;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.OnDemandFileDownloader;

import java.io.*;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Valery Orlov
 */
public class FormTransferGrid extends CustomComponent {

    private static final long serialVersionUID = 1170175803163742829L;

    private final Logger logger = LoggerFactory.getLogger(FormTransferGrid.class);

    private final Table table = new Table();

    public FormTransferGrid() {

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Запрос данных
        final LazyJdoContainer<FormTransfer> container = new LazyJdoContainer<>(FormTransfer.class, 50,
                null);
        container.addContainerProperty("fromContact.name", String.class, null, true, false);
        container.addContainerProperty("toContact.name", String.class, null, true, false);

        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);

        final Button newTFBtn = new Button("Новый", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                final Object newObjId = container.addItem();
                final BeanItem<FormTransfer> newObj = (BeanItem<FormTransfer>) container.getItem(newObjId);

                final FormTransferEditForm editWin = new FormTransferEditForm("Новый акт приема/передачи", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.commit();
                            table.select(newObjId);
                            Notification.show("Акт приема/передачи сохранен", Type.TRAY_NOTIFICATION);
                        } else {
                            container.discard();
                        }
                    }
                });
                editWin.showModal();
            }
        });
        newTFBtn.addStyleName("icon-doc-new");
        newTFBtn.setDescription("Ввод нового акта приема/передачи");
        commandBar.addComponent(newTFBtn);

        final Button editTFBtn = new Button("Изменить", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<FormTransfer> curObj = (BeanItem<FormTransfer>) table.getItem(curObjId);

                final FormTransferEditForm editWin = new FormTransferEditForm("Редактировать акт приема/передачи",
                        curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            Notification.show("Акт приема/передачи сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        editTFBtn.addStyleName("icon-edit-3");
        editTFBtn.setDescription("Редактировать выделенный в списке акта приема/передачи");
        editTFBtn.setEnabled(false);
        commandBar.addComponent(editTFBtn);

        final Button printTFBtn = new Button("Печать");
        printTFBtn.addStyleName("icon-print-2");
        printTFBtn.setDescription("Создать печатное представление акта приема передачи квитанций");
        printTFBtn.setEnabled(false);
        createFormTransferDownloader().extend(printTFBtn);
        commandBar.addComponent(printTFBtn);

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
                editTFBtn.setEnabled(enableBtb);
                printTFBtn.setEnabled(enableBtb);
            }
        });

        final GridDataDecl ds = new FormTransferDataDecl();
        ds.initTableColumns(table);

        panel.addComponent(table);

        setCompositionRoot(panel);
    }

    private OnDemandFileDownloader createFormTransferDownloader() {
        return new OnDemandFileDownloader(new OnDemandFileDownloader.OnDemandStreamResource() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public InputStream getStream() {
                // Взять текущий акт из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<FormTransfer> curObj = (BeanItem<FormTransfer>) table.getItem(curObjId);
                final FormTransfer formTransfer = curObj.getBean();
                checkNotNull(formTransfer, "Нечего печатать", "Нет выбранной записи.");
                try {
                    // 1) Load Docx file by filling Velocity template engine and
                    // cache
                    // it to the registry
                    final InputStream in = getClass().getResourceAsStream("/reports/insurance/FormTransferTemplate.docx");
                    final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
                            TemplateEngineKind.Freemarker);

                    // 2) Create context Java model
                    // Продготовка параметров отчета:
                    // Дата передачи
                    final Date transferDate = formTransfer.getTransferDate().toDate();
                    // Передающее юр. лицо
                    final String fromCompanyName = formTransfer.getFromContact().getAffiliation().getCompanyInfo().getFullName();
                    // Передающеее физ. лицо
                    final String fromContactName = formTransfer.getFromContact().getName();
                    // Принимающее физ. лицо
                    final String toContactName = formTransfer.getToContact().getName();
                    // Паспорт принимающего
                    final PersonInfo toContactPass = formTransfer.getToContact().getPersonInfo();
                    // Список форм
                    final List<String> formsList = formTransfer.getFormNums();
                    // Колличество форм прописью
                    final String formNumsStr = ValueUtil.spellOutThing(formsList.size());
                    final IContext context = report.createContext();
                    context.put("formTransfer", formTransfer);
                    context.put("transferDate", transferDate);
                    context.put("fromCompanyName", fromCompanyName);
                    context.put("fromContactName", fromContactName);
                    context.put("toContactName", toContactName);
                    context.put("toContactPass", toContactPass);
                    context.put("formsList", formsList);
                    context.put("formNumsStr", formNumsStr);

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
                    logger.error("Print Form Transfer error", e);
                    throw Throwables.propagate(e);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public String getFilename() {
                // Взять текущий полис из грида
                // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<FormTransfer> curObj = (BeanItem<FormTransfer>) table.getItem(curObjId);
                final FormTransfer formTransfer = curObj.getBean();
                final String clientName = formTransfer.getToContact().getName();
                try {
                    final String fileName = MessageFormat.format("Акт.приема.передачи.А7.({0}).docx", clientName)
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
