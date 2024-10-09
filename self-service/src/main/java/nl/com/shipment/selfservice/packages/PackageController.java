package nl.com.shipment.selfservice.packages;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import nl.com.shipment.pss.api.PackageApi;
import nl.com.shipment.pss.model.PackageDetail;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class PackageController implements PackageApi {

    private final PackageService packageService;

    @Override
    public ResponseEntity<List<Receiver>> getAllReceivers() {
        log.info("Getting all receivers");
        List<Receiver> receivers = packageService.getReceivers();
        if (receivers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(receivers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> submitPackage(PackageVO packageVO) {
        log.info("Submitting package");
        packageService.submitPackage(packageVO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PackageDetail> getPackage(String packageId){
        log.info("Getting package details for packageId: {}", packageId);
        PackageDetail packageDetails = packageService.getPackageDetails(packageId);
        if(packageDetails == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("Package details retrieved  for Id: {}", packageId);
        return new ResponseEntity<>(packageDetails, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<List<PackageDetail>> getPackagebySender(String senderEmployeeId, String status, Integer offset, Integer limit) {
        log.info("Getting packages by sender: {}, status : {}", senderEmployeeId, status);
        List<PackageDetail> packageDetails = packageService.getPackagesBySender(senderEmployeeId, status, offset, limit);
        if(packageDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("Packages retrieved sent by sender: {}", senderEmployeeId);
        return new ResponseEntity<>(packageDetails, HttpStatus.OK);
    }


}
