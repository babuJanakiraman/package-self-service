package nl.com.shipment.selfservice.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RestClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<Object> responseEntity;

    @InjectMocks
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendRequest_nullData_success() {
        String uri = "http://localhost:8100";
        HttpMethod method = HttpMethod.GET;
        Object data = null;
        ParameterizedTypeReference<Object> typeReference = new ParameterizedTypeReference<>() {};

        when(restTemplate.exchange(eq(uri), eq(method), any(HttpEntity.class), eq(typeReference)))
                .thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(new Object());

        Object result = restClient.sendRequest(uri, method, data, typeReference);

        assertNotNull(result);
        verify(restTemplate, times(1)).exchange(eq(uri), eq(method), any(HttpEntity.class), eq(typeReference));
    }


}