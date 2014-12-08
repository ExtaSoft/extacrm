package ru.extas.web.commons.component.slider;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VSlider;
import com.vaadin.shared.ui.Connect;

/**
 * @author Valery Orlov
 *         Date: 08.12.2014
 *         Time: 18:43
 */
@Connect(SliderExtension.class)
public class SliderExtensionConnector extends AbstractExtensionConnector {

    private Label indicator;
    private VSlider sliderWidget;

    @Override
    protected void extend(final ServerConnector target) {
        // Get the extended widget
        sliderWidget = (VSlider) ((ComponentConnector) target).getWidget();

        indicator = new Label();
        indicator.addStyleName("indicator");

        final Element base = (Element) sliderWidget.getElement().getChild(2);
        final Element handle = base.getFirstChildElement();
        handle.appendChild(indicator.getElement());

        Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(final Event.NativePreviewEvent event) {
                updateIndicator();
            }
        });

        sliderWidget.addValueChangeHandler(new ValueChangeHandler<Double>() {
            @Override
            public void onValueChange(final ValueChangeEvent<Double> event) {
                updateIndicator();
            }
        });
    }

    private void updateIndicator() {
        final double value = sliderWidget.getValue();
        indicator.setHeight(value + "px");
        indicator.setWidth(value + "px");
    }

}
