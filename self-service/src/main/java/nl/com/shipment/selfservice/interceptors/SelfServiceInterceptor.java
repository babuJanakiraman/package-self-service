package nl.com.shipment.selfservice.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SelfServiceInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Map<String, String>> requestHeaders = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("Request Method: {}, Endpoint: {} ", request.getMethod(), request.getRequestURI());
        final Map<String, String> headers = logRequestHeaders(request);
        requestHeaders.set(headers);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        final Map<String, String> headers = requestHeaders.get();
        addHeadersToResponse(response, headers);
    }

    private Map<String, String> logRequestHeaders(HttpServletRequest request) {
        final Map<String, String> headers = new HashMap<>();
        final Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                final String headerName = headerNames.nextElement();
                final String headerValue = request.getHeader(headerName);
                headers.put(headerName, headerValue);
                log.debug("Header: {} = {}", headerName, headerValue);
            }
        }
        return headers;
    }

    private void addHeadersToResponse(HttpServletResponse response, Map<String, String> headers) {
        log.debug("Response Headers: {}", headers);
        response.addHeader("Request-Id", requestHeaders.get().get("Request-Id"));
    }
}