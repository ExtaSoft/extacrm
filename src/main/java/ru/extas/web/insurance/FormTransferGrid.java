/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
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
import ru.extas.model.Company;
import ru.extas.model.FormTransfer;
import ru.extas.model.Person;
import ru.extas.utils.ValueUtil;
import ru.extas.web.commons.*;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 */
public class FormTransferGrid extends ExtaGrid {

    private static final long serialVersionUID = 1170175803163742829L;
    private final Logger logger = LoggerFactory.getLogger(FormTransferGrid.class);

    public FormTransferGrid() {

    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new FormTransferDataDecl();
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final JPAContainer<FormTransfer> container = new ExtaDataContainer<>(FormTransfer.class);
        container.addNestedContainerProperty("fromContact.name");
        container.addNestedContainerProperty("toContact.name");

        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new UIAction("Новый", "Ввод нового акта приема/передачи", "icon-doc-new") {
            @Override
            public void fire(Object itemId) {
                final BeanItem<FormTransfer> newObj = new BeanItem<>(new FormTransfer());

                final FormTransferEditForm editWin = new FormTransferEditForm("Новый акт приема/передачи", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refresh();
                            Notification.show("Акт приема/передачи сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке акта приема/передачи", "icon-edit-3") {
            @Override
            public void fire(final Object itemId) {
                final BeanItem<FormTransfer> curObj = new BeanItem<>(((EntityItem<FormTransfer>) table.getItem(itemId)).getEntity());

                final FormTransferEditForm editWin = new FormTransferEditForm("Редактировать акт приема/передачи",
                        curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refreshItem(itemId);
                            Notification.show("Акт приема/передачи сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        return actions;
    }

    private void printFormTransfer() {
        // Взять текущий акт из грида
        final Object curObjId = checkNotNull(table.getValue(), "No selected row");
        final EntityItem<FormTransfer> curObj = (EntityItem<FormTransfer>) table.getItem(curObjId);
        final FormTransfer formTransfer = curObj.getEntity();
        checkNotNull(formTransfer, "Нечего печатать", "Нет выбранной записи.");

        if (!canPrintForm(formTransfer))
            return;

        try {
            // 1) Load Docx file by filling Velocity template engine and
            // cache
            // it to the registry
            final InputStream in = getClass().getResourceAsStream("/reports/insurance/FormTransferTemplate.docx");
            final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
                    TemplateEngineKind.Freemarker);

            // 2) Create context Java model
            // Продготовка параметров отчета:
            // Передающеее физ. лицо
            final Person fromContact = formTransfer.getFromContact();
            // Передающее юр. лицо
            final Company fromCompany = fromContact.getAffiliation();
            // Принимающее физ. лицо
            final Person toContact = formTransfer.getToContact();
            // Колличество форм прописью
            final String formNumsStr = ValueUtil.spellOutThing(formTransfer.getFormNums().size());
            final IContext context = report.createContext();
            context.put("formTransfer", formTransfer);
            context.put("fromCompany", fromCompany);
            context.put("fromContact", fromContact);
            context.put("toContact", toContact);
            context.put("formNumsStr", formNumsStr);

            // 3) Generate report by merging Java model with the Docx
            final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
            report.process(context, outDoc);

            new DownloadFileWindow(
                    outDoc.toByteArray(),
                    MessageFormat.format("Акт.приема.передачи.А7.{0}.docx",
                            formTransfer.getToContact().getName()))
                    .showModal();

        } catch (IOException | XDocReportException e) {
            logger.error("Print Form Transfer error", e);
            throw Throwables.propagate(e);
        }
    }

    private boolean canPrintForm(FormTransfer formTransfer) {
        final Person fromContact = formTransfer.getFromContact();
        final Person toContact = formTransfer.getToContact();

        List<String> messages = newArrayList();
        if (fromContact.getAffiliation() == null)
            messages.add(MessageFormat.format("У конткта \"{0}\" нет информации о компании (организации).", fromContact.getName()));
        if (toContact.getPassNum() == null)
            messages.add(MessageFormat.format("У конткта \"{0}\" не заполнено поле \"Номер паспорта\".", toContact.getName()));
        if (toContact.getPassIssueDate() == null)
            messages.add(MessageFormat.format("У конткта \"{0}\" не заполнено поле \"Дата выдачи паспорта\".", toContact.getName()));
        if (toContact.getPassIssuedBy() == null)
            messages.add(MessageFormat.format("У конткта \"{0}\" не заполнено поле \"Кем выдан паспорт\".", toContact.getName()));

        if (!messages.isEmpty()) {
            NotificationUtil.showErrors("Недостаточно информации для печати", messages);
            return false;
        }
        return true;
    }

}
