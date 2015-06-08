package ru.extas.web.commons.component;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import ru.extas.web.commons.ExtaTheme;

import java.text.MessageFormat;
import java.util.Locale;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Компонент выбора временного интервала в прошлом
 *
 * @author Valery Orlov
 *         Date: 20.03.2015
 *         Time: 12:53
 */
public class PastDateIntervalField extends ExtaCustomField<Interval> {

    public static final String DATES_DELIMITER = FontAwesome.MINUS.getHtml();

    private InlineDateField startDateField;
    private InlineDateField endDateField;
    private ComboBox namedIntervalField;
    private Label datesLabel;

    private PopupIntervalContent entityContent;
    private PopupView popupView;

    /**
     * Именованные интервалы
     */
    public enum NamedInterval {
        /**
         * С начала недели.
         * Задает временной интервал начинаяя с начала текущей недели и заканчивая сегодняшним днем.
         */
        THIS_WEEK("С начала недели",
                "Задает временной интервал начинаяя с начала текущей недели и заканчивая сегодняшним днем"),
        /**
         * Прошлая календарная неделя.
         * Задает временной интервал с понедельника по воскресенье прошлой недели.
         */
        LAST_WEEK("Прошлая календарная неделя",
                "Задает временной интервал с понедельника по воскресенье прошлой недели"),
        /**
         * Последняя неделя.
         * Задает временной интервал в 7 дней который заканчивается текущей датой.
         */
        WEEK_AGO("Последняя неделя",
                "Задает временной интервал в 7 дней который заканчивается текущей датой"),
        /**
         * С начала месяца.
         * Задает временной интервал с первого числа текущего месяца по сегодняшний днь.
         */
        THIS_MONTH("С начала месяца",
                "Задает временной интервал с первого числа текущего месяца по сегодняшний днь"),
        /**
         * Прошлый календарный месяц.
         * Определяет временной интервал с первого по последнее число прошлого календарного месяца.
         */
        LAST_MONTH("Прошлый календарный месяц",
                "Определяет временной интервал с первого по последнее число прошлого календарного месяца"),
        /**
         * Последний месяц.
         * Определяет временной интервал в один месяц который заканчивается текущей датой.
         */
        MONTH_AGO("Последний месяц",
                "Определяет временной интервал в один месяц который заканчивается текущей датой"),
        /**
         * Последние 3 месяца.
         * Определяет временной интервал в 3 месяца который заканчивается текущей датой.
         */
        THREE_MONTH_AGO("Последние 3 месяца",
                "Определяет временной интервал в 3 месяца который заканчивается текущей датой"),
        /**
         * Последние полгода.
         * Определяет временной интервал в полгода который заканчивается текущей датой.
         */
        HALF_YEAR_AGO("Последние полгода",
                "Определяет временной интервал в полгода который заканчивается текущей датой"),
        /**
         * С начала года.
         * Определяет временной интервал с первого января текущего года по текущую дату
         */
        THIS_YEAR("С начала года",
                "Определяет временной интервал с первого января текущего года по текущую дату"),
        /**
         * Прошлый календарный год.
         * Определяет временной интервал в весь прошлый календарный год.
         */
        LAST_YEAR("Прошлый календарный год",
                "Определяет временной интервал в весь прошлый календарный год"),
        /**
         * Последний год.
         * Определяет временной интервал в один год который заканчивается текущей датой.
         */
        YEAR_AGO("Последний год",
                "Определяет временной интервал в один год который заканчивается текущей датой"),
        /**
         * Свободный интервал.
         * Позволяет выбрать временной интервал указав начальную и конечную дату
         */
        CUSTOM_INTERVAL("Мой интервал",
                "Позволяет выбрать временной интервал указав начальную и конечную дату"),
        /**
         * Все время. Временной интервал не определен.
         */
        ALL_TIME("Все время",
                "Временной интервал не определен");

        private final String caption;
        private final String description;

        NamedInterval(final String caption, final String description) {
            this.caption = caption;
            this.description = description;
        }

        public String getCaption() {
            return caption;
        }

        public String getDescription() {
            return description;
        }
    }

    public PastDateIntervalField(final String caption, final String description) {
        super(caption, description);
        addValueChangeListener(e ->
                updateDates((Interval) e.getProperty().getValue()));
    }

    private void updateDates(final Interval interval) {
        if (interval == null) {
            if (namedIntervalField != null)
                namedIntervalField.setValue(NamedInterval.ALL_TIME);
        } else {
            if (startDateField != null)
                startDateField.setConvertedValue(interval.getStart().toLocalDate());
            if (endDateField != null)
                endDateField.setConvertedValue(interval.getEnd().toLocalDate());
        }
        if (datesLabel != null)
            datesLabel.setValue(getIntervalLabel(interval));
    }

    /**
     * Create the content component or layout for the field. Subclasses of
     * {@link com.vaadin.ui.CustomField} should implement this method.
     * <p>
     * Note that this method is called when the CustomField is attached to a
     * layout or when {@link #getContent()} is called explicitly for the first
     * time. It is only called once for a {@link com.vaadin.ui.CustomField}.
     *
     * @return {@link com.vaadin.ui.Component} representing the UI of the CustomField
     */
    @Override
    protected Component initContent() {
        entityContent = new PopupIntervalContent();
        popupView = new PopupView(entityContent);
        popupView.setHideOnMouseOut(false);
        return popupView;
    }

    private class PopupIntervalContent implements PopupView.Content {

        @Override
        public String getMinimizedValueAsHTML() {
            return getIntervalLabel(getValue());
        }

