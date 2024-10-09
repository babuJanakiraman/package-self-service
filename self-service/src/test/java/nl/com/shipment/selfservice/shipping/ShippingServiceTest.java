package nl.com.shipment.selfservice.shipping;

import nl.com.shipment.pss.shipping.model.ShippingOrder;

import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import nl.com.shipment.selfservice.common.RestClient;
import nl.com.shipment.selfservice.common.ShippingConnectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ShippingConnectionProperties shippingConnectionProperties;

    @InjectMocks
    private ShippingService shippingService;

    @BeforeEach
    void setUp() {
        when(shippingConnectionProperties.getUrl()).thenReturn("http://localhost:8100");
    }

    @Test
    public void createShippingOrder_sendsCorrectRequest() {
        ShippingOrder shippingOrder = new ShippingOrder();
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .toUriString();

        shippingService.createShippingOrder(shippingOrder);

        verify(restClient).sendRequest(eq(expectedUrl), eq(HttpMethod.POST), eq(shippingOrder), eq(new ParameterizedTypeReference<>() {}));
    }
    @Test
    public void createShippingOrder_throwsExceptionWhenRestClientFails() {
        ShippingOrder shippingOrder = new ShippingOrder();
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .toUriString();

        doThrow(new RuntimeException("RestClient error")).when(restClient).sendRequest(eq(expectedUrl), eq(HttpMethod.POST), eq(shippingOrder), eq(new ParameterizedTypeReference<>() {}));

        assertThatThrownBy(() -> shippingService.createShippingOrder(shippingOrder))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("RestClient error");
    }

    @Test
    public void getShippingOrderByOrderId_sendsCorrectRequest() {
        String orderId = "12345";
        ShippingOrderDetails expectedDetails = new ShippingOrderDetails();
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .pathSegment(orderId)
                .toUriString();

        when(restClient.sendRequest(eq(expectedUrl), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<ShippingOrderDetails>() {})))
                .thenReturn(expectedDetails);

        ShippingOrderDetails actualDetails = shippingService.getShippingOrderByOrderId(orderId);

        assertThat(actualDetails).isEqualTo(expectedDetails);
    }

    @Test
    public void getShippingOrderByOrderId_returnsNullWhenOrderIdIsInvalid() {
        String invalidOrderId = "invalidOrderId";
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .pathSegment(invalidOrderId)
                .toUriString();

        when(restClient.sendRequest(eq(expectedUrl), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<ShippingOrderDetails>() {})))
                .thenReturn(null);

        ShippingOrderDetails actualDetails = shippingService.getShippingOrderByOrderId(invalidOrderId);

        assertThat(actualDetails).isNull();
    }

    @Test
    public void listOrders_sendsCorrectRequest() {
        String status = "SENT";
        Integer offset = 0;
        Integer limit = 10;
        List<ShippingOrderDetails> expectedDetails = List.of(new ShippingOrderDetails());
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .queryParam("status", status)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .toUriString();

        when(restClient.sendRequest(eq(expectedUrl), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<List<ShippingOrderDetails>>() {})))
                .thenReturn(expectedDetails);

        List<ShippingOrderDetails> actualDetails = shippingService.listOrders(status, offset, limit);

        assertThat(actualDetails).isEqualTo(expectedDetails);
    }

    @Test
    public void getShippingOrderByOrderId_throwsExceptionWhenRestClientFails() {
        String orderId = "12345";
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .pathSegment(orderId)
                .toUriString();

        when(restClient.sendRequest(eq(expectedUrl), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<ShippingOrderDetails>() {})))
                .thenThrow(new RuntimeException("RestClient error"));

        assertThatThrownBy(() -> shippingService.getShippingOrderByOrderId(orderId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("RestClient error");
    }

    @Test
    public void listOrders_returnsEmptyListWhenParametersAreInvalid() {
        String status = "invalidStatus";
        Integer offset = -1;
        Integer limit = -1;
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .queryParam("status", status)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .toUriString();

        when(restClient.sendRequest(eq(expectedUrl), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<List<ShippingOrderDetails>>() {})))
                .thenReturn(List.of());

        List<ShippingOrderDetails> actualDetails = shippingService.listOrders(status, offset, limit);

        assertThat(actualDetails).isEmpty();
    }

    @Test
    public void listOrders_throwsExceptionWhenRestClientFails() {
        String status = "DELIVERED";
        Integer offset = 0;
        Integer limit = 10;
        String expectedUrl = UriComponentsBuilder.fromHttpUrl("http://localhost:8100")
                .path("/shippingOrders")
                .queryParam("status", status)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .toUriString();

        when(restClient.sendRequest(eq(expectedUrl), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<List<ShippingOrderDetails>>() {})))
                .thenThrow(new RuntimeException("RestClient error"));

        assertThatThrownBy(() -> shippingService.listOrders(status, offset, limit))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("RestClient error");
    }
}