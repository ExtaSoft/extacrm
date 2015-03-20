package ru.extas.web.analytics;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.Interval;
import ru.extas.model.contacts.Company;
import ru.extas.model.contacts.SalePoint;
import ru.extas.model.contacts.SalePoint_;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.security.SecurityFilter;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.PastDateIntervalField;
import ru.extas.web.contacts.company.CompanyField;
import ru.extas.web.contacts.salepoint.SalePointField;
import ru.extas.web.reference.RegionSelect;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 20.03.2015
 *         Time: 20:25
 */
public abstract class AbstractSalesChart extends VerticalLayout {
    private PastDateIntervalField intervalField;
    private RegionSelect regionSelect;
    private CompanyField companyField;
    private SalePointField salePointField;

    /**
     * Constructs an empty VerticalLayout.
     */
    public AbstractSalesChart() {
        // Форма фильтра
        ExtaFormLayout filterForm = createFilterForm();
        addComponent(filterForm);
        addChartContent();
        updateChartData();
    }

    protected abstract void addChartContent();

    protected abstract void updateChartData();

    protected void applyFilters(CriteriaBuilder cb, CriteriaQuery<Tuple> cq, Root<Sale> root) {
        List<Predicate> predicateList = newArrayList();

        // Уже наложенный фильтр
        Predicate userPredicate = cq.getRestriction();
        if (userPredicate != null)
            predicateList.add(userPredicate);

        // Аналитический фильтр
        createAnalyticFilter(cb, root, predicateList);

        // Фильтр безопасности
        Predicate securityPredicate = new SecurityFilter<>(Sale.class, ExtaDomain.SALES_OPENED).createSecurityPredicate(cb, cq);
        if (securityPredicate != null)
            predicateList.add(securityPredicate);

        // Накладываем фильтр, если есть
        if (!predicateList.isEmpty())
            cq.where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
    }

    private void createAnalyticFilter(CriteriaBuilder cb, Root<Sale> root, List<Predicate> predicateList) {
        // - Временной интервал
        Interval interval = intervalField.getValue();
        if (interval != null) {
            predicateList.add(cb.between(root.get(Sale_.createdDate), interval.getStart(), interval.getEnd()));
        }
        // - Регион
        String region = (String) regionSelect.getValue();
        if (region != null) {
            predicateList.add(cb.equal(root.get(Sale_.region), region));
        }
        // - Компания
        Company company = companyField.getValue();
        if (company != null) {
            Join<SalePoint, Company> companyPath = root.join(Sale_.dealer).join(SalePoint_.company);
            predicateList.add(cb.equal(companyPath, company));
        }
        // Торговая точка
        SalePoint salePoint = salePointField.getValue();
        if(salePoint != null) {
            predicateList.add(cb.equal(root.get(Sale_.dealer), salePoint));
        }
    }

    protected ExtaFormLayout createFilterForm() {
        ExtaFormLayout filterForm = new ExtaFormLayout();
        filterForm.addComponent(new FormGroupHeader("Фильтр данных"));
        intervalField = new PastDateIntervalField("Временной интервал",
                "Установите временной интервал за который будет производится анализ данных");
        filterForm.addComponent(intervalField);

        // Указанному региону
        regionSelect = new RegionSelect("Регион");
        filterForm.addComponent(regionSelect);

        // Указанной Компании
        companyField = new CompanyField("Компания");
        filterForm.addComponent(companyField);

        // Указанной ТТ внутри региона;
        salePointField = new SalePointField("Торговая точка",
                "Укажите торговую точку для которой будет строиться аналитика");
        filterForm.addComponent(salePointField);

        final Button button = new Button("Применить", e -> {
            updateChartData();
        });
        button.addStyleName(ExtaTheme.BUTTON_SMALL);
        button.addStyleName(ExtaTheme.BUTTON_PRIMARY);
        HorizontalLayout runFilter = new HorizontalLayout(button);
        runFilter.setMargin(new MarginInfo(true, false, false, false));
        filterForm.addComponent(runFilter);

        return filterForm;
    }
}
