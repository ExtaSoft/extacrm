package ru.extas.model.sale;

import ru.extas.model.common.FileContainer;
import ru.extas.model.common.OwnedFileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Valery Orlov
 *         Date: 11.11.2014
 *         Time: 23:41
 */
@Entity
@Table(name = "SALE_FILE")
public class SaleFileContainer extends OwnedFileContainer {
    public SaleFileContainer(final String ownerId, final FileContainer file) {
        super(ownerId, file);
    }

    public SaleFileContainer() {
    }

    public SaleFileContainer(final FileContainer file) {
        this(null, file);
    }
}
