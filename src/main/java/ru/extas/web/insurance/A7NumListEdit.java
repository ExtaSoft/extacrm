/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Компонент для редактирования списка номеров квитанций
 *
 * @author Valery Orlov
 */
@SuppressWarnings("rawtypes")
public class A7NumListEdit extends CustomField<List> {

    private static final long serialVersionUID = 4372821418602379921L;
    private Table formNums;

    /**
     * Создает поле с заголовком
     *
     * @param caption Заголовок
     */
    public A7NumListEdit(final String caption) {
        this.setCaption(caption);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.ui.CustomField#initContent()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Component initContent() {
        final VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSpacing(true);

        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);

        final Button addNumBtn = new Button("Добавить", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                final String newNum = String.valueOf(event.getRelativeX() * event.getRelativeY());
                addNumber(newNum);
            }
        });
        addNumBtn.setDescription("Добавить номер квитанции к акту приема/передачи");
        commandBar.addComponent(addNumBtn);

        final Button addNumRangeBtn = new Button("Диапазон", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                // TODO
            }
        });
        addNumRangeBtn.setDescription("Добавить диапазон номеров квитанции к акту приема/передачи");
        commandBar.addComponent(addNumRangeBtn);

        fieldLayout.addComponent(commandBar);

        formNums = new Table();
        formNums.addContainerProperty("Номер", String.class, null);
        formNums.addContainerProperty("Действия", Button.class, null);
        formNums.setRequired(true);
        formNums.setSelectable(true);
        formNums.setHeight(20, Unit.EM);
        formNums.setWidth(20, Unit.EM);
        final List<String> numList = (List<String>) getPropertyDataSource().getValue();
        if (numList != null) {
            for (final String num : numList) {
                addNumber(num);
            }
        }
        formNums.addItemSetChangeListener(new ItemSetChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void containerItemSetChange(final ItemSetChangeEvent event) {
                setValue(newArrayList(formNums.getItemIds()));

            }
        });
        fieldLayout.addComponent(formNums);

        return fieldLayout;
    }

    /**
     * @param num Добавляемый номер
     */
    private void addNumber(final String num) {
        final Button delItemBtn = new Button("Удалить");
        delItemBtn.setDescription("Удалить квитанцию из акта");
        delItemBtn.addStyleName("icon-trash");
        delItemBtn.addStyleName("icon-only");
        delItemBtn.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                formNums.removeItem(event.getButton().getData());
            }
        });
        delItemBtn.setData(num);

        formNums.addItem(new Object[]{num, delItemBtn}, num);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.ui.AbstractField#getType()
     */
    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.ui.AbstractField#commit()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void commit() throws SourceException, InvalidValueException {
        super.commit();
        getPropertyDataSource().setValue(newArrayList(formNums.getItemIds()));
    }
}
