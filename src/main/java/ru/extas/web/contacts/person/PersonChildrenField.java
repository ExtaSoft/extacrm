package ru.extas.web.contacts.person;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.model.contacts.PersonChild;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.component.EditField;
import ru.extas.web.commons.component.LocalDateField;
import ru.extas.web.commons.container.ExtaBeanContainer;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author Valery Orlov
 *         Date: 09.09.2014
 *         Time: 13:02
 */
public class PersonChildrenField extends CustomField<List> {


    private final Person person;
    private ExtaBeanContainer<PersonChild> itemContainer;
    private VerticalLayout root;

    public PersonChildrenField(final String caption, final Person person) {
        setCaption(caption);
        this.person = person;
    }

    @Override
    protected Component initContent() {
        final Property dataSource = this.getPropertyDataSource();
        final List<PersonChild> list = dataSource != null ? (List<PersonChild>) dataSource.getValue() : new ArrayList<>();
        itemContainer = new ExtaBeanContainer<>(PersonChild.class);

        root = new VerticalLayout();
        root.setMargin(new MarginInfo(true, false, true, false));
        root.setSpacing(true);

        final Button addBtn = new Button("Добавить", Fontello.PLUS);
        addBtn.addStyleName(ExtaTheme.BUTTON_BORDERLESS_COLORED);
        addBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        addBtn.addClickListener(click -> {
            addChild(new PersonChild(person));
            updateValue();
        });
        root.addComponent(addBtn);

        for (final PersonChild child : list)
            addChild(child);

        return root;
    }

    private void addChild(final PersonChild child) {
        final BeanItem<PersonChild> childItem = itemContainer.addBean(child);

        final CssLayout dataLine = new CssLayout();
        dataLine.addStyleName(ExtaTheme.LAYOUT_COMPONENT_GROUP);

        final LocalDateField birthday = new LocalDateField();
        birthday.setInputPrompt("Дата рождения");
        birthday.addStyleName(ExtaTheme.INLINE_DATEFIELD);
        birthday.addStyleName(ExtaTheme.DATEFIELD_SMALL);
        birthday.setPropertyDataSource(childItem.getItemProperty("birthday"));
        birthday.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(birthday);

        final EditField name = new EditField();
        name.setInputPrompt("Ф.И.О.");
        name.setColumns(15);
        name.addStyleName(ExtaTheme.TEXTFIELD_SMALL);
        name.setPropertyDataSource(childItem.getItemProperty("name"));
        name.addValueChangeListener(event -> updateValue());
        dataLine.addComponent(name);

        final Button delItemBtn = new Button("Удалить");
        delItemBtn.setDescription("Удалить сведения о ребенке");
        delItemBtn.setIcon(Fontello.TRASH_4);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_ICON_ONLY);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_QUIET);
        delItemBtn.addStyleName(ExtaTheme.BUTTON_SMALL);
        delItemBtn.addClickListener(event -> {
            root.removeComponent(dataLine);
            itemContainer.removeItem(child);
            updateValue();
        });
        dataLine.addComponent(delItemBtn);

        root.addComponent(dataLine);
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private void updateValue() {
        setValue(newArrayList(itemContainer.getItemIds()));
    }
}
