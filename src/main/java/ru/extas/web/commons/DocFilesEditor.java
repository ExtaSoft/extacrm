package ru.extas.web.commons;

import com.google.common.io.ByteStreams;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import ru.extas.model.common.FileContainer;
import ru.extas.model.contacts.LegalEntity;
import ru.extas.web.commons.component.FileUploader;
import ru.extas.web.commons.converters.MimeTypeConverter;
import ru.extas.web.commons.window.DownloadFileWindow;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ru.extas.web.commons.GridItem.extractBean;

/**
 * Компонент для ввода редактирования набора прикрепленных файлов.
 *
 * @author Valery Orlov
 *         Date: 18.04.2014
 *         Time: 15:20
 */
public class DocFilesEditor extends CustomField<List>{

    public DocFilesEditor() {
        setBuffered(true);
        addStyleName("base-view");
        setSizeUndefined();
        setWidth(700, Unit.PIXELS);
    }

    @Override
    protected Component initContent() {
        return new DocFilesGrid();
    }

    @Override
    public Class<? extends List> getType() {
        return List.class;
    }

    private class DocFilesGrid extends ExtaGrid {

        @Override
        protected GridDataDecl createDataDecl() {
            return new DocFilesDataDecl();
        }

        @Override
        protected Container createContainer() {
            final Property dataSource = DocFilesEditor.this.getPropertyDataSource();
            final List<FileContainer> list = dataSource != null ? (List<FileContainer>) dataSource.getValue() : new ArrayList<FileContainer>();
            BeanItemContainer<FileContainer> itemContainer = new BeanItemContainer<>(FileContainer.class);
            if (list != null) {
                for (final FileContainer item : list) {
                    itemContainer.addBean(item);
                }
            }
            return itemContainer;
        }

        @Override
        protected List<UIAction> createActions() {
            List<UIAction> actions = newArrayList();

            actions.add(new UIAction("Добавить файлы", "Загрузить новые файлы в систему", "icon-doc-new") {
                @Override
                public void fire(Object itemId) { }

                @Override
                public Component createButton() {
                    UploadFinishedHandler handler = new UploadFinishedHandler() {
                        @Override
                        public void handleFile(InputStream inputStream, String filename, String mimeType, long length) {
                            FileContainer fileContainer = new FileContainer();
                            fileContainer.setName(filename);
                            fileContainer.setMimeType(mimeType);
                            fileContainer.setFileSize(length);
                            try {
                                fileContainer.setFileData(ByteStreams.toByteArray(inputStream));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((BeanItemContainer<FileContainer>) container).addBean(fileContainer);
                            DocFilesEditor.this.setValue(newArrayList(((BeanItemContainer<FileContainer>) container).getItemIds()));
                        }
                    };
                    FileUploader fileUploader = new FileUploader("Добавить файлы", handler);
                    fileUploader.setStyleName("icon-download");
                    return fileUploader;
                }
            });

            actions.add(new ItemAction("Скачать", "Скачать выделенный в списке документ", "icon-download") {
                @Override
                public void fire(Object itemId) {
                    final FileContainer fileContainer = extractBean(table.getItem(itemId));

                    new DownloadFileWindow(fileContainer.getFileData(), fileContainer.getName()).showModal();
                }
            });

            actions.add(new ItemAction("Удалить", "Удалить файл из списка", "icon-trash") {
                @Override
                public void fire(final Object itemId) {
                    container.removeItem(itemId);
                    DocFilesEditor.this.setValue(newArrayList(((BeanItemContainer<LegalEntity>) container).getItemIds()));
                }
            });
            return actions;
        }
    }

    private class DocFilesDataDecl extends GridDataDecl {

        private DocFilesDataDecl() {
            addMapping("name", "Имя файла");
            addMapping("mimeType", "Тип", MimeTypeConverter.class);
            addMapping("fileSize", "Размер");
            super.addDefaultMappings();
        }
    }
}
