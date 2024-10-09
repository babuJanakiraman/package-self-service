package nl.com.shipment.shippingservice.shipping;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import nl.com.shipment.pss.shipping.api.ShippingApi;
import nl.com.shipment.pss.shipping.model.ShippingOrder;
import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Validated
@RestController
@Slf4j
public class ShippingOrdersController implements ShippingApi {

    private final ShippingOrdersService shippingOrdersService;

    public ShippingOrdersController(ShippingOrdersService shippingOrdersService) {
        this.shippingOrdersService = shippingOrdersService;
    }

    @Override
    public ResponseEntity<Void> orderPackage(@Valid @RequestBody ShippingOrder shippingOrder)  {
        log.info("Ordering package: {}", shippingOrder);
        shippingOrdersService.orderPackage(shippingOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ShippingOrderDetails>> listOrders(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit) {
        log.info("Listing orders with status: {}, offset: {}, limit: {}", status, offset, limit);
        List<ShippingOrderDetails> ShippingOrderDetailsList = shippingOrdersService.listOrders(status, offset, limit);
        return new ResponseEntity<>(ShippingOrderDetailsList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ShippingOrderDetails> getOrderDetails(String orderId) {
        log.info("Getting order details for order: {}", orderId);
        ShippingOrderDetails shippingOrderDetails = shippingOrdersService.getOrderDetails(orderId);
        return new ResponseEntity<>(shippingOrderDetails, HttpStatus.OK);
    }




}
