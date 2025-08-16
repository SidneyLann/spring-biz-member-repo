package com.blockchain.base.data;

import java.util.Collections;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfig 
{
  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.driverClassName}")
  private String driverClassName;

  @Value("${spring.datasource.initialSize}")
  private int initialSize;

  @Value("${spring.datasource.maxActive}")
  private int maxActive;

  @Bean
  public DataSource dataSource() {
    DruidDataSource dataSource = new DruidDataSource();

    dataSource.setUrl(dbUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);
    dataSource.setInitialSize(initialSize);
    dataSource.setMaxActive(maxActive);

    dataSource.setConnectionInitSqls(Collections.singletonList("set names utf8mb4;"));

    // return new DataSourceProxy(dataSource);
    return dataSource;
  }
}
