package com.chrisnkl.function.exception;

import lombok.Getter;

@Getter
public class InvoiceNotFoundException extends ApiException {

    public InvoiceNotFoundException() {
        super();
    }

    public InvoiceNotFoundException(String message) {
        super(message);
    }

    public InvoiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
