package nl.com.shipment.shippingservice.exception;

public class RedundantPackageException extends RuntimeException {
    public RedundantPackageException(String message) {
        super(message);
    }
}
