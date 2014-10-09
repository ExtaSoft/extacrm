package ru.extas;

/**
 * @author Valery Orlov
 *         Date: 09.10.2014
 *         Time: 11:21
 */
public class ExtaException extends RuntimeException {

    public ExtaException() {
    }

    public ExtaException(String message) {
        super(message);
    }

    public ExtaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtaException(Throwable cause) {
        super(cause);
    }
}
