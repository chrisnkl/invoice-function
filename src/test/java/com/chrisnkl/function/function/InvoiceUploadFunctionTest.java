package com.chrisnkl.function.function;

import com.chrisnkl.function.config.ApplicationSetting;
import com.chrisnkl.function.config.ServiceFactory;
import com.chrisnkl.function.domain.Headers;
import com.chrisnkl.function.domain.request.InvoiceUploadRequest;
import com.chrisnkl.function.domain.response.ApiResponse;
import com.chrisnkl.function.domain.response.InvoiceUploadResponse;
import com.chrisnkl.function.exception.InvoiceUploadFailureException;
import com.chrisnkl.function.handler.UploadHandler;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InvoiceUploadFunctionTest {

    @Mock
    private UploadHandler uploadHandler;

    @Mock
    private HttpRequestMessage<String> request;

    @Mock
    private HttpResponseMessage.Builder responseBuilder;

    @Mock
    private HttpResponseMessage response;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private Logger logger;

    private InvoiceUploadFunction invoiceUploadFunction;

    @BeforeEach
    void setUp() {

        System.setProperty(ApplicationSetting.STORAGE_ACCOUNT_NAME.name(), "dummy-account-name");
        System.setProperty(ApplicationSetting.INVOICES_CONTAINER_NAME.name(), "dummy-container-name");

        MockitoAnnotations.initMocks(this);
        invoiceUploadFunction = new InvoiceUploadFunction(uploadHandler);
    }

    @Test
    void testConstructorNoArg() {
        try (MockedStatic<ServiceFactory> mock = Mockito.mockStatic(ServiceFactory.class)) {
            UploadHandler mockHandler = mock(UploadHandler.class);
            mock.when(ServiceFactory::getInvoiceUploadHandler).thenReturn(mockHandler);

            InvoiceUploadFunction function = new InvoiceUploadFunction();

            assertNotNull(function);
        }
    }

    @Test
    void testFunction_whenSuccessful_returnCreated() {

        // Arrange
        String fileName = "fileName";
        String content = "content";
        InvoiceUploadResponse uploadResponse = new InvoiceUploadResponse("blobUrl");
        ApiResponse<InvoiceUploadResponse> createdResponse = ApiResponse.created("Invoice uploaded successfully.", uploadResponse);

        when(request.getHeaders()).thenReturn(Map.of(Headers.FILE_NAME.getName(), fileName));
        when(request.getBody()).thenReturn(content);
        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.CREATED.value());
        when(response.getBody()).thenReturn(createdResponse);
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceUploadFunction.run(request, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertEquals("Invoice uploaded successfully.", ((ApiResponse<InvoiceUploadResponse>) response.getBody()).message());
        assertEquals("blobUrl", ((ApiResponse<InvoiceUploadResponse>) response.getBody()).data().blobUrl());

    }

    @Test
    void testFunction_whenUploadFails_returnInternalServerError() {

        // Arrange
        String fileName = "fileName";
        String content = "content";
        InvoiceUploadRequest uploadRequest = InvoiceUploadRequest.create(fileName, content);

        doThrow(new InvoiceUploadFailureException("Error uploading invoice.")).when(uploadHandler).uploadInvoice(uploadRequest);
        when(request.getHeaders()).thenReturn(Map.of(Headers.FILE_NAME.getName(), fileName));
        when(request.getBody()).thenReturn(content);
        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceUploadFunction.run(request, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());

    }

    @Test
    void testFunction_whenUploadFailsOther_returnInternalServerError() {

        // Arrange
        String fileName = "fileName";
        String content = "content";
        InvoiceUploadRequest uploadRequest = InvoiceUploadRequest.create(fileName, content);

        doThrow(new RuntimeException("Internal Server Error.")).when(uploadHandler).uploadInvoice(uploadRequest);
        when(request.getHeaders()).thenReturn(Map.of(Headers.FILE_NAME.getName(), fileName));
        when(request.getBody()).thenReturn(content);
        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceUploadFunction.run(request, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());

    }


}