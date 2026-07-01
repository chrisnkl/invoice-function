package com.chrisnkl.function.handler;

import com.chrisnkl.function.exception.InvoiceDownloadFailureException;

@FunctionalInterface
public interface DownloadHandler {

    byte[] downloadInvoice(String fileName) throws InvoiceDownloadFailureException;

}
