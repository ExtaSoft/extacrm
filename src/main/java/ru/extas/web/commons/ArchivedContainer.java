package ru.extas.web.commons;

import ru.extas.model.common.IdentifiedObject;

/**
 * @author Valery Orlov
 *         Date: 23.11.2014
 *         Time: 20:21
 */
public interface ArchivedContainer {

    boolean isArchiveExcluded();

    void setArchiveExcluded(boolean archiveExcluded);
}
