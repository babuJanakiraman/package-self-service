package nl.com.shipment.selfservice.common;

import nl.com.shipment.pss.model.Address;
import nl.com.shipment.pss.model.PackageDetail;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;
import nl.com.shipment.pss.shipping.model.ShippingOrder;
import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PackageServiceHelperTest {

    private PackageServiceHelper packageServiceHelper;

    @BeforeEach
    void setUp() {
        packageServiceHelper = new PackageServiceHelper();
    }

    @Test
    public void translateToShipping_shouldTranslateCorrectly() {
        PackageVO packageVO = new PackageVO("Package1", 2000.0, "10001", "20001");
        Receiver receiver = new Receiver("10001", "John", new Address("Street1", "3000GB"));

        ShippingOrder shippingOrder = packageServiceHelper.translateToShipping(packageVO, receiver);

        assertEquals("John", shippingOrder.getReceiverName());
        assertEquals("Street1", shippingOrder.getStreetName());
        assertEquals("3000GB", shippingOrder.getPostalCode());
        assertEquals("Package1", shippingOrder.getPackageName());
        assertEquals(ShippingOrder.PackageSizeEnum.L, shippingOrder.getPackageSize());
    }

    @Test
    public void translateToShipping_shouldCalculatePackageSizeCorrectly() {
        PackageVO smallPackage = new PackageVO("SmallPackage", 100.0, "10001", "20001");
        PackageVO mediumPackage = new PackageVO("MediumPackage", 500.0, "10001", "20001");
        PackageVO largePackage = new PackageVO("LargePackage", 5000.0, "10001", "20001");
        PackageVO extraLargePackage = new PackageVO("ExtraLargePackage", 15000.0, "10001", "20001");
        Receiver receiver = new Receiver("10001", "John", new Address("Street1", "3000GB"));

        assertEquals(ShippingOrder.PackageSizeEnum.S, packageServiceHelper.translateToShipping(smallPackage, receiver).getPackageSize());
        assertEquals(ShippingOrder.PackageSizeEnum.M, packageServiceHelper.translateToShipping(mediumPackage, receiver).getPackageSize());
        assertEquals(ShippingOrder.PackageSizeEnum.L, packageServiceHelper.translateToShipping(largePackage, receiver).getPackageSize());
        assertEquals(ShippingOrder.PackageSizeEnum.XL, packageServiceHelper.translateToShipping(extraLargePackage, receiver).getPackageSize());
    }

    @Test
    public void translateToPackageDetail_shouldTranslateCorrectly() throws ParseException {
        ShippingOrderDetails shippingOrderDetails = getShippingOrderDetails();
        shippingOrderDetails.setOrderStatus(ShippingOrderDetails.OrderStatusEnum.SENT);

        PackageDetail packageDetail = packageServiceHelper.translateToPackageDetail(shippingOrderDetails);

        assertEquals("20001", packageDetail.getPackageId());
        assertEquals("Package1", packageDetail.getPackageName());
        assertEquals(PackageDetail.PackageStatusEnum.SENT, packageDetail.getPackageStatus());
        assertEquals("2023-09-24", packageDetail.getDateOfRegistration());
        assertNull(packageDetail.getDateOfReceipt());
    }

    @Test
    public void translateToPackageDetail_shouldSetDateOfReceiptWhenDelivered() throws ParseException {
        ShippingOrderDetails shippingOrderDetails = getShippingOrderDetails();

        PackageDetail packageDetail = packageServiceHelper.translateToPackageDetail(shippingOrderDetails);

        assertEquals("20001", packageDetail.getPackageId());
        assertEquals("Package1", packageDetail.getPackageName());
        assertEquals(PackageDetail.PackageStatusEnum.DELIVERED, packageDetail.getPackageStatus());
        assertEquals("2023-10-01", packageDetail.getDateOfReceipt());
        assertEquals("2023-09-24", packageDetail.getDateOfRegistration());

    }

    private static ShippingOrderDetails getShippingOrderDetails() throws ParseException {
        ShippingOrderDetails shippingOrderDetails = new ShippingOrderDetails();
        shippingOrderDetails.setPackageId("20001");
        shippingOrderDetails.setPackageName("Package1");
        shippingOrderDetails.setOrderStatus(ShippingOrderDetails.OrderStatusEnum.DELIVERED);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date deliveryDate = dateFormat.parse("2023-10-01");
        shippingOrderDetails.setActualDeliveryDateTime(deliveryDate);
        shippingOrderDetails.setExpectedDeliveryDate(deliveryDate);
        return shippingOrderDetails;
    }
}