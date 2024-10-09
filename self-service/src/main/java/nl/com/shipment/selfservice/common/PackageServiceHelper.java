package nl.com.shipment.selfservice.common;

import lombok.extern.slf4j.Slf4j;
import nl.com.shipment.pss.model.PackageDetail;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;
import nl.com.shipment.pss.shipping.model.ShippingOrder;
import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class PackageServiceHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ShippingOrder translateToShipping(PackageVO packageVO, Receiver receiver) {
        return new ShippingOrder()
                .packageSize(ShippingOrder.PackageSizeEnum.valueOf(calculatePackageSize(packageVO)))
                .receiverName(receiver.getName())
                .streetName(receiver.getAddress().getStreetName())
                .postalCode(receiver.getAddress().getPostalCode())
                .packageName(packageVO.getPackageName());
    }

    private String calculatePackageSize(PackageVO packageVO) {
        final BigDecimal weightInKg = BigDecimal.valueOf((packageVO.getWeight() / 1000));

        if (weightInKg.compareTo(BigDecimal.valueOf(0.2)) < 0) {
            return ShippingOrderDetails.PackageSizeEnum.S.getValue();
        } else if (weightInKg.compareTo(BigDecimal.valueOf(1)) < 0) {
            return ShippingOrderDetails.PackageSizeEnum.M.getValue();
        } else if (weightInKg.compareTo(BigDecimal.valueOf(10)) < 0) {
            return ShippingOrderDetails.PackageSizeEnum.L.getValue();
        } else {
            return ShippingOrderDetails.PackageSizeEnum.XL.getValue();
        }
    }

    public PackageDetail translateToPackageDetail(ShippingOrderDetails shippingOrderDetails) {
        PackageDetail packageDetail = new PackageDetail();
        packageDetail.setPackageId(shippingOrderDetails.getPackageId());
        packageDetail.setPackageName(shippingOrderDetails.getPackageName());
        packageDetail.setPackageStatus(PackageDetail.PackageStatusEnum.valueOf(shippingOrderDetails.getOrderStatus().getValue()));
        if (shippingOrderDetails.getOrderStatus().equals(ShippingOrderDetails.OrderStatusEnum.DELIVERED)) {
            String formattedDate = sdf.format(shippingOrderDetails.getActualDeliveryDateTime());
            packageDetail.setDateOfReceipt(formattedDate);
        }
        packageDetail.setDateOfRegistration(sdf.format(calculateDateOfReceipt(shippingOrderDetails.getExpectedDeliveryDate())));

        return packageDetail;
    }

    private Date calculateDateOfReceipt(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }

}