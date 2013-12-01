/**
 *
 */
package ru.extas.web.insurance;

import com.google.common.base.Throwables;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
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
import ru.extas.model.Insurance;
import ru.extas.model.UserRole;
import ru.extas.server.UserManagementService;
import ru.extas.web.commons.*;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

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
        final JPAContainer<Insurance> container = new ExtaDataContainer<>(Insurance.class);
        container.addNestedContainerProperty("client.name");
        container.addNestedContainerProperty("client.birthday");
        container.addNestedContainerProperty("client.cellPhone");
        UserManagementService userService = lookup(UserManagementService.class);
        // пользователю доступны только собственные записи
        if (userService.isCurUserHasRole(UserRole.USER)) {
            container.addContainerFilter(new Compare.Equal("createdBy", userService.getCurrentUserLogin()));
        }
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
                            ((JPAContainer) container).refresh();
                            Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке полис страхования", "icon-edit-3") {
            @Override
            public void fire(final Object itemId) {
                final BeanItem<Insurance> curObj = new BeanItem<>(((EntityItem<Insurance>) table.getItem(itemId)).getEntity());

                final InsuranceEditForm editWin = new InsuranceEditForm("Редактировать полис", curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refreshItem(itemId);
                            Notification.show("Полис сохранен", Type.TRAY_NOTIFICATION);
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
        final Object curObjId = itemId;
        final EntityItem<Insurance> curObj = (EntityItem<Insurance>) table.getItem(curObjId);
        final Insurance insurance = curObj.getEntity();
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
            context.put("periodOfCover",
                    insurance.getCoverTime() == null || insurance.getCoverTime() == Insurance.PeriodOfCover.YEAR
                            ? "12 месяцев" : "6 месяцев");

            // 3) Generate report by merging Java model with the Docx
            final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
            report.process(context, outDoc);

            final String clientName = insurance.getClient().getName();
            final String policyNum = insurance.getRegNum();
            final String policyFileName = MessageFormat.format("Полис {0} {1}.docx", policyNum, clientName);

            new DownloadFileWindow(outDoc.toByteArray(), policyFileName).showModal();

        } catch (IOException | XDocReportException e) {
            logger.error("Print policy error", e);
            throw Throwables.propagate(e);
        }
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


}
