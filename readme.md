
## 简介

Spring Boot API sedd 是一个基于 Spring Boot & Mybatis Plus 的种子项目

线上建议使用-Dlog4j.configurationFile=log4j2-product.xml 指定log4j2 的配置

## 特征

1. 统一异常处理
2. application.properties 可以配置打印超过指定时间的sql
3. 日志中可以配置统一打印http请求和返回的参数


## 示例

user 表
```sql
CREATE TABLE `user` (
  `test_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `name` varchar(30) DEFAULT NULL COMMENT '名称',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `test_type` int(11) DEFAULT NULL COMMENT '测试下划线字段命名类型',
  `test_date` datetime DEFAULT NULL COMMENT '日期',
  `role` bigint(20) DEFAULT NULL COMMENT '测试',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  PRIMARY KEY (`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
```