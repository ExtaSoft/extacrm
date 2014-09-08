package ru.extas.web.contacts;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component initContent() {
        final VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setSpacing(true);

        if (!isReadOnly()) {
            final HorizontalLayout commandBar = new HorizontalLayout();
            commandBar.addStyleName(ExtaTheme.CONFIGURE);
            commandBar.setSpacing(true);

            final Button addProdBtn = new Button("Добавить", new Button.ClickListener() {

                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {

                    final PersonSelectWindow selectWindow = new PersonSelectWindow("Выберите сотрудника");
                    selectWindow.addCloseListener(e -> {
                        if (selectWindow.isSelectPressed()) {
                            container.addBean(selectWindow.getSelected());
                            setValue(newHashSet(container.getItemIds()));
                            NotificationUtil.showSuccess("Сотрудник добавлен");
                        }
                    });
                    selectWindow.showModal();
                }
            });
            addProdBtn.setDescription("Добавить физ. лицо в список сотрудников");
            addProdBtn.setIcon(Fontello.DOC_NEW);
            commandBar.addComponent(addProdBtn);

            final Button delProdBtn = new Button("Удалить", event -> {
                if (table.getValue() != null) {
                    container.removeItem(table.getValue());
                    setValue(newHashSet(container.getItemIds()));
                }
            });
            delProdBtn.setDescription("Удалить физ. лицо из списка сотрудников");
            delProdBtn.setIcon(Fontello.TRASH);
            commandBar.addComponent(delProdBtn);

            fieldLayout.addComponent(commandBar);
        }

        table = new Table();
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
        container.addNestedContainerProperty("actualAddress.region");
        table.setContainerDataSource(container);

        final GridDataDecl dataDecl = new PersonDataDecl();
        fullInitTable(table, dataDecl);

        fieldLayout.addComponent(table);

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
