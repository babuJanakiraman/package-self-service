package nl.com.shipment.selfservice.packages;

import nl.com.shipment.pss.model.PackageDetail;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;
import nl.com.shipment.pss.shipping.model.ShippingOrder;
import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import nl.com.shipment.selfservice.common.PackageServiceHelper;
import nl.com.shipment.selfservice.common.SelfServiceUtil;
import nl.com.shipment.selfservice.exception.ReceiverNotFoundException;
import nl.com.shipment.selfservice.shipping.ShippingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock
    private ShippingService shippingService;

    @Mock
    private PackageServiceHelper packageServiceHelper;

    @InjectMocks
    private PackageService packageService;

    private PackageVO packageVO;
    private Receiver receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        packageService = new PackageService(shippingService, packageServiceHelper);
        packageVO = new PackageVO();
        packageVO.setReceiverId("receiverId");

        receiver = new Receiver();
        receiver.setEmployeeId("receiverId");
    }

    @Test
    public void getReceivers_returnsExpectedReceivers() {
        List<Receiver> expectedReceivers = List.of(
                new Receiver() {{ setEmployeeId("receiverId1"); }},
                new Receiver() {{ setEmployeeId("receiverId2"); }}
        );
        try (MockedStatic<SelfServiceUtil> utilities = mockStatic(SelfServiceUtil.class)) {
            utilities.when(SelfServiceUtil::getReceivers).thenReturn(expectedReceivers);

            List<Receiver> actualReceivers = packageService.getReceivers();

            assertThat(actualReceivers).isEqualTo(expectedReceivers);
        }
    }

    @Test
    public void submitPackage_success() {
        try (MockedStatic<SelfServiceUtil> utilities = Mockito.mockStatic(SelfServiceUtil.class)) {
            utilities.when(SelfServiceUtil::getReceivers).thenReturn(List.of(receiver));
            when(packageServiceHelper.translateToShipping(any(PackageVO.class), any(Receiver.class))).thenReturn(new ShippingOrder());

            packageService.submitPackage(packageVO);

            verify(shippingService, times(1)).createShippingOrder(any(ShippingOrder.class));
        }
    }

    @Test
    public void submitPackage_receiverNotFound() {
        try (MockedStatic<SelfServiceUtil> utilities = Mockito.mockStatic(SelfServiceUtil.class)) {
            utilities.when(SelfServiceUtil::getReceivers).thenReturn(List.of());

            assertThrows(ReceiverNotFoundException.class, () -> packageService.submitPackage(packageVO));

            verify(shippingService, never()).createShippingOrder(any(ShippingOrder.class));
        }
    }

    @Test
    public void getPackageDetails_returnsExpectedPackageDetail() {
        String packageId = "testPackageId";
        ShippingOrderDetails shippingOrderDetails = new ShippingOrderDetails();
        PackageDetail expectedPackageDetail = new PackageDetail();

        when(shippingService.getShippingOrderByOrderId(anyString())).thenReturn(shippingOrderDetails);
        when(packageServiceHelper.translateToPackageDetail(shippingOrderDetails)).thenReturn(expectedPackageDetail);

        PackageDetail actualPackageDetail = packageService.getPackageDetails(packageId);

        assertThat(actualPackageDetail).isEqualTo(expectedPackageDetail);
    }
}