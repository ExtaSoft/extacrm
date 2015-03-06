package ru.extas.model.sale;

import ru.extas.model.common.FileContainer;
import ru.extas.model.lead.LeadFileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Valery Orlov
 *         Date: 11.11.2014
 *         Time: 23:41
 */
@Entity
@Table(name = "SALE_FILE")
public class SaleFileContainer extends FileContainer {
    public SaleFileContainer(String ownerId, FileContainer file) {
        super(ownerId, file);
    }

    public SaleFileContainer() {
    }

    public SaleFileContainer(FileContainer file) {
        this(null, file);
    }
}
