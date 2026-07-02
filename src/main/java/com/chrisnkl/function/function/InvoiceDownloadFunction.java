package com.chrisnkl.function.function;

import com.chrisnkl.function.config.ServiceFactory;
import com.chrisnkl.function.domain.Headers;
import com.chrisnkl.function.domain.response.ApiResponse;
import com.chrisnkl.function.exception.InvoiceDownloadFailureException;
import com.chrisnkl.function.exception.InvoiceNotFoundException;
import com.chrisnkl.function.handler.DownloadHandler;
import com.chrisnkl.function.util.Tracker;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
import java.util.logging.Level;

public class InvoiceDownloadFunction {

    private final DownloadHandler downloadHandler;

    public InvoiceDownloadFunction() {
        this(ServiceFactory.getInvoiceDownloadHandler());
    }

    public InvoiceDownloadFunction(DownloadHandler downloadHandler) {
        this.downloadHandler = downloadHandler;
    }

    @FunctionName("InvoiceDownloadFunction")
    public HttpResponseMessage run(@HttpTrigger(
            name = "req",
            authLevel = AuthorizationLevel.ANONYMOUS,
            methods = HttpMethod.GET,
            route = "invoices/{fileName}"
    ) HttpRequestMessage<Optional<String>> request, @BindingName("fileName") String fileName, final ExecutionContext context) {

        Tracker tracker = Tracker.create("download-function-execution", context);
        try {

            if (fileName == null || fileName.isBlank()) {
                context.getLogger().log(Level.SEVERE, "File Name is null or blank");
                return buildResponse(request, ApiResponse.badRequest("File Name is null or blank"));
            }

            Tracker downloadTracker = Tracker.create("download-invoice", context);
            byte[] content = downloadHandler.downloadInvoice(fileName);
            downloadTracker.complete();

            return buildInvoiceResponse(request, fileName, content);

        } catch (InvoiceNotFoundException e) {
            context.getLogger().log(Level.WARNING, "Invoice not found.", e);
            return buildResponse(request, ApiResponse.notFound("Invoice not found."));
        } catch (InvoiceDownloadFailureException e) {
            context.getLogger().log(Level.SEVERE, "Failed to download invoice", e);
            return buildResponse(request, ApiResponse.internalError("Failed to download invoice."));
        } catch (Exception e) {
            context.getLogger().log(Level.SEVERE, "An unexpected error occurred.", e);
            return buildResponse(request, ApiResponse.internalError("An unexpected error occurred."));
        } finally {
            tracker.complete();
        }
    }

    public HttpResponseMessage buildResponse(HttpRequestMessage<Optional<String>> request, ApiResponse<?> response) {
        return request.createResponseBuilder(HttpStatus.valueOf(response.status()))
                .header(Headers.CONTENT_TYPE.getName(), "application/json")
                .body(response)
                .build();
    }

    public HttpResponseMessage buildInvoiceResponse(HttpRequestMessage<Optional<String>> request, String fileName, byte[] content) {
        return request.createResponseBuilder(HttpStatus.OK)
                .header(Headers.CONTENT_TYPE.getName(), "text/plain; charset=utf-8")
                .header(Headers.CONTENT_DISPOSITION.getName(), "attachment; filename=\""+fileName+"\"")
                .body(content)
                .build();
    }


}
