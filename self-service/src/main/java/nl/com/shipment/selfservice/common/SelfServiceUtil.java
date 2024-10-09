package nl.com.shipment.selfservice.common;


import nl.com.shipment.pss.model.Address;
import nl.com.shipment.pss.model.PackageVO;
import nl.com.shipment.pss.model.Receiver;

import java.util.ArrayList;
import java.util.List;

public class SelfServiceUtil {

    public static List<Receiver> getReceivers(){
        List<Receiver> receivers = new ArrayList<>();
        receivers.add(new Receiver("10001","Robert Swaak",new Address("Street1","3000GB")));
        receivers.add(new Receiver("10002","Jane Doe",new Address("Street2","3001GB")));
        receivers.add(new Receiver("10003","John Smith",new Address("Street3","3002GB")));
        receivers.add(new Receiver("10004","Alice Johnson",new Address("Street4","3003GB")));
        receivers.add(new Receiver("10005","Bob Brown",new Address("Street5","3004GB")));
        receivers.add(new Receiver("10006","Charlie Green",new Address("Street6","3005GB")));
        receivers.add(new Receiver("10007","David White",new Address("Street7","3006GB")));
        receivers.add(new Receiver("10008","Robert Swaak",new Address("Street8","3007GB")));
        return receivers;
    }

    public static List<PackageVO> getPackages(){
        List<PackageVO> packages = new ArrayList<>();
        packages.add(new PackageVO("package1", 2000.0,"10001","20001"));
        packages.add(new PackageVO("package2", 3000.0,"10002","20002"));
        packages.add(new PackageVO("package3", 4000.0,"10003","20003"));
        packages.add(new PackageVO("package4", 5000.0,"10004","20001"));
        packages.add(new PackageVO("Package5", 6000.0,"10005","20002"));
        packages.add(new PackageVO("Package6", 7000.0,"10006","20003"));
        packages.add(new PackageVO("Package7", 8000.0,"10007","20001"));
        packages.add(new PackageVO("Package8", 9000.0,"10008","20002"));
        packages.add(new PackageVO("Package9", 10000.0,"10009","20003"));
        packages.add(new PackageVO("Package10", 11000.0,"10010","20001"));
        packages.add(new PackageVO("Package11", 12000.0,"10011","20002"));
        packages.add(new PackageVO("Package12", 13000.0,"10012","20003"));
        return packages;
    }
}

