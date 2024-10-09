package nl.com.shipment.shippingservice.config;

import nl.com.shipment.shippingservice.Interceptor.ShippingServiceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ShippingServiceInterceptor requestResponseInterceptor;

    public WebConfig(ShippingServiceInterceptor requestResponseInterceptor) {
        this.requestResponseInterceptor = requestResponseInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestResponseInterceptor);
    }
}
