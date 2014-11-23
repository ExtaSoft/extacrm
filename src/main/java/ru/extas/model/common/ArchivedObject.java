package ru.extas.model.common;

/**
 * Идентифицируемый объект с возможностью архивации
 *
 * @author Valery Orlov
 *         Date: 23.11.2014
 *         Time: 18:18
 */
public interface ArchivedObject {

    String COLUMN_NAME = "IS_ARCHIVED";
    String PROPERTY_NAME = "archived";

    boolean isArchived();

    void setArchived(boolean archived);
}