        @Override
        public Component getPopupComponent() {
            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            final Label popupCaption = new Label("Выбор временного интервала");
            popupCaption.addStyleName(ExtaTheme.LABEL_H3);
            popupCaption.addStyleName(ExtaTheme.LABEL_COLORED);
            layout.addComponent(popupCaption);

            final HorizontalLayout namedLayout = new HorizontalLayout();
            namedLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            namedLayout.setSpacing(true);

            namedIntervalField = new ComboBox();
            namedIntervalField.setWidth(31, Unit.EX);
            namedIntervalField.setPageLength(15);
            namedIntervalField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
            namedIntervalField.setFilteringMode(FilteringMode.OFF);
            namedIntervalField.setNewItemsAllowed(false);
            namedIntervalField.setTextInputAllowed(false);
            namedIntervalField.setInvalidAllowed(false);
            namedIntervalField.setNullSelectionAllowed(false);
            namedIntervalField.addStyleName(ExtaTheme.COMBOBOX_SMALL);
            for (final NamedInterval intervalName : NamedInterval.values()) {
                final Item intervalItem = namedIntervalField.addItem(intervalName);
                final String intervalNameCaption = intervalName.getCaption();
                namedIntervalField.setItemCaption(intervalName, intervalNameCaption);
            }
            namedIntervalField.addValueChangeListener(e -> {
                final NamedInterval namedInterval = (NamedInterval) namedIntervalField.getValue();
                final Interval interval = getIntervalByName(namedInterval);
                if (interval != null) {
                    setValue(interval);
                } else if (namedInterval == NamedInterval.ALL_TIME) {
                    setValue(null);
                }
            });
            namedLayout.addComponent(namedIntervalField);

            datesLabel = new Label(DATES_DELIMITER, ContentMode.HTML);
            namedLayout.addComponent(datesLabel);

            final HorizontalLayout datesLayout = new HorizontalLayout();
            datesLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            final ValueChangeListener dateListener = e -> {
                final LocalDate startDate = (LocalDate) startDateField.getConvertedValue();
                final LocalDate endDate = (LocalDate) endDateField.getConvertedValue();
                if (startDate != null && endDate != null)
                    setValue(new Interval(startDate.toDateTimeAtCurrentTime(), endDate.toDateTimeAtCurrentTime()));
            };
            startDateField = new InlineDateField("Начало");
            startDateField.setConverter(LocalDate.class);
            startDateField.addValueChangeListener(dateListener);
            datesLayout.addComponent(startDateField);

            datesLayout.addComponent(new Label(DATES_DELIMITER, ContentMode.HTML));

            endDateField = new InlineDateField("Конец");
            endDateField.setConverter(LocalDate.class);
            endDateField.addValueChangeListener(dateListener);
            datesLayout.addComponent(endDateField);

            updateDates(getValue());

            layout.addComponents(namedLayout, datesLayout);

            return layout;
        }
    }

    private String getIntervalLabel(final Interval interval) {
        if (interval != null) {
            return MessageFormat.format(" [ {0} - {1} ]",
                    interval.getStart().toString("dd.MM.yyyy", lookup(Locale.class)),
                    interval.getEnd().toString("dd.MM.yyyy", lookup(Locale.class)));
        } else
            return NamedInterval.ALL_TIME.getCaption();
    }

    private Interval getIntervalByName(final NamedInterval intervalName) {
        Interval interval = null;
        final DateTime today = new DateTime();
        switch (intervalName) {

            case THIS_WEEK: {
                final DateTime startDate = today.dayOfWeek().withMinimumValue();
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case LAST_WEEK: {
                final DateTime startDate = today.minus(Period.weeks(1)).dayOfWeek().withMinimumValue();
                final DateTime endDate = startDate.dayOfWeek().withMaximumValue();
                interval = new Interval(startDate, endDate);
            }
            break;
            case WEEK_AGO: {
                final DateTime startDate = today.minus(Period.weeks(1));
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case THIS_MONTH: {
                final DateTime startDate = today.dayOfMonth().withMinimumValue();
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case LAST_MONTH: {
                final DateTime startDate = today.minus(Period.months(1)).dayOfMonth().withMinimumValue();
                final DateTime endDate = startDate.dayOfMonth().withMaximumValue();
                interval = new Interval(startDate, endDate);
            }
            break;
            case MONTH_AGO: {
                final DateTime startDate = today.minus(Period.months(1));
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case THREE_MONTH_AGO: {
                final DateTime startDate = today.minus(Period.months(3));
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case HALF_YEAR_AGO: {
                final DateTime startDate = today.minus(Period.months(6));
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case THIS_YEAR: {
                final DateTime startDate = today.dayOfYear().withMinimumValue();
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case LAST_YEAR: {
                final DateTime startDate = today.minus(Period.years(1)).dayOfYear().withMinimumValue();
                final DateTime endDate = startDate.dayOfYear().withMaximumValue();
                interval = new Interval(startDate, endDate);
            }
            break;
            case YEAR_AGO: {
                final DateTime startDate = today.minus(Period.years(1));
                final DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case CUSTOM_INTERVAL:
                break;
            case ALL_TIME:
                break;
        }
        return interval;
    }

    /**
     * Returns the type of the Field. The methods <code>getValue</code> and
     * <code>setValue</code> must be compatible with this type: one must be able
     * to safely cast the value returned from <code>getValue</code> to the given
     * type and pass any variable assignable to this type as an argument to
     * <code>setValue</code>.
     *
     * @return the type of the Field
     */
    @Override
    public Class<? extends Interval> getType() {
        return Interval.class;
    }
}
