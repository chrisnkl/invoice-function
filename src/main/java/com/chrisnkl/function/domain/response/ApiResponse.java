package com.chrisnkl.function.domain.response;

import com.microsoft.azure.functions.HttpStatus;

import java.time.Instant;

public record ApiResponse<T>(

        String timestamp,
        int status,
        String message,
        T data

) {

    private ApiResponse(int status, String message) {
        this(Instant.now().toString(), status, message, null);
    }

    private ApiResponse(int status, String message, T data) {
        this(Instant.now().toString(), status, message, data);
    }

    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<T>(HttpStatus.OK.value(), message);
    }

    public static <T> ApiResponse<T> created(String message) {
        return new ApiResponse<T>(HttpStatus.CREATED.value(), message);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<T>(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<T>(HttpStatus.NOT_FOUND.value(), message);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<T>(HttpStatus.OK.value(), message, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<T>(HttpStatus.CREATED.value(), message, data);
    }

    public static <T> ApiResponse<T> badRequest(String message, T data) {
        return new ApiResponse<T>(HttpStatus.BAD_REQUEST.value(), message, data);
    }

    public static <T> ApiResponse<T> internalError(String message, T data) {
        return new ApiResponse<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    public static <T> ApiResponse<T> notFound(String message, T data) {
        return new ApiResponse<T>(HttpStatus.NOT_FOUND.value(), message, data);
    }


}
