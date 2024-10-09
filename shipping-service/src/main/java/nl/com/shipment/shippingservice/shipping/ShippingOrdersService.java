package nl.com.shipment.shippingservice.shipping;

import lombok.extern.slf4j.Slf4j;

import nl.com.shipment.pss.shipping.model.ShippingOrder;
import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import nl.com.shipment.shippingservice.exception.PackageNotFoundException;
import nl.com.shipment.shippingservice.exception.RedundantPackageException;
import nl.com.shipment.shippingservice.util.PackageServiceUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ShippingOrdersService {

    private static final List<ShippingOrder> shippingOrderList = new ArrayList<>();

    public void orderPackage(ShippingOrder shippingOrder)  {
        PackageServiceUtil.getShippingOrderDetailsList().forEach(shippingOrderDetails -> {
            if(shippingOrderDetails.getPackageName().equals(shippingOrder.getPackageName())){
                throw new RedundantPackageException("The selected packageName was already taken.");
            }
        });
        shippingOrderList.stream().forEach(s -> {
            if(s.getPackageName().equals(shippingOrder.getPackageName())){
                throw new RedundantPackageException("The selected packageName was already taken.");
            }
        });
        shippingOrderList.add(shippingOrder);
    }

    public ShippingOrderDetails getOrderDetails(String orderId) {
        ShippingOrderDetails shippingOrderDetails = PackageServiceUtil.getShippingOrderDetailsList().stream()
                .filter(order -> order.getPackageId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Package not found for given Id: {}", orderId );
                    return new PackageNotFoundException("Package not found!");
        });
        return shippingOrderDetails;
    }

    public List<ShippingOrderDetails> listOrders(String status, Integer offset, Integer limit)  {
        if (status != null) {
            try {
                ShippingOrderDetails.OrderStatusEnum statusEnum = ShippingOrderDetails.OrderStatusEnum.valueOf(status.toUpperCase());
                return PackageServiceUtil.getShippingOrderDetailsList().stream()
                        .filter(shippingOrderDetails -> shippingOrderDetails.getOrderStatus().equals(statusEnum))
                        .skip(offset != null ? offset : 0)
                        .limit(limit != null ? limit : Long.MAX_VALUE)
                        .toList();
            } catch (IllegalArgumentException e) {
                log.error("Invalid status: {}", status);
            }
        }
        return PackageServiceUtil.getShippingOrderDetailsList().stream()
                .skip(offset != null ? offset : 0)
                .limit(limit != null ? limit : Long.MAX_VALUE)
                .toList();
    }


}
