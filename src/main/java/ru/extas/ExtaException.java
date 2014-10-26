package ru.extas;

/**
 * @author Valery Orlov
 *         Date: 09.10.2014
 *         Time: 11:21
 */
public class ExtaException extends RuntimeException {

    public ExtaException() {
    }

    public ExtaException(final String message) {
        super(message);
    }

    public ExtaException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExtaException(final Throwable cause) {
        super(cause);
    }
}
