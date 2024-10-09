package nl.com.shipment.selfservice.packages;

import nl.com.shipment.pss.model.Address;
import nl.com.shipment.pss.model.PackageDetail;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PackageControllerTest {

    @Mock
    private PackageService packageService;

    @InjectMocks
    private PackageController packageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllReceivers_shouldReturnReceivers() {
        List<Receiver> receivers = List.of(new Receiver("10001", "John", new Address("Street1", "3000GB")));
        when(packageService.getReceivers()).thenReturn(receivers);

        ResponseEntity<List<Receiver>> response = packageController.getAllReceivers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(receivers, response.getBody());
    }

    @Test
    public void getAllReceivers_shouldReturnNoContentWhenEmpty() {
        when(packageService.getReceivers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Receiver>> response = packageController.getAllReceivers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void submitPackage_shouldReturnCreated() {
        PackageVO packageVO = new PackageVO("Package1", 2000.0, "10001", "20001");

        ResponseEntity<Void> response = packageController.submitPackage(packageVO);

        verify(packageService, times(1)).submitPackage(packageVO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void getPackage_shouldReturnPackageDetails() {
        String packageId = "20001";
        PackageDetail packageDetail = new PackageDetail();
        when(packageService.getPackageDetails(packageId)).thenReturn(packageDetail);

        ResponseEntity<PackageDetail> response = packageController.getPackage(packageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packageDetail, response.getBody());
    }

    @Test
    public void getPackage_shouldReturnNoContentWhenNull() {
        String packageId = "20001";
        when(packageService.getPackageDetails(packageId)).thenReturn(null);

        ResponseEntity<PackageDetail> response = packageController.getPackage(packageId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getPackagebySender_shouldReturnPackageDetails() {
        String senderEmployeeId = "10001";
        String status = "DELIVERED";
        Integer offset = 0;
        Integer limit = 10;
        List<PackageDetail> packageDetails = List.of(new PackageDetail());
        when(packageService.getPackagesBySender(senderEmployeeId, status, offset, limit)).thenReturn(packageDetails);

        ResponseEntity<List<PackageDetail>> response = packageController.getPackagebySender(senderEmployeeId, status, offset, limit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packageDetails, response.getBody());
    }

    @Test
    public void getPackagebySender_shouldReturnNoContentWhenEmpty() {
        String senderEmployeeId = "10001";
        String status = "DELIVERED";
        Integer offset = 0;
        Integer limit = 10;
        when(packageService.getPackagesBySender(senderEmployeeId, status, offset, limit)).thenReturn(Collections.emptyList());

        ResponseEntity<List<PackageDetail>> response = packageController.getPackagebySender(senderEmployeeId, status, offset, limit);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}