package com.chrisnkl.function.config;

import com.chrisnkl.function.handler.DownloadHandler;
import com.chrisnkl.function.handler.InvoiceDownloadHandler;
import com.chrisnkl.function.handler.InvoiceUploadHandler;
import com.chrisnkl.function.handler.UploadHandler;
import com.chrisnkl.function.service.BlobService;
import com.chrisnkl.function.service.IBlobService;
import lombok.Getter;

public class ServiceFactory {

    @Getter private static final IBlobService blobService = new BlobService();
    @Getter private static final UploadHandler invoiceUploadHandler = new InvoiceUploadHandler(blobService);
    @Getter private static final DownloadHandler invoiceDownloadHandler = new InvoiceDownloadHandler(blobService);


    private ServiceFactory() {}


}
