package ru.extas.web.commons.container.jpa;

/**
 * @author Valery Orlov
 *         Date: 14.04.2015
 *         Time: 12:01
 */
public class JpaSortBy {
    private final String property;
    private final boolean asceding;

    public JpaSortBy(final String property, final boolean asceding) {
        this.property = property;
        this.asceding = asceding;
    }

    public String getProperty() {
        return property;
    }

    public boolean isAsceding() {
        return asceding;
    }
}
