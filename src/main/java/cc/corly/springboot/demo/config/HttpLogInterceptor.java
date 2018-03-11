package cc.corly.springboot.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class HttpLogInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(HttpLogInterceptor.class);
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        StringBuilder sb = new StringBuilder("RequestLog:");
        sb.append(request.getMethod()).append(",");
        sb.append(request.getURI()).append(",");
        String requestBody = new String(body, StandardCharsets.UTF_8);
        sb.append("body=").append(requestBody);
        log.info(sb.toString());

        return null;
    }
}
