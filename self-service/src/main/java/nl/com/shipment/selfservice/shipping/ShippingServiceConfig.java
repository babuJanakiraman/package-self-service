package nl.com.shipment.selfservice.shipping;

import lombok.AllArgsConstructor;
import nl.com.shipment.selfservice.common.ShippingConnectionProperties;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ShippingServiceConfig {

    @Bean(name = "shippingServiceHttpClient")
    public CloseableHttpClient getHttpClient(ShippingConnectionProperties shippingConnectionProperties) {

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(shippingConnectionProperties.getMaxHttpPoolSize());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(shippingConnectionProperties.getMaxHttpPoolSize());

        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofMilliseconds(shippingConnectionProperties.getConnectionTimeoutInMillis()))
                        .setResponseTimeout(Timeout.ofMilliseconds(shippingConnectionProperties.getReadTimeoutInMillis()))
                        .setConnectionKeepAlive(TimeValue.ofMilliseconds(shippingConnectionProperties.getKeepAliveStrategy()))
                        .build())
                .disableConnectionState()
                .build();
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }


}