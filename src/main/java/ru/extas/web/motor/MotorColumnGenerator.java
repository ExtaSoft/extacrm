package ru.extas.web.motor;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import ru.extas.model.lead.Lead;
import ru.extas.web.commons.GridDataDecl;

import java.text.MessageFormat;

import static ru.extas.web.commons.GridItem.extractBean;

/**
* @author Valery Orlov
*         Date: 06.08.2014
*         Time: 18:04
*/
public class MotorColumnGenerator extends GridDataDecl.ComponentColumnGenerator {
    @Override
    public Object generateCell(Object columnId, Item item) {
        return new Label(
                MessageFormat.format("<strong>{0}</strong><br/>{1} | {2}",
                        item.getItemProperty("motorType").getValue(),
                        item.getItemProperty("motorBrand").getValue(),
                        item.getItemProperty("motorModel").getValue()),
                ContentMode.HTML);
    }

}
