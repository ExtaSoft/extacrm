/**
 *
 */
package ru.extas.web.insurance;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import ru.extas.model.FormTransfer;
import ru.extas.vaadin.addon.jdocontainer.LazyJdoContainer;
import ru.extas.web.commons.GridDataDecl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Valery Orlov
 */
public class FormTransferGrid extends CustomComponent {

    private static final long serialVersionUID = 1170175803163742829L;

    private final Table table = new Table();

    public FormTransferGrid() {

        final CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();

        // Запрос данных
        final LazyJdoContainer<FormTransfer> container = new LazyJdoContainer<>(FormTransfer.class, 50,
                null);
        container.addContainerProperty("fromContact.name", String.class, null, true, false);
        container.addContainerProperty("toContact.name", String.class, null, true, false);

        final HorizontalLayout commandBar = new HorizontalLayout();
        commandBar.addStyleName("configure");
        commandBar.setSpacing(true);

        final Button newTFBtn = new Button("Новый", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                final Object newObjId = container.addItem();
                final BeanItem<FormTransfer> newObj = (BeanItem<FormTransfer>) container.getItem(newObjId);

                final FormTransferEditForm editWin = new FormTransferEditForm("Новый акт приема/передачи", newObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            container.commit();
                            table.select(newObjId);
                            Notification.show("Акт приема/передачи сохранен", Type.TRAY_NOTIFICATION);
                        } else {
                            container.discard();
                        }
                    }
                });
                editWin.showModal();
            }
        });
        newTFBtn.addStyleName("icon-doc-new");
        newTFBtn.setDescription("Ввод нового акта приема/передачи");
        commandBar.addComponent(newTFBtn);

        final Button editTFBtn = new Button("Изменить", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public void buttonClick(final ClickEvent event) {
                // Взять текущий полис из грида
                final Object curObjId = checkNotNull(table.getValue(), "No selected row");
                final BeanItem<FormTransfer> curObj = (BeanItem<FormTransfer>) table.getItem(curObjId);

                final FormTransferEditForm editWin = new FormTransferEditForm("Редактировать акт приема/передачи",
                        curObj);
                editWin.addCloseListener(new CloseListener() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void windowClose(final CloseEvent e) {
                        if (editWin.isSaved()) {
                            Notification.show("Акт приема/передачи сохранен", Type.TRAY_NOTIFICATION);
                        }
                    }
                });
                editWin.showModal();
            }
        });
        editTFBtn.addStyleName("icon-edit-3");
        editTFBtn.setDescription("Редактировать выделенный в списке акта приема/передачи");
        editTFBtn.setEnabled(false);
        commandBar.addComponent(editTFBtn);

        // PopupButton popupButton = new PopupButton("Action");
        // HorizontalLayout popupLayout = new HorizontalLayout();
        // popupButton.setContent(popupLayout); // Set popup content
        // Button modifyButton = new Button("Modify");
        // modifyButton.setIcon(new
        // ThemeResource("../runo/icons/16/document-txt.png"));
        // popupLayout.addComponent(modifyButton);
        // Button addButton = new Button("Add");
        // addButton.setIcon(new
        // ThemeResource("../runo/icons/16/document-add.png"));
        // popupLayout.addComponent(addButton);
        // Button deleteButton = new Button("Delete");
        // deleteButton.setIcon(new
        // ThemeResource("../runo/icons/16/document-delete.png"));
        // commandBar.addComponent(popupButton);
        // popupLayout.addComponent(deleteButton);

        final Button printTFBtn = new Button("Печать");
        printTFBtn.addStyleName("icon-print-2");
        printTFBtn.setDescription("Создать печатное представление полиса страхования");
        printTFBtn.setEnabled(false);
        // createPolicyDownloader(true).extend(printPolyceMatBtn);
        commandBar.addComponent(printTFBtn);

        panel.addComponent(commandBar);

        table.setContainerDataSource(container);
        table.setSizeFull();

        // Обеспечиваем корректную работу кнопок зависящих от выбранной записи
        table.setImmediate(true);
        table.addValueChangeListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(final ValueChangeEvent event) {
                final boolean enableBtb = event.getProperty().getValue() != null;
                editTFBtn.setEnabled(enableBtb);
                printTFBtn.setEnabled(enableBtb);
            }
        });

        final GridDataDecl ds = new FormTransferDataDecl();
        ds.initTableColumns(table);

        panel.addComponent(table);

        setCompositionRoot(panel);
    }

}
