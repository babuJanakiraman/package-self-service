package nl.com.shipment.selfservice.exception.handler;


import lombok.extern.slf4j.Slf4j;
import nl.com.shipment.selfservice.exception.ReceiverNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import nl.com.shipment.pss.model.Error;
import nl.com.shipment.pss.model.ErrorResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "An unexpected error occurred on the server.";



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setErrorMessage(ex.getBody().getDetail());
        List<Error> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Error error = new Error();
            error.setField(fieldError.getField());
            error.setMessage(fieldError.getField()+ " " + fieldError.getDefaultMessage());
            errors.add(error);

        });
        errorResponse.setErrors(errors);
        log.error("Method argument not valid :{}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setErrorMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(ReceiverNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReceiverNotFoundException(ReceiverNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setErrorMessage(ex.getMessage());
        log.error("Receiver not found : {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(400);
        errorResponse.setErrorMessage(ex.getMessage());
        log.error("Receiver not found : {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(500);
        errorResponse.setErrorMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        log.error("Internal server error :{}", ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }



}
