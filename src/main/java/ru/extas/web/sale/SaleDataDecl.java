package ru.extas.web.sale;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.SaleComment;
import ru.extas.model.sale.Sale_;
import ru.extas.web.commons.*;
import ru.extas.web.commons.converters.PhoneConverter;
import ru.extas.web.commons.window.CloseOnlyWindow;
import ru.extas.web.contacts.Name2ShortNameConverter;
import ru.extas.web.lead.SalePointColumnGenerator;
import ru.extas.web.motor.MotorColumnGenerator;

import java.util.EnumSet;
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
     */
    public SaleDataDecl() {
        addMapping("num", "№", new NumColumnGenerator() {
            @Override
            public void fireClick(final Item item) {
                final Sale curObj = GridItem.extractBean(item);

                final SaleEditForm editWin = new SaleEditForm(curObj);
                FormUtils.showModalWin(editWin);
            }
        }, null);
        addMapping("client.name", "Клиент");
        addMapping("client.phone", "Телефон", PhoneConverter.class);
        addMapping("motor_all", "Техника", new MotorColumnGenerator(), null);
        addMapping("motorType", "Тип техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorBrand", "Марка техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorModel", "Модель техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("motorPrice", "Стоимость техники", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("dealer.name", "Мотосалон", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("pointOfSale", "Регион | Мотосалон", new SalePointColumnGenerator("dealer", null, "region"), null);
        addMapping("region", "Регион", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping(Sale_.cancelReason.getName(), "Причина отмены", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED));
        addMapping("responsible.name", "Ответственный", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), Name2ShortNameConverter.class);
        addMapping("responsibleAssist.name", "Заместитель", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), Name2ShortNameConverter.class);
        addMapping("dealerManager.name", "Представитель дилера", EnumSet.of(DataDeclMapping.PresentFlag.COLLAPSED), Name2ShortNameConverter.class);
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
        super.addDefaultMappings();
    }
}
