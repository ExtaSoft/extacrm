/**
 *
 */
package ru.extas.web.commons;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import ru.extas.web.commons.DataDeclMapping.PresentFlag;
import ru.extas.web.users.LoginToUserNameConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static ru.extas.server.ServiceLocator.lookup;

/**
 * Опции отрбражения датасета в таблице:
 * <ul>
 * <li>порядок</li>
 * <li>заголовок</li>
 * <li>доступность</li>
 * <li>видимость</li>
 * </ul>
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public class GridDataDecl implements Serializable {

    public void setColumnCollapsed(final String column, final boolean collapsed) {
        final Optional<DataDeclMapping> mapping = getMappings().stream().filter(m -> m.getPropName().equals(column)).findFirst();
        if(mapping.isPresent())
            mapping.get().setCollapsed(collapsed);
    }

    public interface GridColumnGenerator extends Serializable {
        Object generateCell(Object columnId, Item item, Object itemId);

        Property getCellProperty(Object columnId, Item item);

        Class<?> getType();
    }

    public static abstract class ComponentColumnGenerator implements GridColumnGenerator {
        @Override
        public Property getCellProperty(final Object columnId, final Item item) {
            return null;
        }

        @Override
        public Class<?> getType() {
            return null;
        }
    }

    private final List<DataDeclMapping> mappings = new ArrayList<>();

    /**
     * <p>Constructor for GridDataDecl.</p>
     */
    protected GridDataDecl() {
        super();
        addMapping("id", "Идентификатор", EnumSet.of(PresentFlag.COLLAPSED));
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName     - Имя свойства
     * @param caption      - Заголовок столбца
     * @param presentFlags параметры отображения
     */
    protected void addMapping(final String propName, final String caption, final EnumSet<PresentFlag> presentFlags) {
        mappings.add(new DataDeclMapping(propName, caption, presentFlags));
    }

    /**
     * <p>addMapping.</p>
     *
     * @param propName a {@link java.lang.String} object.
     * @param caption a {@link java.lang.String} object.
     * @param generator a {@link ru.extas.web.commons.GridDataDecl.GridColumnGenerator} object.
     * @param presentFlags a {@link java.util.EnumSet} object.
     */
    protected void addMapping(final String propName, final String caption, final GridColumnGenerator generator, final EnumSet<PresentFlag> presentFlags) {
        mappings.add(new DataDeclMapping(propName, caption, generator, presentFlags));
    }

    protected void addMapping(final String propName, final String caption, final GridColumnGenerator generator) {
        addMapping(propName, caption, generator, null);
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName - Имя свойства
     * @param caption  - Заголовок столбца
     */
    protected void addMapping(final String propName, final String caption) {
        mappings.add(new DataDeclMapping(propName, caption));
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName     - Имя свойства
     * @param caption      - Заголовок столбца
     * @param converterCls a {@link java.lang.Class} object.
     */
    public void addMapping(final String propName, final String caption, final Class<? extends Converter<String, ?>> converterCls) {
        mappings.add(new DataDeclMapping(propName, caption, lookup(converterCls)));
    }

    /**
     * Задает параметры отображения для свойства объекта
     *
     * @param propName     - Имя свойства
     * @param caption      - Заголовок столбца
     * @param presentFlags - параметры отображения
     * @param converterCls - конвертер значения
     */
    public void addMapping(final String propName, final String caption, final EnumSet<PresentFlag> presentFlags, final Class<? extends Converter<String, ?>> converterCls) {
        mappings.add(new DataDeclMapping(propName, caption, presentFlags, lookup(converterCls), null));
    }

    /**
     * Добавляет маркеры создания/модификации записи
     */
    protected void addDefaultMappings() {
	    addMapping("lastModifiedBy", "Кто изменил", EnumSet.of(PresentFlag.COLLAPSED), LoginToUserNameConverter.class);
	    addMapping("lastModifiedDate", "Когда изменил", EnumSet.of(PresentFlag.COLLAPSED)/*
                                                                                     * ,
																					 * StringToJodaDTConverter
																					 * .
																					 * class
																					 */);
	    addMapping("createdBy", "Кто создал", EnumSet.of(PresentFlag.COLLAPSED), LoginToUserNameConverter.class);
	    addMapping("createdDate", "Когда создал", EnumSet.of(PresentFlag.COLLAPSED)/*
                                                                                 * ,
																				 * StringToJodaDTConverter
																				 * .
																				 * class
																				 */);
    }

    /**
     * <p>Getter for the field <code>mappings</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<DataDeclMapping> getMappings() {
        return mappings;
    }
}
