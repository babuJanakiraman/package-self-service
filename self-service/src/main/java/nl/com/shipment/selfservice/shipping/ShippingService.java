package nl.com.shipment.selfservice.shipping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.com.shipment.pss.shipping.model.ShippingOrder;
import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import nl.com.shipment.selfservice.common.RestClient;
import nl.com.shipment.selfservice.common.ShippingConnectionProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class ShippingService {
    private static final String SHIPPING_ORDERS = "/shippingOrders";

    private final ShippingConnectionProperties shippingConnectionProperties;
    private final RestClient restClient;

    public void createShippingOrder(ShippingOrder shippingOrder) {
        log.debug("Creating shipping order: {}", shippingOrder);
        String url = UriComponentsBuilder.fromHttpUrl(shippingConnectionProperties.getUrl())
                .path(SHIPPING_ORDERS)
                .toUriString();
        restClient.sendRequest(url, HttpMethod.POST, shippingOrder, new ParameterizedTypeReference<>() {
        });
    }

    public ShippingOrderDetails getShippingOrderByOrderId(String orderId) {
        String url = UriComponentsBuilder.fromHttpUrl(shippingConnectionProperties.getUrl())
                .path(SHIPPING_ORDERS)
                .pathSegment(orderId)
                .toUriString();
        return restClient.sendRequest(url, HttpMethod.GET, null, new ParameterizedTypeReference<ShippingOrderDetails>() {});
    }


    public List<ShippingOrderDetails> listOrders(String status, Integer offset, Integer limit) {
        String url = UriComponentsBuilder.fromHttpUrl(shippingConnectionProperties.getUrl())
                .path(SHIPPING_ORDERS)
                .queryParam("status", status)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .toUriString();
        return restClient.sendRequest(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShippingOrderDetails>>() {});
    }
    public List<ShippingOrderDetails> listOrders(Integer offset, Integer limit) {

        String url = UriComponentsBuilder.fromHttpUrl(shippingConnectionProperties.getUrl())
                .path(SHIPPING_ORDERS)
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .toUriString();
        return restClient.sendRequest(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShippingOrderDetails>>() {});
    }
}
