package ru.extas.web.lead;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.extas.model.contacts.SalePoint;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.contacts.SalePointEditForm;

import static com.google.common.base.Strings.nullToEmpty;

/**
* @author Valery Orlov
*         Date: 06.08.2014
*         Time: 18:38
*/
public class SalePointColumnGenerator extends GridDataDecl.ComponentColumnGenerator {

    private String salePointPropId;
    private String salePointNamePropId;
    private String regionPropId;

    public SalePointColumnGenerator(String salePointPropId, String salePointNamePropId, String regionPropId) {
        this.salePointPropId = salePointPropId;
        this.salePointNamePropId = salePointNamePropId;
        this.regionPropId = regionPropId;
    }

    @Override
    public Object generateCell(Object columnId, Item item) {
        String region = "";
        Button link = new Button();
        link.addStyleName("link");
        final SalePoint salePoint = (SalePoint) item.getItemProperty(salePointPropId).getValue();
        if (salePoint != null) {
            link.setCaption(salePoint.getName());
            link.addClickListener(event -> {

                final SalePointEditForm editWin = new SalePointEditForm(salePoint);
                FormUtils.showModalWin(editWin);

            });
            region = salePoint.getActualAddress().getRegion();
        } else if (salePointNamePropId != null) {
            region = nullToEmpty((String) item.getItemProperty(regionPropId).getValue());
            link.setCaption(nullToEmpty((String) item.getItemProperty(salePointNamePropId).getValue()));
            link.setEnabled(false);
        }
        VerticalLayout cell = new VerticalLayout();
        cell.addComponent(new Label(region));
        cell.addComponent(link);
        return cell;
    }
}
