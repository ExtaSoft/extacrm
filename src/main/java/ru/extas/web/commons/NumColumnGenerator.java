package ru.extas.web.commons;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.extas.web.commons.converters.StringToJodaDTConverter;

import static ru.extas.server.ServiceLocator.lookup;
import static ru.extas.web.commons.GridDataDecl.ComponentColumnGenerator;

/**
* @author Valery Orlov
*         Date: 06.08.2014
*         Time: 17:48
*/
public abstract class NumColumnGenerator extends ComponentColumnGenerator {

    private final StringToJodaDTConverter dtConverter;

    private final String numProperty;

    public NumColumnGenerator(final String numProperty) {
        this.numProperty = numProperty;
        dtConverter = lookup(StringToJodaDTConverter.class);
        dtConverter.setPattern("EEE, dd MMM, HH:mm");
    }

    public NumColumnGenerator() {
        this("num");
    }

    @Override
    public Object generateCell(final Object columnId, final Item item, final Object itemId) {
        final Property numProp = item.getItemProperty(numProperty);
        final VerticalLayout cell = new VerticalLayout();
        final Button link = new Button(String.valueOf(numProp.getValue()));
        link.addClickListener(event -> fireClick(item));
        link.addStyleName(ExtaTheme.BUTTON_LINK);
        cell.addComponent(link);
        final Label createdAt = new Label(item.getItemProperty("createdAt"));
        createdAt.setConverter(dtConverter);
        cell.addComponent(createdAt);
        return cell;
    }

    public abstract void fireClick(final Item item);
}
