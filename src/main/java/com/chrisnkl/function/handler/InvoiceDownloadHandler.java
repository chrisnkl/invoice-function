package com.chrisnkl.function.handler;

import com.chrisnkl.function.exception.InvoiceDownloadFailureException;
import com.chrisnkl.function.service.IBlobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InvoiceDownloadHandler implements DownloadHandler {

    private final IBlobService blobService;

    @Override
    public byte[] downloadInvoice(String fileName) throws InvoiceDownloadFailureException {
        return blobService.downloadInvoice(fileName);
    }
}
