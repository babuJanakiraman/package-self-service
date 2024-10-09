package nl.com.shipment.selfservice.packages;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import nl.com.shipment.pss.model.PackageDetail;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;

import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;
import nl.com.shipment.selfservice.common.PackageServiceHelper;
import nl.com.shipment.selfservice.common.SelfServiceUtil;
import nl.com.shipment.selfservice.exception.ReceiverNotFoundException;
import nl.com.shipment.selfservice.shipping.ShippingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PackageService {

    private final ShippingService shippingService;
    private final PackageServiceHelper packageServiceHelper;

    public List<Receiver> getReceivers() {
        return SelfServiceUtil.getReceivers();
    }

    public void submitPackage(PackageVO packageVO) {
        Receiver receiver = findReceiver(packageVO);
        shippingService.createShippingOrder(packageServiceHelper.translateToShipping(packageVO, receiver));
    }

    public PackageDetail getPackageDetails(String packageId) {
        return packageServiceHelper.translateToPackageDetail(getShippingOrderByOrderId(packageId));
    }

    public List<PackageDetail> getPackagesBySender(String senderEmployeeId, String status, Integer offset, Integer limit) {
        List<PackageVO> packages = getAllPackages();
        List<String> packagesNames = filterPackageNameBySender(packages, senderEmployeeId);
        log.debug("Packages by sender: {}", packagesNames);

        if (packagesNames.isEmpty()) {
            return new ArrayList<>();
        }

        List<ShippingOrderDetails> shippingOrderDetailsList = getShippingOrderDetails(status, offset, limit);

        if (shippingOrderDetailsList.isEmpty()) {
            return new ArrayList<>();
        }
        return shippingOrderDetailsList.stream()
                .filter(s -> packagesNames.stream()
                        .anyMatch(name -> name.equalsIgnoreCase(s.getPackageName().trim())))
                .map(packageServiceHelper::translateToPackageDetail)
                .collect(Collectors.toList());
    }


    private ShippingOrderDetails getShippingOrderByOrderId(String packageId) {
        return shippingService.getShippingOrderByOrderId(packageId);
    }

    private Receiver findReceiver(PackageVO packageVO) {
        return SelfServiceUtil.getReceivers().stream()
                .filter(r -> r.getEmployeeId().equals(packageVO.getReceiverId()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Receiver not found for given Id: {}", packageVO.getReceiverId());
                    return new ReceiverNotFoundException("Receiver not found!");
                });
    }

    private List<PackageVO> getAllPackages() {
        return SelfServiceUtil.getPackages();
    }

    private List<String> filterPackageNameBySender(List<PackageVO> packages, String senderEmployeeId) {
        return packages.stream().filter(p -> p.getSenderId().equals(senderEmployeeId)).map(PackageVO::getPackageName).collect(Collectors.toList());
    }

    private List<ShippingOrderDetails> getShippingOrderDetails(String status, Integer offset, Integer limit) {
        if(StringUtils.isNotBlank(status)){
            return shippingService.listOrders(status, offset, limit);
        }
        return shippingService.listOrders(offset, limit);

    }
}