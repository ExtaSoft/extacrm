/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.Person_;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.insurance.A7Form;
import ru.extas.model.insurance.A7Form_;
import ru.extas.model.insurance.FormTransfer;
import ru.extas.model.insurance.FormTransfer_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.model.security.SecureTarget;
import ru.extas.web.commons.*;
import ru.extas.web.commons.ExtaEditForm;

import javax.persistence.criteria.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * <p>FormTransferGrid class.</p>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class FormTransferGrid extends ExtaGrid<FormTransfer> {

    private static final long serialVersionUID = 1170175803163742829L;
    private final static Logger logger = LoggerFactory.getLogger(FormTransferGrid.class);

    /**
     * <p>Constructor for FormTransferGrid.</p>
     */
    public FormTransferGrid() {
        super(FormTransfer.class);
    }

    @Override
    public ExtaEditForm<FormTransfer> createEditForm(FormTransfer formTransfer) {
        return new FormTransferEditForm(formTransfer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected GridDataDecl createDataDecl() {
        return new FormTransferDataDecl();
    }

    private class FormTransferSecuredContainer extends AbstractSecuredDataContainer<FormTransfer> {

        public FormTransferSecuredContainer(Class<FormTransfer> entityClass, ExtaDomain domain) {
            super(entityClass, domain);
        }

        @Override
        protected Predicate createPredicate4Target(CriteriaBuilder cb, CriteriaQuery<?> cq, SecureTarget target) {
            Predicate predicate = null;
            final Root<FormTransfer> objectRoot = (Root<FormTransfer>) getFirst(cq.getRoots(), null);
            final Person curUserContact = securityService.getCurrentUserContact();

            switch (target) {
                case OWNONLY:
                    predicate = cb.or(
                            cb.equal(objectRoot.get(FormTransfer_.fromContact), curUserContact),
                            cb.equal(objectRoot.get(FormTransfer_.toContact), curUserContact));
                    break;
                case SALE_POINT: {
                    Set<SalePoint> workPlaces = null;
                    workPlaces = curUserContact.getWorkPlaces();
                    if (!isEmpty(workPlaces)) {
                        SetJoin<Person, SalePoint> workPlaceRootF = objectRoot.join(FormTransfer_.fromContact, JoinType.LEFT).join(Person_.workPlaces, JoinType.LEFT);
                        SetJoin<Person, SalePoint> workPlaceRootT = objectRoot.join(FormTransfer_.toContact, JoinType.LEFT).join(Person_.workPlaces, JoinType.LEFT);
                        predicate = cb.or(workPlaceRootF.in(workPlaces), workPlaceRootT.in(workPlaces));
                    }
                    break;
                }
                case CORPORATE: {
                    Set<SalePoint> workPlaces = null;
                    final Set<Company> companies = curUserContact.getEmployers();
                    for (final Company company : companies) {
                        workPlaces.addAll(company.getSalePoints());
                    }
                    if (!isEmpty(workPlaces)) {
                        SetJoin<Person, SalePoint> workPlaceRootF = objectRoot.join(FormTransfer_.fromContact, JoinType.LEFT).join(Person_.workPlaces, JoinType.LEFT);
                        SetJoin<Person, SalePoint> workPlaceRootT = objectRoot.join(FormTransfer_.toContact, JoinType.LEFT).join(Person_.workPlaces, JoinType.LEFT);
                        predicate = cb.or(workPlaceRootF.in(workPlaces), workPlaceRootT.in(workPlaces));
                    }
                    break;
                }
                case ALL:
                    break;
            }

            return predicate;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Container createContainer() {
        // Запрос данных
        final JPAContainer<FormTransfer> container = new FormTransferSecuredContainer(FormTransfer.class, ExtaDomain.INSURANCE_TRANSFER);
        container.addNestedContainerProperty("fromContact.name");
        container.addNestedContainerProperty("toContact.name");

        return container;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UIAction> createActions() {
        List<UIAction> actions = newArrayList();

        actions.add(new NewObjectAction("Новый", "Ввод нового акта приема/передачи"));
        actions.add(new EditObjectAction("Изменить", "Редактировать выделенный в списке акта приема/передачи"));

        actions.add(new ItemAction("Печать", "Создать печатное представление акта приема передачи квитанций", Fontello.PRINT_2) {
            @Override
            public void fire(Object itemId) {
                printFormTransfer(itemId);
            }
        });
        return actions;
    }

    private void printFormTransfer(final Object itemId) {
//		final FormTransfer formTransfer = extractBean(table.getItem(itemId));
//		checkNotNull(formTransfer, "Нечего печатать", "Нет выбранной записи.");
//
//		if (!canPrintForm(formTransfer))
//			return;
//
//		try {
//			// 1) Load Docx file by filling Velocity template engine and
//			// cache
//			// it to the registry
//			final InputStream in = getClass().getResourceAsStream("/reports/insurance/FormTransferTemplate.docx");
//			final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
//					TemplateEngineKind.Freemarker);
//
//			// 2) Create context Java model
//			// Продготовка параметров отчета:
//			// Передающеее физ. лицо
//			final Person fromContact = formTransfer.getFromContact();
//			// Передающее юр. лицо
//			final Company fromCompany = fromContact.getAffiliation();
//			// Принимающее физ. лицо
//			final Person toContact = formTransfer.getToContact();
//			// Колличество форм прописью
//			final String formNumsStr = ValueUtil.spellOutThing(formTransfer.getFormNums().size());
//			final IContext context = report.createContext();
//			context.put("formTransfer", formTransfer);
//			context.put("fromCompany", fromCompany);
//			context.put("fromContact", fromContact);
//			context.put("toContact", toContact);
//			context.put("formNumsStr", formNumsStr);
//
//			// 3) Generate report by merging Java model with the Docx
//			final ByteArrayOutputStream outDoc = new ByteArrayOutputStream();
//			report.process(context, outDoc);
//
//			new DownloadFileWindow(
//					outDoc.toByteArray(),
//					MessageFormat.format("Акт.приема.передачи.А7.{0}.docx",
//							formTransfer.getToContact().getName()))
//					.showModal();
//
//		} catch (IOException | XDocReportException e) {
//			logger.error("Print Form Transfer error", e);
//			throw Throwables.propagate(e);
//		}
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
