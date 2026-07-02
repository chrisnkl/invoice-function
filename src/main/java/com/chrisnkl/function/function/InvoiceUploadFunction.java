package com.chrisnkl.function.function;

import java.util.*;
import java.util.logging.Level;

import com.chrisnkl.function.domain.Headers;
import com.chrisnkl.function.domain.response.ApiResponse;
import com.chrisnkl.function.domain.response.InvoiceUploadResponse;
import com.chrisnkl.function.exception.InvoiceUploadFailureException;
import com.chrisnkl.function.config.ServiceFactory;
import com.chrisnkl.function.domain.request.InvoiceUploadRequest;
import com.chrisnkl.function.handler.UploadHandler;
import com.chrisnkl.function.util.Tracker;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class InvoiceUploadFunction {

    private final UploadHandler uploadHandler;

    public InvoiceUploadFunction() {
        this(ServiceFactory.getInvoiceUploadHandler());
    }

    public InvoiceUploadFunction(UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
    }

    @FunctionName("InvoiceUploadFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "invoices"
            ) HttpRequestMessage<String> request, final ExecutionContext context) {

        Tracker tracker = Tracker.create("upload-function-execution", context);
        try {

            // Retrieve the file name required from the specified header
            String fileName = request.getHeaders().getOrDefault(Headers.FILE_NAME.getName(), null);

            // Retrieve the content bytes
            String content = request.getBody();

            // Convert to upload request and let domain driven behavior validate the record
            InvoiceUploadRequest uploadRequest = InvoiceUploadRequest.create(fileName+".txt", content);

            context.getLogger().log(Level.INFO, "Invoice Upload Request: " + uploadRequest);

            // Build the upload response if successful
            Tracker trackerUpload = Tracker.create("upload-invoice", context);
            InvoiceUploadResponse response = uploadHandler.uploadInvoice(uploadRequest);
            trackerUpload.complete();

            return buildResponse(request, ApiResponse.created("Invoice uploaded successfully.", response));

        } catch (InvoiceUploadFailureException e) {
            context.getLogger().log(Level.SEVERE, "Failed to upload invoice", e);
            return buildResponse(request, ApiResponse.internalError("Failed to upload invoice."));
        } catch (Exception e) {
            context.getLogger().log(Level.SEVERE, "An unexpected error occurred.", e);
            return buildResponse(request, ApiResponse.internalError("An unexpected error occurred."));
        }
        finally {
            tracker.complete();
        }
    }

    public HttpResponseMessage buildResponse(HttpRequestMessage<String> request, ApiResponse<?> response) {
        return request.createResponseBuilder(HttpStatus.valueOf(response.status()))
                .header(Headers.CONTENT_TYPE.getName(), "application/json")
                .body(response)
                .build();
    }

}
