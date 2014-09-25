package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import ru.extas.model.contacts.Person;
import ru.extas.web.commons.ExtaTheme;
import ru.extas.web.commons.Fontello;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.NotificationUtil;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.extas.web.commons.TableUtils.fullInitTable;

/**
 * Реализует ввод/редактирование списка сотрудников для компании и торговой точки
 *
 * @author Valery Orlov
 *         Date: 17.02.14
 *         Time: 13:04
 * @version $Id: $Id
 * @since 0.3
 */
public class ContactEmployeeField extends CustomField<Set> {

    private Table table;
    private BeanItemContainer<Person> container;

    /**
     * <p>Constructor for ContactEmployeeField.</p>
     */
    public ContactEmployeeField() {
        super.setBuffered(true);
        setBuffered(true);
        setWidth(600, Unit.PIXELS);
        setHeight(300, Unit.PIXELS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSizeFull();
        fieldLayout.setSpacing(true);
        fieldLayout.setMargin(new MarginInfo(true, false, true, false));

        if (!isReadOnly()) {
            final MenuBar commandBar = new MenuBar();
            commandBar.setAutoOpen(true);
            commandBar.addStyleName(ExtaTheme.GRID_TOOLBAR);
            commandBar.addStyleName(ExtaTheme.MENUBAR_BORDERLESS);
            commandBar.focus();

            final MenuBar.MenuItem addProdBtn = commandBar.addItem("Добавить", event -> {

                final PersonSelectWindow selectWindow = new PersonSelectWindow("Выберите сотрудника");
                selectWindow.addCloseListener(e -> {
                    if (selectWindow.isSelectPressed()) {
                        container.addBean(selectWindow.getSelected());
                        setValue(newHashSet(container.getItemIds()));
                        NotificationUtil.showSuccess("Сотрудник добавлен");
                    }
                });
                selectWindow.showModal();
            });
            addProdBtn.setDescription("Добавить физ. лицо в список сотрудников");
            addProdBtn.setIcon(Fontello.DOC_NEW);

            final MenuBar.MenuItem delProdBtn = commandBar.addItem("Удалить", event -> {
                if (table.getValue() != null) {
                    container.removeItem(table.getValue());
                    setValue(newHashSet(container.getItemIds()));
                }
            });
            delProdBtn.setDescription("Удалить физ. лицо из списка сотрудников");
            delProdBtn.setIcon(Fontello.TRASH);

            fieldLayout.addComponent(commandBar);
        }

        table = new Table();
        table.setSizeFull();
        table.setRequired(true);
        table.setSelectable(true);
        table.setColumnCollapsingAllowed(true);
        final Property dataSource = getPropertyDataSource();
        final Set<Person> list = dataSource != null ? (Set<Person>) dataSource.getValue() : new HashSet<Person>();
        container = new BeanItemContainer<>(Person.class);
        if (list != null) {
            for (final Person doc : list) {
                container.addBean(doc);
            }
        }
        container.addNestedContainerProperty("regAddress.region");
        table.setContainerDataSource(container);

        final GridDataDecl dataDecl = new PersonDataDecl();
        fullInitTable(table, dataDecl);

        fieldLayout.addComponent(table);
        fieldLayout.setExpandRatio(table, 1);

        return fieldLayout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Set> getType() {
        return Set.class;
    }

}
