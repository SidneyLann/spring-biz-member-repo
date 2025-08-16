package com.blockchain.base.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import io.seata.integration.http.TransactionPropagationInterceptor;

//@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);
    registry.addInterceptor(new TransactionPropagationInterceptor()).addPathPatterns("/**").excludePathPatterns("/", "/fe/**", "/be/**", "/h5/**", "/ueditor/**");
  }
  
}
