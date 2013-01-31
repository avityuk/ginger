package com.vityuk.ginger;

public class LocalizationException extends RuntimeException {
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
