package com.chrisnkl.function.handler;

import com.chrisnkl.function.domain.response.InvoiceUploadResponse;
import com.chrisnkl.function.exception.InvoiceUploadFailureException;
import com.chrisnkl.function.domain.request.InvoiceUploadRequest;

@FunctionalInterface
public interface UploadHandler {

    InvoiceUploadResponse uploadInvoice(InvoiceUploadRequest request) throws InvoiceUploadFailureException;

}
