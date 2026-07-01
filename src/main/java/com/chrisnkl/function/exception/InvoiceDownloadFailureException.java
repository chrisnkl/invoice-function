package com.chrisnkl.function.exception;

import lombok.Getter;

@Getter
public class InvoiceDownloadFailureException extends ApiException {

    public InvoiceDownloadFailureException() {
        super();
    }

    public InvoiceDownloadFailureException(String message) {
        super(message);
    }

    public InvoiceDownloadFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
