package com.chrisnkl.function.service;

import com.azure.core.util.BinaryData;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.chrisnkl.function.config.ApplicationConfig;
import com.chrisnkl.function.domain.response.InvoiceUploadResponse;
import com.chrisnkl.function.exception.ApiException;
import com.chrisnkl.function.exception.EnvironmentVariableMissingException;
import com.chrisnkl.function.exception.InvoiceNotFoundException;
import com.chrisnkl.function.domain.request.InvoiceUploadRequest;
import com.chrisnkl.function.exception.InvoiceUploadFailureException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BlobService implements IBlobService {

    private final BlobContainerClient client;

    public BlobService() throws EnvironmentVariableMissingException {

        String account = ApplicationConfig.STORAGE_ACCOUNT;
        String container = ApplicationConfig.INVOICES_CONTAINER;
        String endpoint = String.format("https://%s.blob.core.windows.net", account);

        this.client = new BlobContainerClientBuilder()
                .endpoint(endpoint)
                .containerName(container)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

    }

    @Override
    public InvoiceUploadResponse uploadInvoice(InvoiceUploadRequest request) {
        log.info("BlobService.uploadInvoice is starting with request: {}", request);
        if (client == null) throw new ApiException("BlobContainerClient is null.");

        log.info("Retrieving blob client for upload: {}", request.fileName());
        BlobClient blobClient = client.getBlobClient(request.fileName());

        log.info("Uploading content of length: {} to blob: {}", request.content().length(), request.fileName());

        try {
            blobClient.upload(new ByteArrayInputStream(request.content().getBytes(StandardCharsets.UTF_8)), request.content().length(), true);
        } catch (Exception e) {
            throw new InvoiceUploadFailureException("Error uploading invoice.", e);
        }
        log.info("BlobService.uploadInvoice is ending with blob URL: {}", blobClient.getBlobUrl());
        return new InvoiceUploadResponse(blobClient.getBlobUrl());

    }

    @Override
    public byte[] downloadInvoice(String blobName) {
        log.info("BlobService.downloadInvoice is starting with request: {}", blobName);
        if (client == null) throw new ApiException("BlobContainerClient is null.");

        log.info("Retrieving blob client for download: {}", blobName);
        BlobClient blobClient = client.getBlobClient(blobName);

        log.info("Checking blob existence for: {}", blobName);
        if (!blobClient.exists()) throw new InvoiceNotFoundException(String.format("Invoice %s does not exist", blobName));

        log.info("Downloading content for blob: {}", blobName);
        BinaryData binaryData = blobClient.downloadContent();

        log.info("BlobService.downloadInvoice is ending with content of length: {}", binaryData.toBytes().length);
        return binaryData.toBytes();
    }

}
