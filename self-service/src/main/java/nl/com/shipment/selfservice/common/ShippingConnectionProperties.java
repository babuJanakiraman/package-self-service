package nl.com.shipment.selfservice.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ShippingConnectionProperties {

        @Value("${shippingService.maxHttpPoolSize:20}")
        private int maxHttpPoolSize;

        @Value("${shippingService.readTimeoutInMillis:3000}")
        private int readTimeoutInMillis;

        @Value("${shippingService.connectionTimeoutInMillis:300}")
        private int connectionTimeoutInMillis;

        @Value("${shippingService.url}")
        private String url;

        @Value("${shippingService.keepAliveStrategy:5000}")
        private int keepAliveStrategy;

}
