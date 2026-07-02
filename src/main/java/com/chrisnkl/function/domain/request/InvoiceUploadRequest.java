package com.chrisnkl.function.domain.request;

import com.chrisnkl.function.exception.ApiException;

public record InvoiceUploadRequest(

        String fileName,
        String content

) {

    public InvoiceUploadRequest {
        if (fileName == null || fileName.isBlank()) throw new ApiException("File Name must not be null or empty.");
        if (content == null || content.isBlank()) throw new ApiException("Content must not be null or empty.");
        fileName = fileName.endsWith(".txt") ? fileName : fileName + ".txt";
    }

    public static InvoiceUploadRequest create(String fileName, String content) {
        return new InvoiceUploadRequest(fileName, content);
    }
}
