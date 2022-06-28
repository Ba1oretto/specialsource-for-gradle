package com.baioretto.specialsource.exception;

@SuppressWarnings("unused")
public class SpecialSourceInternalException extends RuntimeException {
    public SpecialSourceInternalException() {
    }

    public SpecialSourceInternalException(String message) {
        super(String.format("This is the plugin internal exception, %s", message));
    }
}
