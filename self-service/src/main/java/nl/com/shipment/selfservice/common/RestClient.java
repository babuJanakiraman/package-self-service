package nl.com.shipment.selfservice.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import nl.com.shipment.pss.model.Error;
import nl.com.shipment.selfservice.exception.TechnicalException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RestClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SHIPPING_SERVICE = "shipping-service";

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    
    @CircuitBreaker(name = SHIPPING_SERVICE, fallbackMethod = "fallback")
    public <T> T sendRequest(String uri, HttpMethod method, Object data, ParameterizedTypeReference<T> typeReference) {
        try {
            log.info("Sending request to: {}, method: {}, RequestData: {}", uri, method, data);
            ResponseEntity<T> response = restTemplate.exchange(uri, method, new HttpEntity<>(data), typeReference);
            log.debug("Response received: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            TechnicalException technicalException = parseErrorResponse(ex.getResponseBodyAsString());
            log.error("Error occurred while sending request to: {}, method: {}, RequestData: {}. Exception: {}", uri, method, data, technicalException.getMessage());
            throw technicalException;
        } catch (Exception ex) {
            log.error("Unexpected error occurred while sending request to: {}, method: {}, RequestData: {}. Exception: {}", uri, method, data, ex.getMessage());
            throw ex;
        }
    }

    public <T> T fallback(String uri, HttpMethod method, Object data, ParameterizedTypeReference<T> typeReference, Throwable throwable) {
        log.error("Fallback method called due to: {}", throwable.getMessage());
        return null;
    }

    private TechnicalException parseErrorResponse(String responseBody) {
        try {
            JsonNode rootNode = OBJECT_MAPPER.readTree(responseBody);
            int status = rootNode.path("status").asInt();
            String message = rootNode.path("message").asText();
            List<Error> errors = new ArrayList<>();

            rootNode.path("errors").forEach(errorNode -> {
                Error error = new Error();
                error.setField(errorNode.path("field").asText());
                error.setMessage(errorNode.path("message").asText());
                errors.add(error);
            });

            return new TechnicalException(status, message, errors);
        } catch (Exception e) {
            log.error("Error parsing response body: {}", e.getMessage());
            Error internalError = new Error();
            internalError.setField("Internal");
            internalError.setMessage("Error parsing response body");
            return new TechnicalException(500, "Internal Server Error", List.of(internalError));
        }
    }
}
