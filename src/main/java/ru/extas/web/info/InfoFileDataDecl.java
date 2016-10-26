package ru.extas.web.info;

import com.google.common.base.Joiner;
import com.vaadin.data.Item;
import ru.extas.model.common.FileContainer;
import ru.extas.model.info.InfoFile;
import ru.extas.model.info.InfoFile_;
import ru.extas.web.commons.FileDownloadButton;
import ru.extas.web.commons.GridDataDecl;
import ru.extas.web.commons.GridItem;

/**
 * Created by valery on 25.10.16.
 */
public class InfoFileDataDecl extends GridDataDecl {
    public InfoFileDataDecl() {
        addMapping(InfoFile_.name.getName(), "Файл", new ComponentColumnGenerator() {
            @Override
            public Object generateCell(Object columnId, Item item, Object itemId) {
                final FileContainer file = GridItem.extractBean(item);
                return new FileDownloadButton(file);
            }
        });
        addMapping(InfoFile_.description.getName(), "Описание");
        addMapping(InfoFile_.permitBrands.getName(), "Доступ брендам", new ComponentColumnGenerator() {
            @Override
            public String generateCell(final Object columnId, final Item item, final Object itemId) {
                return Joiner.on(", ").join(GridItem.<InfoFile>extractBean(item).getPermitBrands());
            }
        });
        addDefaultMappings();
    }
}
