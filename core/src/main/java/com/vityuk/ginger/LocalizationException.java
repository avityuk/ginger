package com.vityuk.ginger;

public class LocalizationException extends RuntimeException {
    private static final long serialVersionUID = -14501346199256189L;

    public LocalizationException() {
        super();
    }

    public LocalizationException(String message) {
        super(message);
    }

    public LocalizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalizationException(Throwable cause) {
        super(cause);
    }
}
