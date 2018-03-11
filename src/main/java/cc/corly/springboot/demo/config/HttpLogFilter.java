package cc.corly.springboot.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

@Component
public class HttpLogFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(HttpLogFilter.class);
    private int maxPayloadLength = 1000;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer("RequestLog:");
            sb.append(httpServletRequest.getMethod()).append(",");
            sb.append(httpServletRequest.getRequestURL());
            String queryStr = httpServletRequest.getQueryString();
            if (StringUtils.isNotBlank(queryStr)) {
                sb.append("?").append(queryStr);
            }
            request.getInputStream();
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
            String body = requestWrapper.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//            String body = getContentAsString(requestWrapper.getContentAsByteArray(), maxPayloadLength, request.getCharacterEncoding());
            if (StringUtils.isNotBlank(body)) {
                sb.append(", body=").append(body);
            }
            log.info(sb.toString());
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {

    }

    private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
        if (buf == null || buf.length == 0) return "";
        int length = Math.min(buf.length, this.maxPayloadLength);
        try {
            return new String(buf, 0, length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }
}
