package ru.extas.web.commons.component.slider;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Slider;

/**
 * Дополняет функционал стандартного слайдера
 *
 * @author Valery Orlov
 *         Date: 08.12.2014
 *         Time: 18:42
 */
public class SliderExtension extends AbstractExtension {

    public final void extend(final Slider target) {
        super.extend(target);
        target.addStyleName("ea-extended-slider");
    }
}
