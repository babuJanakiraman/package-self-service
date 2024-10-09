package nl.com.shipment.selfservice.common;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestClient {

    private static final String SHIPPING_SERVICE = "shipping-service";

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = SHIPPING_SERVICE, fallbackMethod = "fallback")
    public <T> T sendRequest(String uri, HttpMethod method, Object data, ParameterizedTypeReference<T> typeReference) {
        log.debug("Sending request to: {}, method: {}, RequestData: {}", uri, method, data);
        ResponseEntity<T> response = restTemplate.exchange(uri, method, new HttpEntity<>(data), typeReference);
        log.debug("Response received: {}", response.getBody());
        return response.getBody();
    }

    public <T> T fallback(String uri, HttpMethod method, Object data, ParameterizedTypeReference<T> typeReference, Throwable throwable) {
        log.error("Fallback method called due to: {}", throwable.getMessage());
        return null;
    }
}