/**
 *
 */
package ru.extas.server.references;

import java.util.Collection;

/**
 * Поставщик простых справочных данных
 *
 * @author Valery Orlov
 * @version $Id: $Id
 * @since 0.3
 */
public interface SupplementService {

	/**
	 * Запрашивает виды документов
	 *
	 * @return список видов документов
	 */
	Collection<String> loadDocumentTypes();

}
