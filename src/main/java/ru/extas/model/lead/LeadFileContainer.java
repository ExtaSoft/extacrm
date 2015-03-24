package ru.extas.model.lead;

import ru.extas.model.common.FileContainer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Valery Orlov
 *         Date: 11.11.2014
 *         Time: 23:41
 */
@Entity
@Table(name = "LEAD_FILE")
public class LeadFileContainer extends FileContainer {
}
