package ru.extas.web.lead;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.Lead;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 */
public class LeadsGrid extends ExtaGrid {
    private static final long serialVersionUID = 4876073256421755574L;
    private final static Logger logger = LoggerFactory.getLogger(LeadsGrid.class);
    private final Lead.Status status;

    public LeadsGrid(Lead.Status status) {
        super(false);
        this.status = status;
        initialize();
    }

    @Override
    protected GridDataDecl createDataDecl() {
        return new LeadDataDecl();
    }

    @Override
    protected void initTable(Mode mode) {
        super.initTable(mode);
        // Покозываем колонку результата в закрытых
        if (status == Lead.Status.CLOSED)
            table.setColumnCollapsed("result", false);
    }

    @Override
    protected Container createContainer() {
        // Запрос данных
        final JPAContainer<Lead> container = new ExtaDataContainer<>(Lead.class);
        container.addContainerFilter(new Compare.Equal("status", status));
        container.sort(new Object[]{"createdAt"}, new boolean[]{false});
        return container;
    }

    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        if (status == Lead.Status.NEW || status == Lead.Status.QUALIFIED) {
            actions.add(new UIAction("Новый", "Ввод нового лида", "icon-doc-new") {
                @Override
                public void fire(Object itemId) {
                    final BeanItem<Lead> newObj = new BeanItem<>(new Lead());

                    final LeadEditForm editWin = new LeadEditForm("Ввод нового лида в систему", newObj, status == Lead.Status.QUALIFIED);
                    editWin.addCloseListener(new Window.CloseListener() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public void windowClose(final Window.CloseEvent e) {
                            if (editWin.isSaved()) {
                                ((JPAContainer) container).refresh();
                                Notification.show("Лид сохранен", Notification.Type.TRAY_NOTIFICATION);
                            }
                        }
                    });
                    editWin.showModal();
                }
            });
        }

        actions.add(new DefaultAction("Изменить", "Редактировать выделенный в списке лид", "icon-edit-3") {
            @Override
            public void fire(final Object itemId) {
                final BeanItem<Lead> curObj = new BeanItem<>(((EntityItem<Lead>) table.getItem(itemId)).getEntity());

                final LeadEditForm editWin = new LeadEditForm("Редактирование лида", curObj, false);
                editWin.addCloseListener(new Window.CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final Window.CloseEvent e) {
                        if (editWin.isSaved()) {
                            ((JPAContainer) container).refreshItem(itemId);
                            Notification.show("Лид сохранен", Notification.Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });

        if (status == Lead.Status.NEW) {
            actions.add(new ItemAction("Квалифицировать", "Квалифицировать лид", "icon-doc-new") {
                @Override
                public void fire(final Object itemId) {
                    final BeanItem<Lead> curObj = new BeanItem<>(((EntityItem<Lead>) table.getItem(itemId)).getEntity());

                    final LeadEditForm editWin = new LeadEditForm("Квалификация лида", curObj, true);
                    editWin.addCloseListener(new Window.CloseListener() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public void windowClose(final Window.CloseEvent e) {
                            if (editWin.isSaved()) {
                                ((JPAContainer) container).refreshItem(itemId);
                                Notification.show("Лид квалифицирован", Notification.Type.TRAY_NOTIFICATION);
                            }
                        }
                    });
                    editWin.showModal();
                }
            });
        }

	    actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса к которому привязан текущий Лид", "icon-sitemap") {
		    @Override
		    public void fire(Object itemId) {
			    final Lead curObj = ((EntityItem<Lead>) table.getItem(itemId)).getEntity();

			    // Ищем процесс к которому привязана текущая продажа
			    RuntimeService runtimeService = lookup(RuntimeService.class);
			    ProcessInstance process =
					    runtimeService.createProcessInstanceQuery()
							    .includeProcessVariables()
							    .variableValueEquals("lead", curObj)
							    .singleResult();

			    if (process != null) {
				    // Показать статус выполнения процесса
				    BPStatusForm statusForm = new BPStatusForm(process.getProcessInstanceId());
				    statusForm.showModal();
			    } else {
				    Notification.show("Нет бизнес процесса с которым связан текущий Лид.");
			    }
		    }
	    });

	    return actions;
    }

}
