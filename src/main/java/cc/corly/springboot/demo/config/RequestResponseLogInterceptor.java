package cc.corly.springboot.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class RequestResponseLogInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RequestResponseLogInterceptor.class);

    private boolean includeResponsePayload = true;
    private int maxPayloadLength = 1000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer("RequestLog:");
            sb.append(request.getMethod()).append(",");
            sb.append(request.getRequestURL());
            String queryStr = request.getQueryString();
            if (StringUtils.isNotBlank(queryStr)) {
                sb.append("?").append(queryStr);
            }
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

            String body = getContentAsString(requestWrapper.getContentAsByteArray(), maxPayloadLength, request.getCharacterEncoding());
            if (StringUtils.isNotBlank(body)) {
                sb.append(", body=").append(body);
            }
            log.info(sb.toString());
        }

        return true;
    }

    private String getRequestBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (
                InputStream inputStream = request.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
        ){
            char[] chars = new char[maxPayloadLength];
            int readByte = br.read(chars);
            if (readByte > 0) {
                sb.append(chars, 0, readByte);
            } else {
                sb.append("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
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
