package cc.corly.springboot.demo.config;

import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class MybatisConfig {
    @Resource
    Environment env;

    /**
     * SQL执行效率插件
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        interceptor.setWriteInLog(true);
        String maxTime = env.getProperty("mybatis.sql.execute.maxtime");
        if (StringUtils.isBlank(maxTime)) {
            maxTime = "20";
        }
        interceptor.setMaxTime(Integer.parseInt(maxTime));
        return interceptor;
    }
}
