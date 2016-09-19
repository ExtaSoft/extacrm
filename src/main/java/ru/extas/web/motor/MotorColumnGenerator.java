package ru.extas.web.motor;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import ru.extas.model.motor.MotorInstance;
import ru.extas.web.commons.GridDataDecl;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * Формируем столбец с описанием техники
 *
 * @author Valery Orlov
 *         Date: 06.08.2014
 *         Time: 18:04
 */
public class MotorColumnGenerator extends GridDataDecl.ComponentColumnGenerator {
    @Override
    public Object generateCell(final Object columnId, final Item item, final Object itemId) {
        final StringBuilder content = new StringBuilder();
        final List<MotorInstance> motorInstances = (List<MotorInstance>) item.getItemProperty("motorInstances").getValue();
        if (!isEmpty(motorInstances)) {
            MotorInstance first = motorInstances.get(0);
            if (first.getType() != null)
                content.append("<strong>").append(first.getType()).append("</strong>");
            if (first.getBrand() != null) {
                if (first.getType() != null)
                    content.append("<br/>");
                content.append(first.getBrand());
            }
            if (first.getModel() != null) {
                if (first.getType() != null && first.getBrand() == null)
                    content.append("<br/>");
                else if (first.getBrand() != null)
                    content.append(" | ");
                content.append(first.getModel());
            }
        }
        return new Label(content.toString(), ContentMode.HTML);
    }

}
