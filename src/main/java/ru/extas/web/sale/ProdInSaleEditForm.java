package ru.extas.web.sale;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import ru.extas.model.sale.*;
import ru.extas.web.commons.ExtaEditForm;
import ru.extas.web.commons.NotificationUtil;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.product.ProductSelect;

/**
 * Панель ввода/редактирования кредитного продукта
 *
 * @author Valery Orlov
 *         Date: 20.01.14
 *         Time: 18:27
 * @version $Id: $Id
 * @since 0.3
 */
public class ProdInSaleEditForm extends ExtaEditForm<ProductInSale> {

    // Компоненты редактирования
    @PropertyId("product")
    private ProductSelect productField;
    @PropertyId("summ")
    private EditField summField;
    @PropertyId("downpayment")
    private EditField downpaymentField;
    @PropertyId("period")
    private EditField periodField;

    /**
     * <p>Constructor for ProdInSaleEditForm.</p>
     *
     * @param caption a {@link java.lang.String} object.
     * @param obj     a {@link com.vaadin.data.util.BeanItem} object.
     */
    public ProdInSaleEditForm(final String caption, final ProductInSale obj) {
        super(caption, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initObject(final ProductInSale obj) {
        if (obj.isNew()) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ProductInSale saveObject(final ProductInSale obj) {
        NotificationUtil.showSuccess("Продукт сохранен в продаже");
        return obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ComponentContainer createEditFields(final ProductInSale obj) {
        final FormLayout form = new ExtaFormLayout();
        form.setSizeFull();

        productField = new ProductSelect("Название продукта", "Введите название продукта", obj.getProduct());
        productField.setWidth(100, Unit.PERCENTAGE);
        productField.setRequired(true);
        productField.addValueChangeListener(e -> {
            Product prod = (Product) e.getProperty().getValue();
            setSummCaption(prod);
        });
        form.addComponent(productField);

        summField = new EditField("Сумма", "Введите сумму по продукту");
        summField.setRequired(true);
        form.addComponent(summField);
        setSummCaption(obj.getProduct());

        downpaymentField = new EditField("Первоначальный взнос", "Введите сумму первоначального взноса по продукту");
        //downpaymentField.setRequired(true);
        form.addComponent(downpaymentField);

        periodField = new EditField("Срок", "Введите период действия продукта");
        periodField.setRequired(true);
        form.addComponent(periodField);

        return form;
    }

    private void setSummCaption(Product prod) {
        if (prod != null) {
            if (prod instanceof ProdCredit)
                summField.setCaption("Сумма кредита");
            else if (prod instanceof ProdInsurance)
                summField.setCaption("Страховая сумма");
            else if (prod instanceof ProdInstallments)
                summField.setCaption("Сумма рассрочки");
        }
    }


}
