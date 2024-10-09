package nl.com.shipment.shippingservice.exception.handler;

import nl.com.shipment.pss.shipping.model.Error;
import nl.com.shipment.pss.shipping.model.ValidationError;
import nl.com.shipment.shippingservice.exception.PackageNotFoundException;
import nl.com.shipment.shippingservice.exception.RedundantPackageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Error error = new Error();
        error.setStatus(400);
        error.setMessage("Validation failed");
        List<ValidationError> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            ValidationError validationError = new ValidationError();
            validationError.setField(fieldError.getField());
            validationError.setMessage(fieldError.getField()+ " " + fieldError.getDefaultMessage());
            errors.add(validationError);

        });
        error.setErrors(errors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RedundantPackageException.class)
    public ResponseEntity<Error> handleRedundantPackageException(RedundantPackageException ex) {
        Error error = new Error();
        error.setStatus(409);
        error.setMessage(ex.getMessage());
        ValidationError validationError = new ValidationError();
        validationError.setField("packageName");
        validationError.setMessage(ex.getMessage());
        List<ValidationError> errors = List.of(validationError);
        error.setErrors(errors);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(PackageNotFoundException.class)
    public ResponseEntity<Error> handlePackageNotFoundException(PackageNotFoundException ex) {
        Error error = new Error();
        error.setStatus(500);
        error.setMessage(ex.getMessage());
        ValidationError validationError = new ValidationError();
        validationError.setField("PackageId");
        validationError.setMessage(ex.getMessage());
        List<ValidationError> errors = List.of(validationError);
        error.setErrors(errors);
        return ResponseEntity.internalServerError().body(error);
    }

}
