package com.chrisnkl.function.function;

import com.chrisnkl.function.config.ApplicationSetting;
import com.chrisnkl.function.config.ServiceFactory;
import com.chrisnkl.function.exception.InvoiceDownloadFailureException;
import com.chrisnkl.function.exception.InvoiceNotFoundException;
import com.chrisnkl.function.handler.DownloadHandler;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.mockito.MockedStatic;

class InvoiceDownloadFunctionTest {


    @Mock
    private DownloadHandler downloadHandler;

    @Mock
    private HttpRequestMessage<Optional<String>> request;

    @Mock
    private HttpResponseMessage.Builder responseBuilder;

    @Mock
    private HttpResponseMessage response;

    @Mock
    private ExecutionContext executionContext;

    @Mock
    private Logger logger;

    private InvoiceDownloadFunction invoiceDownloadFunction;

    @BeforeEach
    void setUp() {

        System.setProperty(ApplicationSetting.STORAGE_ACCOUNT_NAME.name(), "dummy-account-name");
        System.setProperty(ApplicationSetting.INVOICES_CONTAINER_NAME.name(), "dummy-container-name");

        MockitoAnnotations.openMocks(this);
        invoiceDownloadFunction = new InvoiceDownloadFunction(downloadHandler);
    }

    @Test
    void testConstructorNoArg() {
        try (MockedStatic<ServiceFactory> mock = Mockito.mockStatic(ServiceFactory.class)) {
            DownloadHandler mockHandler = mock(DownloadHandler.class);
            mock.when(ServiceFactory::getInvoiceDownloadHandler).thenReturn(mockHandler);
            InvoiceDownloadFunction function = new InvoiceDownloadFunction();

            assertNotNull(function);
        }
    }

    @Test
    void testFunction_whenSuccessful_returnOk() {

        // Arrange
        String fileName = "fileName";

        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK.value());
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceDownloadFunction.run(request, fileName, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());

    }

    @Test
    void testFunction_whenInvoiceNotFound_returnNotFound() {

        // Arrange
        String fileName = "fileName";

        doThrow(new InvoiceNotFoundException("Invoice not found.")).when(downloadHandler).downloadInvoice(fileName);
        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND.value());
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceDownloadFunction.run(request, fileName, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(404, response.getStatusCode());

    }

    @Test
    void testFunction_whenInvoiceFails_returnInternalServerError() {

        // Arrange
        String fileName = "fileName";

        doThrow(new InvoiceDownloadFailureException("Failed to download invoice")).when(downloadHandler).downloadInvoice(fileName);
        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceDownloadFunction.run(request, fileName, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());

    }

    @Test
    void testFunction_whenInvoiceFailsOtherError_returnInternalServerError() {

        // Arrange
        String fileName = "fileName";

        doThrow(new RuntimeException("An unexpected error occurred.")).when(downloadHandler).downloadInvoice(fileName);
        when(responseBuilder.header(anyString(), anyString())).thenReturn(responseBuilder);
        when(responseBuilder.body(any())).thenReturn(responseBuilder);
        when(request.createResponseBuilder(any(HttpStatus.class))).thenReturn(responseBuilder);
        when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        when(responseBuilder.build()).thenReturn(response);
        when(executionContext.getLogger()).thenReturn(logger);

        // Act
        HttpResponseMessage response = invoiceDownloadFunction.run(request, fileName, executionContext);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());

    }


}