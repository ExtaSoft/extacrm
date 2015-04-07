package ru.extas.web.sale;

import com.vaadin.ui.OptionGroup;
import org.vaadin.viritin.fields.EnumSelect;
import ru.extas.model.sale.Sale;
import ru.extas.web.util.ComponentUtil;

/**
 * Выбор причины отмены продажи
 * <p>
 * Created by valery on 06.04.15.
 */
public class CancelReasonSelect extends EnumSelect<Sale.CancelReason> {

    public CancelReasonSelect() {
        super("Причина отмены продажи");
        withSelectType(OptionGroup.class);
        setWidthUndefined();
        setCaptionGenerator(ComponentUtil.getEnumCaptionGenerator(Sale.CancelReason.class));
        setOptions(Sale.CancelReason.values());
    }
}
