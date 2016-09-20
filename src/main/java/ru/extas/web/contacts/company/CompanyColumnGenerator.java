package ru.extas.web.contacts.company;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import ru.extas.model.contacts.Company;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.FormUtils;
import ru.extas.web.commons.GridDataDecl;

/**
 * Created by valery on 15.09.16.
 */
public class CompanyColumnGenerator extends GridDataDecl.ComponentColumnGenerator {
    private final String companyPropId;

    public CompanyColumnGenerator(final String companyPropId) {
        this.companyPropId = companyPropId;
    }

    public CompanyColumnGenerator() {
        this("company");
    }

    @Override
    public Object generateCell(final Object columnId, final Item item, final Object itemId) {
        final Company company = (Company) item.getItemProperty(companyPropId).getValue();
        final Button link = new Button();
        link.addStyleName(ExtaTheme.BUTTON_LINK);
        if (company != null) {
            link.setCaption(company.getName());
            link.addClickListener(event -> {
                final CompanyEditForm editWin = new CompanyEditForm(company);
//                    editWin.setReadOnly(true);
                FormUtils.showModalWin(editWin);
            });
            return link;
        } else {
            return null;
        }
    }
}
