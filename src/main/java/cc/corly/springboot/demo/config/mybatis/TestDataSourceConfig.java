package cc.corly.springboot.demo.config.mybatis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "cc.corly.springboot.demo.mapper", sqlSessionFactoryRef = "sqlSessionFactoryTest")
public class TestDataSourceConfig {
    @Resource
    private PerformanceInterceptor performanceInterceptor;

    @ConfigurationProperties(prefix = "jdbc.test")
    @Bean("dataSourceTest")
    public DataSource druidDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManagerTest")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier(value = "dataSourceTest") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactoryTest")
    @ConfigurationProperties(prefix = "mybatis-plus.test")
    @ConfigurationPropertiesBinding()
    public SqlSessionFactory sqlSessionFactoryBrokerCommonBiz(@Qualifier(value = "dataSourceTest") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType(DBType.MYSQL.getDb());
        factoryBean.setPlugins(new Interceptor[]{
                paginationInterceptor, performanceInterceptor
        });
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }

}
