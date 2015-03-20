package ru.extas.web.analytics;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.Interval;
import ru.extas.model.sale.Sale;
import ru.extas.model.sale.Sale_;
import ru.extas.model.security.ExtaDomain;
import ru.extas.security.SecurityFilter;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.component.ExtaFormLayout;
import ru.extas.web.commons.component.FormGroupHeader;
import ru.extas.web.commons.component.PastDateIntervalField;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 20.03.2015
 *         Time: 20:25
 */
public abstract class AbstractSalesChart extends VerticalLayout {
    private PastDateIntervalField intervalField;

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
        Predicate userPredicat = cq.getRestriction();
        if (userPredicat != null)
            predicateList.add(userPredicat);

        // Аналитический фильтр
        // - Временной интервал
        Interval interval = intervalField.getValue();
        if (interval != null) {
            predicateList.add(cb.between(root.get(Sale_.createdDate), interval.getStart(), interval.getEnd()));
        }

        // Фильтр безопасности
        Predicate securityPredicat = new SecurityFilter<>(Sale.class, ExtaDomain.SALES_OPENED).createSecurityPredicate(cb, cq);
        if (securityPredicat != null)
            predicateList.add(securityPredicat);

        // Накладываем фильтр, если есть
        if (!predicateList.isEmpty())
            cq.where(cb.and(predicateList.toArray(new Predicate[predicateList.size()])));
    }

    protected ExtaFormLayout createFilterForm() {
        ExtaFormLayout filterForm = new ExtaFormLayout();
        filterForm.addComponent(new FormGroupHeader("Фильтр данных"));
        intervalField = new PastDateIntervalField("Интервал анализа",
                "Установите временной интервал за который будет производится анализ данных");
        filterForm.addComponent(intervalField);

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
