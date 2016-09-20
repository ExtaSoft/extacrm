package ru.extas.web.sale;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import ru.extas.model.contacts.Client;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.SaleComment;
import ru.extas.model.sale.Sale_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.web.commons.*;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.commons.window.CloseOnlyWindow;
import ru.extas.web.contacts.ClientColumnGenerator;
import ru.extas.web.contacts.Name2ShortNameConverter;
import ru.extas.web.contacts.company.CompanyEditForm;
import ru.extas.web.contacts.employee.EmployeeColumnGenerator;
import ru.extas.web.lead.SalePointColumnGenerator;
import ru.extas.web.motor.MotorColumnGenerator;

import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.getLast;

/**
 * @author Valery Orlov
 *         Date: 15.10.13
 *         Time: 12:52
 */
class SaleDataDecl extends GridDataDecl {
    /**
     * <p>Constructor for SaleDataDecl.</p>
     *
     * @param domain
     */
    public SaleDataDecl(final ExtaDomain domain) {
        switch (domain) {
            case SALES_OPENED:
                addOpenedMappings();
                break;
            case SALES_SUCCESSFUL:
                addClosedMappings();
                break;
            case SALES_CANCELED:
                addCanceledMappings();
                break;
        }
    }

    private void addNumMapping() {
        addMapping("num", "№", new NumColumnGenerator() {
            @Override
            public void fireClick(final Item item) {
                final Sale curObj = GridItem.extractBean(item);

                final SaleEditForm editWin = new SaleEditForm(curObj);
                FormUtils.showModalWin(editWin);
            }
        }, null);
    }

    private void addClosedMappings() {
        addNumMapping();
        addCreatedDateMapping(false);
        addLastModifiedDateMapping(false, "Дата завершения");
        addSourceMapping(true);
        addClientNameMapping(true);
        addMotorMappings();
        addDealerMappings();
        addRegionCityMappings();
        addLastModifiedByMapping(false, "Кто закрыл");
        addCreatedByMapping(true);
        addManagersMapping();
    }

    private void addCanceledMappings() {
        addNumMapping();
        addCreatedDateMapping(false);
        addLastModifiedDateMapping(false, "Дата отмены");
        addSourceMapping(true);
        addClientNameMapping(true);
        addMotorMappings();
        addDealerMappings();
        addRegionCityMappings();
        addMapping(Sale_.cancelReason.getName(), "Причина отмены", getPresentFlags(true));
        addLastModifiedByMapping(false, "Кто отменил");
        addCreatedByMapping(true);
        addManagersMapping();
    }

    private void addSourceMapping(final boolean isCollapsed) {
        addMapping("source", "Источник лида", getPresentFlags(isCollapsed));
    }

    private void addOpenedMappings() {
        addNumMapping();
        addLastModifiedDateMapping(false);
        addSourceMapping(false);
        addClientNameMapping(false);
        addMotorMappings();
        addRegionCityMappings();
        addDealerMappings();
        addCreatedByMapping(false);
        addMapping("comments", "Коментарии", new ComponentColumnGenerator() {
            final int COMMENT_SIZE = 27;

            @Override
            public Object generateCell(final Object columnId, final Item item, final Object itemId) {
                final Sale sale = GridItem.extractBean(item);
                final SaleComment lastCommentObj = getLast(sale.getComments(), null);
                String lastComment = "";
                if (lastCommentObj != null) {
                    lastComment = lastCommentObj.getText();
                    if (!isNullOrEmpty(lastComment) && lastComment.length() > COMMENT_SIZE + 3)
                        lastComment = lastComment.substring(0, COMMENT_SIZE) + "...";
                }
                final Button link = new Button(lastComment);
                link.addStyleName(ExtaTheme.BUTTON_LINK);
                link.setHtmlContentAllowed(true);
                link.setDescription("Нажмите чтобы просмотреть все коментарии");
                link.addClickListener(event -> {
                    final CommentsField<SaleComment> commentsField = new CommentsField<>(SaleComment.class);
                    commentsField.setPropertyDataSource(new ObjectProperty(sale.getComments(), List.class, true));
                    commentsField.setReadOnly(true);
                    final CloseOnlyWindow win = new CloseOnlyWindow("Комментарии к продаже № " + sale.getNum(), new Panel(commentsField));
                    win.setHeight(350, Sizeable.Unit.PIXELS);
                    win.setWidth(550, Sizeable.Unit.PIXELS);
                    win.addAttachListener(e -> commentsField.setReadOnly(true));
                    win.showModal();
                });
                return link;
            }
        }, null);
        addCreatedDateMapping(true);
        addLastModifiedByMapping(true);
        addManagersMapping();
    }

    private void addManagersMapping() {
        addMapping("responsible.name", "Ответственный", getPresentFlags(true), Name2ShortNameConverter.class);
        addMapping("responsibleAssist.name", "Заместитель", getPresentFlags(true), Name2ShortNameConverter.class);
        addMapping("dealerManager.name", "Представитель дилера", getPresentFlags(true), Name2ShortNameConverter.class);
    }

    private void addDealerMappings() {
        addMapping("dealer.name", "Мотосалон", getPresentFlags(true));
        addMapping("pointOfSale", "Регион | Мотосалон", new SalePointColumnGenerator("dealer", null, "region"), null);
    }

    private void addRegionCityMappings() {
        addMapping("dealer.posAddress.regionWithType", "Регион");
        addMapping("dealer.posAddress.cityWithType", "Город");
    }

    private void addMotorMappings() {
        addMapping("motorInstances", "Техника", new MotorColumnGenerator(), null);
    }

    private void addClientNameMapping(final boolean isCollapsed) {
        addMapping("client.name", "Клиент", new ClientColumnGenerator());
        addMapping("client.phone", "Телефон", getPresentFlags(isCollapsed), PhoneConverter.class);
        addMapping("clientContact.name", "Контактное лицо", new EmployeeColumnGenerator("clientContact"), getPresentFlags(isCollapsed));
        addMapping("clientContact.phone", "Контактный телефон", getPresentFlags(isCollapsed), PhoneConverter.class);
        addMapping("client_company", "Компания клиента", new ComponentColumnGenerator() {

            @Override
            public Object generateCell(final Object columnId, final Item item, final Object itemId) {
                final Client client = (Client) item.getItemProperty(Sale_.client.getName()).getValue();
                if (client != null && client instanceof LegalEntity) {
                    final Button link = new Button();
                    link.addStyleName(ExtaTheme.BUTTON_LINK);
                    final Company company = ((LegalEntity) client).getCompany();
                    if (company != null) {
                        link.setCaption(company.getName());
                        link.addClickListener(event -> {
                            final CompanyEditForm editWin = new CompanyEditForm(company);
                            FormUtils.showModalWin(editWin);
                        });
                        return link;
                    }
                }
                return null;
            }
        });
    }

}
