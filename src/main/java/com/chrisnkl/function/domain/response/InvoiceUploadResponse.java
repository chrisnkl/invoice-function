package com.chrisnkl.function.domain.response;

import com.chrisnkl.function.exception.ApiException;

public record InvoiceUploadResponse(

        String blobUrl

) {

    public InvoiceUploadResponse {
        if (blobUrl == null || blobUrl.isBlank()) throw new ApiException("Blob url must not be null or empty.");
    }

}
