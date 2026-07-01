package com.chrisnkl.function.exception;

import lombok.Getter;

@Getter
public class InvoiceUploadFailureException extends ApiException {

    public InvoiceUploadFailureException() {
        super();
    }

    public InvoiceUploadFailureException(String message) {
        super(message);
    }

    public InvoiceUploadFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
