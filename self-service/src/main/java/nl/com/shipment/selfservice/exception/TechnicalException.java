package nl.com.shipment.selfservice.exception;

import lombok.Getter;
import nl.com.shipment.pss.model.Error;

import java.util.List;

@Getter
public class TechnicalException extends RuntimeException {
    private final int status;
    private final String message;
    private final List<Error> errors;

    public TechnicalException(int status, String message, List<Error> errors) {
        super(message);
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

}