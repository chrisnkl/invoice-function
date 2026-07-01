package com.chrisnkl.function.service;

import com.chrisnkl.function.domain.request.InvoiceUploadRequest;
import com.chrisnkl.function.domain.response.InvoiceUploadResponse;

public interface IBlobService {

    InvoiceUploadResponse uploadInvoice(InvoiceUploadRequest request);
    byte[] downloadInvoice(String blobName);

}
