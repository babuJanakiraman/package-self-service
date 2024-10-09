package nl.com.shipment.selfservice.config;

import nl.com.shipment.selfservice.interceptors.SelfServiceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SelfServiceInterceptor selfServiceInterceptor;

    public WebConfig(SelfServiceInterceptor selfServiceInterceptor) {
        this.selfServiceInterceptor = selfServiceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(selfServiceInterceptor);
    }
}
