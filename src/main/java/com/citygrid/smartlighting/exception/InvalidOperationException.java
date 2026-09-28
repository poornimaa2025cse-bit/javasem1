package com.citygrid.smartlighting.exception;

/** Thrown for valid-but-not-allowed operations, e.g. paying a bill that isn't posted yet. */
public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
