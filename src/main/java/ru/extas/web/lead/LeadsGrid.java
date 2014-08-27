package ru.extas.web.lead;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.bpm.BPStatusForm;
import ru.extas.web.commons.*;
import ru.extas.web.commons.AbstractEditForm;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * <p>LeadsGrid class.</p>
 *
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:24
 * @version $Id: $Id
 * @since 0.3
 */
public class LeadsGrid extends ExtaGrid<Lead> {
	private static final long serialVersionUID = 4876073256421755574L;
	private final static Logger logger = LoggerFactory.getLogger(LeadsGrid.class);
	private final Lead.Status status;

	/**
	 * <p>Constructor for LeadsGrid.</p>
	 *
	 * @param status a {@link ru.extas.model.lead.Lead.Status} object.
	 */
	public LeadsGrid(Lead.Status status) {
		super(Lead.class, false);
		this.status = status;
		initialize();
	}

	/** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new LeadDataDecl();
	}

    @Override
    public AbstractEditForm<Lead> createEditForm(Lead lead) {
        return new LeadEditForm(lead, status == Lead.Status.QUALIFIED);
    }

    /** {@inheritDoc} */
	@Override
	protected void initTable(Mode mode) {
		super.initTable(mode);
		// Покозываем колонку результата в закрытых
		if (status == Lead.Status.CLOSED)
			table.setColumnCollapsed("result", false);
	}

	/** {@inheritDoc} */
	@Override
	protected Container createContainer() {
		// Запрос данных
		final ExtaDataContainer<Lead> container = new SecuredDataContainer<>(Lead.class,
                status == Lead.Status.NEW ? ExtaDomain.LEADS_NEW :
                        status == Lead.Status.QUALIFIED ? ExtaDomain.LEADS_QUAL :
                                ExtaDomain.LEADS_CLOSED);
		container.addContainerFilter(new Compare.Equal("status", status));
		container.sort(new Object[]{"createdAt"}, new boolean[]{false});
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		List<UIAction> actions = newArrayList();

		if (status == Lead.Status.NEW || status == Lead.Status.QUALIFIED) {
			actions.add(new NewObjectAction("Новый", "Ввод нового лида"));
		}

		actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке лид"));

		if (status == Lead.Status.NEW) {
			actions.add(new ItemAction("Квалифицировать", "Квалифицировать лид", Fontello.DOC_NEW) {
				@Override
				public void fire(final Object itemId) {
					final Lead curObj = GridItem.extractBean(table.getItem(itemId));

					final LeadEditForm editWin = new LeadEditForm(curObj, true);
                    editWin.addCloseFormListener(new AbstractEditForm.CloseFormListener() {
                        @Override
                        public void closeForm(AbstractEditForm.CloseFormEvent event) {
							if (editWin.isSaved()) {
								refreshContainerItem(itemId);
							}
						}
					});
                    FormUtils.showModalWin(editWin);
				}
			});
		}

		actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса к которому привязан текущий Лид", Fontello.SITEMAP) {
			@Override
			public void fire(Object itemId) {
				final Lead curObj = extractBean(table.getItem(itemId));

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
                    NotificationUtil.showWarning("Нет бизнес процесса с которым связан текущий Лид.");
				}
			}
		});

		return actions;
	}

}
