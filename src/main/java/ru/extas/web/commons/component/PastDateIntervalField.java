package ru.extas.web.commons.component;

import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.*;
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
public class PastDateIntervalField extends CustomField<Interval> {

    public static final String DATES_DELIMITER = FontAwesome.MINUS.getHtml();
    private LocalDateField startDateField;
    private LocalDateField endDateField;
    private ComboBox namedIntervalField;
    private Label datesLabel;

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

        NamedInterval(String caption, String description) {
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
        setCaption(caption);
        setDescription(description);
        addValueChangeListener(e -> {
            if(e.getProperty().getValue() == null)
                namedIntervalField.setValue(NamedInterval.ALL_TIME);
        });
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
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        layout.setMargin(new MarginInfo(true, false, true, false));
        layout.setSpacing(true);

        namedIntervalField = new ComboBox();
        namedIntervalField.setWidth(31, Unit.EX);
        namedIntervalField.setPageLength(15);
        namedIntervalField.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        namedIntervalField.setFilteringMode(FilteringMode.OFF);
        namedIntervalField.setNewItemsAllowed(false);
        namedIntervalField.setTextInputAllowed(false);
        namedIntervalField.setInvalidAllowed(false);
        namedIntervalField.setNullSelectionAllowed(false);
//        namedIntervalField.addStyleName(ExtaTheme.COMBOBOX_BORDERLESS);
        for (NamedInterval intervalName : NamedInterval.values()) {
            Item intervalItem = namedIntervalField.addItem(intervalName);
            String intervalNameCaption = intervalName.getCaption();
            namedIntervalField.setItemCaption(intervalName, intervalNameCaption);
        }
        namedIntervalField.addValueChangeListener(e -> {
            boolean isDatesVisible = false;
            String label = DATES_DELIMITER;
            final NamedInterval namedInterval = (NamedInterval) namedIntervalField.getValue();
            Interval interval = getIntervalByName(namedInterval);
            if (interval != null) {
                setValue(interval);
                label = MessageFormat.format(" ({0} - {1})",
                        interval.getStart().toString("dd.MM.yyyy", lookup(Locale.class)),
                        interval.getEnd().toString("dd.MM.yyyy", lookup(Locale.class)));
            } else if (namedInterval == NamedInterval.CUSTOM_INTERVAL) {
                isDatesVisible = true;
                label = DATES_DELIMITER;
                interval = getValue();
                if (interval != null) {
                    startDateField.setConvertedValue(interval.getStart().toLocalDate());
                    endDateField.setConvertedValue(interval.getEnd().toLocalDate());
                }
            } else if (namedInterval == NamedInterval.ALL_TIME) {
                setValue(null);
                label = "Весь диапазон значений";
            }
            startDateField.setVisible(isDatesVisible);
            endDateField.setVisible(isDatesVisible);
            datesLabel.setValue(label);
        });
        layout.addComponent(namedIntervalField);

        final ValueChangeListener dateListener = e -> {
            LocalDate startDate = (LocalDate) startDateField.getConvertedValue();
            LocalDate endDate = (LocalDate) endDateField.getConvertedValue();
            if (startDate != null && endDate != null)
                setValue(new Interval(startDate.toDateTimeAtCurrentTime(), endDate.toDateTimeAtCurrentTime()));
        };
        startDateField = new LocalDateField();
        startDateField.addValueChangeListener(dateListener);
        layout.addComponent(startDateField);

        datesLabel = new Label(DATES_DELIMITER, ContentMode.HTML);
        layout.addComponent(datesLabel);

        endDateField = new LocalDateField();
        endDateField.addValueChangeListener(dateListener);
        layout.addComponent(endDateField);

        startDateField.setVisible(false);
        endDateField.setVisible(false);

        namedIntervalField.setValue(NamedInterval.ALL_TIME);

        return layout;
    }

    private Interval getIntervalByName(NamedInterval intervalName) {
        Interval interval = null;
        DateTime today = new DateTime();
        switch (intervalName) {

            case THIS_WEEK: {
                DateTime startDate = today.dayOfWeek().withMinimumValue();
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case LAST_WEEK: {
                DateTime startDate = today.minus(Period.weeks(1)).dayOfWeek().withMinimumValue();
                DateTime endDate = startDate.dayOfWeek().withMaximumValue();
                interval = new Interval(startDate, endDate);
            }
            break;
            case WEEK_AGO: {
                DateTime startDate = today.minus(Period.weeks(1));
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case THIS_MONTH: {
                DateTime startDate = today.dayOfMonth().withMinimumValue();
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case LAST_MONTH: {
                DateTime startDate = today.minus(Period.months(1)).dayOfMonth().withMinimumValue();
                DateTime endDate = startDate.dayOfMonth().withMaximumValue();
                interval = new Interval(startDate, endDate);
            }
            break;
            case MONTH_AGO: {
                DateTime startDate = today.minus(Period.months(1));
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case THREE_MONTH_AGO: {
                DateTime startDate = today.minus(Period.months(3));
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case HALF_YEAR_AGO: {
                DateTime startDate = today.minus(Period.months(6));
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case THIS_YEAR: {
                DateTime startDate = today.dayOfYear().withMinimumValue();
                DateTime endDate = today;
                interval = new Interval(startDate, endDate);
            }
            break;
            case LAST_YEAR: {
                DateTime startDate = today.minus(Period.years(1)).dayOfYear().withMinimumValue();
                DateTime endDate = startDate.dayOfYear().withMaximumValue();
                interval = new Interval(startDate, endDate);
            }
            break;
            case YEAR_AGO: {
                DateTime startDate = today.minus(Period.years(1));
                DateTime endDate = today;
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
