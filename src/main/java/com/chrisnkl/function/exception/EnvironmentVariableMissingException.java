package com.chrisnkl.function.exception;

import lombok.Getter;

@Getter
public class EnvironmentVariableMissingException extends ApiException {

    public EnvironmentVariableMissingException() {
        super();
    }

    public EnvironmentVariableMissingException(String message) {
        super(message);
    }

    public EnvironmentVariableMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
