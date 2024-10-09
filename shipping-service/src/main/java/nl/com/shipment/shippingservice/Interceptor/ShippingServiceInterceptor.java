package nl.com.shipment.shippingservice.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ShippingServiceInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Map<String, String>> requestHeaders = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, String> headers = logRequestHeaders(request);
        requestHeaders.set(headers);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Map<String, String> headers = requestHeaders.get();
        addHeadersToResponse(response, headers);
    }

    private Map<String, String> logRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.put(headerName, headerValue);
                log.debug("Header: {} = {}", headerName, headerValue);
            }
        }
        return headers;
    }

    private void addHeadersToResponse(HttpServletResponse response, Map<String, String> headers) {
        log.debug("Response Headers: {}", headers);
        response.addHeader("X-Correlation-ID", String.valueOf(UUID.randomUUID()));
        response.addHeader("Request-Id", String.valueOf(UUID.randomUUID()));
    }
}