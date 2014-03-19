package ru.extas.server;

import ru.extas.model.FormTransfer;

/**
 * <p>FormTransferService interface.</p>
 *
 * @author Valery Orlov
 *         Date: 18.12.13
 *         Time: 13:11
 * @version $Id: $Id
 */
public interface FormTransferService {
/**
 * Вставить/обновить акт приема передачи
 *
 * @param tf акт приема передачи
 */
void saveAndChangeOwner(FormTransfer tf);
}
