package ru.extas.web.lead;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.lead.Lead;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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
	public LeadsGrid(final Lead.Status status) {
		super(Lead.class);
		this.status = status;
	}

    public Lead.Status getStatus() {
        return status;
    }

    /** {@inheritDoc} */
	@Override
	protected GridDataDecl createDataDecl() {
		return new LeadDataDecl(this);
	}

    @Override
    public ExtaEditForm<Lead> createEditForm(final Lead lead, final boolean isInsert) {
        final LeadEditForm editForm = new LeadEditForm(lead, isInsert && status == Lead.Status.QUALIFIED);
        editForm.setReadOnly(status != Lead.Status.NEW && !isInsert);
        return editForm;
    }

    /** {@inheritDoc} */
	@Override
	protected void initTable(final Mode mode) {
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
		container.addNestedContainerProperty("responsible.name");
		container.addContainerFilter(new Compare.Equal("status", status));
		container.sort(new Object[]{"createdDate"}, new boolean[]{false});
		return container;
	}

	/** {@inheritDoc} */
	@Override
	protected List<UIAction> createActions() {
		final List<UIAction> actions = newArrayList();

		if (status == Lead.Status.NEW || status == Lead.Status.QUALIFIED) {
			actions.add(new NewObjectAction("Новый", "Ввод нового лида"));
		}

		actions.add(new EditObjectAction(status == Lead.Status.NEW ? "Изменить" : "Просмотреть", "Редактировать выделенный в списке лид"));

		if (status == Lead.Status.NEW) {
			actions.add(new ItemAction("Квалифицировать", "Квалифицировать лид", Fontello.CHECK_2) {
				@Override
				public void fire(final Object itemId) {
                    doQualifyLead(itemId);
				}
			});
		}

//		actions.add(new ItemAction("Статус БП", "Показать панель статуса бизнес процесса к которому привязан текущий Лид", Fontello.SITEMAP) {
//			@Override
//			public void fire(Object itemId) {
//				final Lead curObj = extractBean(table.getItem(itemId));
//
//				// Ищем процесс к которому привязана текущая продажа
//				RuntimeService runtimeService = lookup(RuntimeService.class);
//				ProcessInstance process =
//						runtimeService.createProcessInstanceQuery()
//								.includeProcessVariables()
//								.variableValueEquals("lead", curObj)
//								.singleResult();
//
//				if (process != null) {
//					// Показать статус выполнения процесса
//					BPStatusForm statusForm = new BPStatusForm(process.getProcessInstanceId());
//					statusForm.showModal();
//				} else {
//                    NotificationUtil.showWarning("Нет бизнес процесса с которым связан текущий Лид.");
//				}
//			}
//		});

		return actions;
	}

    public void doQualifyLead(final Object itemId) {
        final Lead curObj = GridItem.extractBean(table.getItem(itemId));

        final LeadEditForm editWin = new LeadEditForm(curObj, true);
        editWin.addCloseFormListener(event -> {
            if (editWin.isSaved()) {
                refreshContainerItem(itemId);
            }
        });
        FormUtils.showModalWin(editWin);
    }

}
