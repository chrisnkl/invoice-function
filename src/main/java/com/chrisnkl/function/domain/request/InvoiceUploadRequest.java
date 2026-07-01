package com.chrisnkl.function.domain.request;

import com.chrisnkl.function.exception.ApiException;

public record InvoiceUploadRequest(

        String fileName,
        byte[] content

) {

    public InvoiceUploadRequest {
        if (fileName == null || fileName.isBlank()) throw new ApiException("File Name must not be null or empty.");
        if (content == null || content.length == 0) throw new ApiException("Content must not be null or empty.");
    }

    public static InvoiceUploadRequest create(String fileName, byte[] content) {
        return new InvoiceUploadRequest(fileName, content);
    }
}
