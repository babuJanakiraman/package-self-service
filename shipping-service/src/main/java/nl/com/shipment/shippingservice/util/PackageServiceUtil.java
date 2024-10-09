package nl.com.shipment.shippingservice.util;



import nl.com.shipment.pss.shipping.model.ShippingOrderDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PackageServiceUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public static List<ShippingOrderDetails> getShippingOrderDetailsList() {
        return List.of(
                new ShippingOrderDetails("3f6c794b-2c96-491e-81fb-a2f9731d02c4", "package1", ShippingOrderDetails.PackageSizeEnum.M, "1082PP", "Gustav Mahlerlaan 10", "Robert Swaak", ShippingOrderDetails.OrderStatusEnum.DELIVERED, parseDate("2024-10-19"), convertStringToDate("2024-10-17T08:30:00Z")),
                new ShippingOrderDetails("4a7d794b-3d97-492e-82fc-b3f9731d03d5", "package2", ShippingOrderDetails.PackageSizeEnum.L, "1083PP", "Beethovenstraat 20", "Jane Doe", ShippingOrderDetails.OrderStatusEnum.SENT, parseDate("2021-11-01"),null),
                new ShippingOrderDetails("5b8e794b-4e98-493e-83fd-c4f9731d04e6", "package3", ShippingOrderDetails.PackageSizeEnum.S, "1084PP", "Bachstraat 30", "John Smith", ShippingOrderDetails.OrderStatusEnum.IN_PROGRESS, parseDate("2024-11-01"),null),
                new ShippingOrderDetails("6c9f794b-5f99-494e-84fe-d5f9731d05f7", "package4", ShippingOrderDetails.PackageSizeEnum.XL, "1085PP", "Mozartlaan 40", "Alice Johnson", ShippingOrderDetails.OrderStatusEnum.IN_PROGRESS, parseDate("2024-11-01"), null),
                new ShippingOrderDetails("7d0g794b-6g00-495e-85ff-e6f9731d06g8", "package5", ShippingOrderDetails.PackageSizeEnum.M, "1086PP", "Chopinlaan 50", "Bob Brown", ShippingOrderDetails.OrderStatusEnum.DELIVERED, parseDate("2024-11-01"), convertStringToDate("2024-10-05T08:30:00Z")),
                new ShippingOrderDetails("8e1h794b-7h01-496e-86gg-f7f9731d07h9", "package6", ShippingOrderDetails.PackageSizeEnum.L, "1087PP", "Vivaldilaan 60", "Charlie Green", ShippingOrderDetails.OrderStatusEnum.SENT, parseDate("2024-11-01"), null),
                new ShippingOrderDetails("9f2i794b-8i02-497e-87hh-g8f9731d08i0", "package7", ShippingOrderDetails.PackageSizeEnum.S, "1088PP", "Tchaikovskilaan 70", "David White", ShippingOrderDetails.OrderStatusEnum.IN_PROGRESS, parseDate("2024-11-01"), null),
                new ShippingOrderDetails("0g3j794b-9j03-498e-88ii-h9f9731d09j1", "package8", ShippingOrderDetails.PackageSizeEnum.M, "1082PP", "Gustav Mahlerlaan 10", "Robert Swaak", ShippingOrderDetails.OrderStatusEnum.SENT, parseDate("2024-11-01"), null),
                new ShippingOrderDetails("1h4k794b-0k04-499e-89jj-i0f9731d00k2", "package9", ShippingOrderDetails.PackageSizeEnum.L, "1083PP", "Beethovenstraat 20", "Jane Doe", ShippingOrderDetails.OrderStatusEnum.IN_PROGRESS, parseDate("2024-11-01"), null),
                new ShippingOrderDetails("2i5l794b-1l05-400e-90kk-j1f9731d01l3", "package10", ShippingOrderDetails.PackageSizeEnum.S, "1084PP", "Bachstraat 30", "John Smith", ShippingOrderDetails.OrderStatusEnum.DELIVERED, parseDate("2024-11-01"), parseDate("2024-10-05T08:30:00Z"))
        );
    }
    private static Date parseDate(String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("CET"));

        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
