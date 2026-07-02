package com.chrisnkl.function.domain.request;

import com.chrisnkl.function.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
class InvoiceUploadRequestTest {

    @Test
    void testValidRequest() {

        // Arrange
        final String fileName = "fileName";
        final String content = "content";

        // Act
        InvoiceUploadRequest request = new InvoiceUploadRequest(fileName, content);

        // Assert
        assertEquals(fileName, request.fileName(), () -> "The filename does not match");
        assertEquals(content, request.content(), () -> "The content does not match");

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "    "})
    void testInvalidRequest_whenInvalidFileName_throwsException(String fileName) {

        final String content = "content";

        assertThrows(ApiException.class, () -> new InvoiceUploadRequest(fileName, content));

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "    "})
    void testInvalidRequest_whenInvalidContent_throwsException(String content) {

        final String fileName = "fileName";

        assertThrows(ApiException.class, () -> new InvoiceUploadRequest(fileName, content));

    }

    @Test
    void testFactoryMethod() {

        final String fileName = "fileName";
        final String content = "content";

        InvoiceUploadRequest request = InvoiceUploadRequest.create(fileName, content);

        assertEquals(fileName, request.fileName(), () -> "The filename does not match");
        assertEquals(content, request.content(), () -> "The content does not match");

    }


}