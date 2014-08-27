package ru.extas.web.motor;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import ru.extas.web.commons.GridDataDecl;

/**
* @author Valery Orlov
*         Date: 06.08.2014
*         Time: 18:04
*/
public class MotorColumnGenerator extends GridDataDecl.ComponentColumnGenerator {
    @Override
    public Object generateCell(Object columnId, Item item) {
        StringBuilder content = new StringBuilder();
        final Object motorType = item.getItemProperty("motorType").getValue();
        final Object motorBrand = item.getItemProperty("motorBrand").getValue();
        final Object motorModel = item.getItemProperty("motorModel").getValue();
        if(motorType != null)
            content.append("<strong>").append(motorType).append("</strong>");
        if(motorBrand != null) {
            if(motorType != null)
                content.append("<br/>");
            content.append(motorBrand);
        }
        if(motorModel != null) {
            if(motorType != null && motorBrand == null)
                content.append("<br/>");
            else if(motorBrand != null)
                content.append(" | ");
            content.append(motorModel);
        }
        return new Label(content.toString(), ContentMode.HTML);
    }

}
