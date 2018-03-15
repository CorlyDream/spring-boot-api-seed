package cc.corly.springboot.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class HttpLogFilter extends AbstractRequestLoggingFilter {
    private static final Logger log = LoggerFactory.getLogger(HttpLogFilter.class);

    public static final String TRACE_KEY = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put(TRACE_KEY, UUID.randomUUID().toString());
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        super.doFilterInternal(request, responseWrapper, filterChain);
        if (log.isInfoEnabled()) {
            String respBody = getContentAsString(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8.name());
            log.info("uri={};ResponseBody={}", request.getRequestURI(), respBody);
            responseWrapper.copyBodyToResponse();
        }
        MDC.remove(TRACE_KEY);
    }


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }


    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }

    private String getContentAsString(byte[] buf, String charsetName) {
        if (buf == null || buf.length == 0) {
            return "";
        }
        int length = Math.min(buf.length, getMaxPayloadLength());
        try {
            return new String(buf, 0, length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }
}
