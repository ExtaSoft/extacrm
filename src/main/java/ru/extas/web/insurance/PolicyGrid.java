/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import ru.extas.model.Policy;
import ru.extas.web.commons.ExtaDataContainer;
import ru.extas.web.commons.GridDataDecl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Список страховых полисов в рамках БСО
 *
 * @author Valery Orlov
 */
public class PolicyGrid extends CustomComponent {

    private static final long serialVersionUID = 4876073256421755574L;

    // private final Logger logger =
    // LoggerFactory.getLogger(InsuranceGrid.class);
    private final Table table;

    public PolicyGrid() {

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Запрос данных
        final JPAContainer<Policy> container = new ExtaDataContainer<>(Policy.class);

        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);

        final Button newPolicyBtn = new Button("Новый", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                final BeanItem<Policy> newObj = new BeanItem<>(new Policy());

                final PolicyEditForm editWin = new PolicyEditForm("Новый бланк", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.refresh();
                            Notification.show("Бланк сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        newPolicyBtn.addStyleName("icon-doc-new");
        newPolicyBtn.setDescription("Ввод нового бланка");
        commandBar.addComponent(newPolicyBtn);

        final Button editPolicyBtn = new Button("Изменить", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                // // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<Policy> curObj = new BeanItem<>(((EntityItem<Policy>) table.getItem(curObjId)).getEntity());

                final PolicyEditForm editWin = new PolicyEditForm("Редактировать бланк", curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.refreshItem(curObjId);
                            Notification.show("Бланк сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        editPolicyBtn.addStyleName("icon-edit-3");
        editPolicyBtn.setDescription("Редактировать выделенный в списке бланк");
        editPolicyBtn.setEnabled(false);
        commandBar.addComponent(editPolicyBtn);

        panel.addComponent(commandBar);

        table = new Table();
        table.setContainerDataSource(container);
        table.setSizeFull();

        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.setImmediate(true);
        table.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final boolean enableBtb = event.getProperty().getValue() != null;
                editPolicyBtn.setEnabled(enableBtb);
            }
        });
// if (table.size() > 0)
// table.select(table.firstItemId());

        final GridDataDecl ds = new PolicyDataDecl();
        ds.initTableColumns(table);

        panel.addComponent(table);

        setCompositionRoot(panel);
    }
}
