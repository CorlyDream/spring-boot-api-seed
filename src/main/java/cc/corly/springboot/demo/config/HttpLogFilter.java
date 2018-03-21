package cc.corly.springboot.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * http 请求输出日志
 *
 * @see AbstractRequestLoggingFilter
 */
@Component
public class HttpLogFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(HttpLogFilter.class);

    public static final String TRACE_KEY = "traceId";

    public static final String DEFAULT_BEFORE_MESSAGE_PREFIX = "Before request [";

    public static final String DEFAULT_BEFORE_MESSAGE_SUFFIX = "]";

    public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After request [";

    public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";

    private static final int DEFAULT_MAX_PAYLOAD_LENGTH = 50;


    private boolean includeQueryString = false;

    private boolean includeClientInfo = false;

    private boolean includeHeaders = false;

    private boolean includePayload = false;

    private int maxPayloadLength = DEFAULT_MAX_PAYLOAD_LENGTH;

    private String beforeMessagePrefix = DEFAULT_BEFORE_MESSAGE_PREFIX;

    private String beforeMessageSuffix = DEFAULT_BEFORE_MESSAGE_SUFFIX;

    private String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;

    private String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;

    private Set<String> excludeUris = new HashSet<>();

    private boolean includeResponseBody = false;

    protected boolean shouldLog(HttpServletRequest request) {
        return !excludeUris.contains(request.getRequestURI());
    }

    public void setIncludeResponseBody(boolean includeResponseBody) {
        this.includeResponseBody = includeResponseBody;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put(TRACE_KEY, UUID.randomUUID().toString());

        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;

        if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }

        boolean shouldLog = shouldLog(requestToUse);
        HttpServletResponse responseToUse = response;
        if (includeResponseBody && shouldLog && !(request instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        if (shouldLog && isFirstRequest) {
            beforeRequest(requestToUse, getBeforeMessage(requestToUse));
        }

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (shouldLog && !isAsyncStarted(requestToUse)) {
                afterRequest(requestToUse, getAfterMessage(requestToUse));
            }
        }

        if (shouldLog && includeResponseBody && log.isInfoEnabled()) {
            log.info("uri={};ResponseBody={}", request.getRequestURI(), getMessagePayload(responseToUse));
        }

        MDC.remove(TRACE_KEY);
    }


    private String getBeforeMessage(HttpServletRequest request) {
        return createMessage(request, this.beforeMessagePrefix, this.beforeMessageSuffix);
    }

    private String getAfterMessage(HttpServletRequest request) {
        return createMessage(request, this.afterMessagePrefix, this.afterMessageSuffix);
    }

    protected void beforeRequest(HttpServletRequest request, String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }


    protected void afterRequest(HttpServletRequest request, String message) {
        if (log.isInfoEnabled()) {
            log.info(message);
        }
    }

    protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
        StringBuilder msg = new StringBuilder();
        msg.append(prefix);
        msg.append("uri=").append(request.getRequestURI());

        if (isIncludeQueryString()) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }

        if (isIncludeClientInfo()) {
            String client = request.getRemoteAddr();
            if (org.springframework.util.StringUtils.hasLength(client)) {
                msg.append(";client=").append(client);
            }
            HttpSession session = request.getSession(false);
            if (session != null) {
                msg.append(";session=").append(session.getId());
            }
            String user = request.getRemoteUser();
            if (user != null) {
                msg.append(";user=").append(user);
            }
        }

        if (isIncludeHeaders()) {
            msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
        }

        if (isIncludePayload()) {
            String payload = getMessagePayload(request);
            if (payload != null) {
                msg.append(";payload=").append(payload);
            }
        }

        msg.append(suffix);
        return msg.toString();
    }

    @Nullable
    protected String getMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, getMaxPayloadLength());
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }

    @Nullable
    protected String getMessagePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            try {

                if (buf.length > 0) {
                    int length = Math.min(buf.length, getMaxPayloadLength());
                    try {
                        return new String(buf, 0, length, wrapper.getCharacterEncoding());
                    } catch (UnsupportedEncodingException ex) {
                        return "[unknown]";
                    }
                }
            } finally {

                try {
                    wrapper.copyBodyToResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void addExcludeUri(String uri) {
        excludeUris.add(uri);
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

    public boolean isIncludePayload() {
        return includePayload;
    }

    public int getMaxPayloadLength() {
        return maxPayloadLength;
    }

    public boolean isIncludeHeaders() {
        return includeHeaders;
    }

    public boolean isIncludeClientInfo() {
        return includeClientInfo;
    }

    public boolean isIncludeQueryString() {
        return includeQueryString;
    }

    public void setIncludeQueryString(boolean includeQueryString) {
        this.includeQueryString = includeQueryString;
    }

    public void setIncludeClientInfo(boolean includeClientInfo) {
        this.includeClientInfo = includeClientInfo;
    }

    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }

    public void setMaxPayloadLength(int maxPayloadLength) {
        this.maxPayloadLength = maxPayloadLength;
    }
}
