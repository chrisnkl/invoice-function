package com.chrisnkl.function.handler;

import com.chrisnkl.function.domain.response.InvoiceUploadResponse;
import com.chrisnkl.function.exception.InvoiceUploadFailureException;
import com.chrisnkl.function.domain.request.InvoiceUploadRequest;
import com.chrisnkl.function.service.IBlobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InvoiceUploadHandler implements UploadHandler {

    private final IBlobService blobService;

    @Override
    public InvoiceUploadResponse uploadInvoice(InvoiceUploadRequest request) throws InvoiceUploadFailureException {
        log.info("Uploading invoice with request: {}", request);
        return blobService.uploadInvoice(request);
    }
}
