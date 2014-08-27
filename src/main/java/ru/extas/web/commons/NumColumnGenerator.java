package ru.extas.web.commons;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.extas.web.commons.converters.StringToJodaDTConverter;

import static ru.extas.web.commons.GridDataDecl.*;

/**
* @author Valery Orlov
*         Date: 06.08.2014
*         Time: 17:48
*/
public abstract class NumColumnGenerator extends ComponentColumnGenerator {

    private final StringToJodaDTConverter dtConverter = new StringToJodaDTConverter("EEE, dd MMM, HH:mm");

    private final String numProperty;

    public NumColumnGenerator(final String numProperty) {
        this.numProperty = numProperty;
    }

    public NumColumnGenerator() {
        this("num");
    }

    @Override
    public Object generateCell(Object columnId, final Item item) {
        Property numProp = item.getItemProperty(numProperty);
        VerticalLayout cell = new VerticalLayout();
        Button link = new Button(String.valueOf(numProp.getValue()));
        link.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireClick(item);
            }
        });
        link.addStyleName("link");
        cell.addComponent(link);
        //cell.setComponentAlignment(link, Alignment.TOP_RIGHT);
        final Label createdAt = new Label(item.getItemProperty("createdAt"));
        createdAt.setConverter(dtConverter);
        cell.addComponent(createdAt);
        //cell.setComponentAlignment(createdAt, Alignment.BOTTOM_RIGHT);
        return cell;
    }

    public abstract void fireClick(final Item item);
}
