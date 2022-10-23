package com.wj.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * WebMvc配置
 *
 * @author wangjie
 * @date 11:51 2022年07月26日
 **/
@Component
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private SignInterceptor signInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 放开knife4j的html拦截
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 放开knife4j的css和js拦截
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //放开knife4j的图片拦截
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/META-INF/resources/img/");

    }

    /**
     * 登录拦截
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {

        // 放行路径
        List<String> patterns = new ArrayList<>();
        patterns.add("/doc.html");
        patterns.add("/webjars/**");
        patterns.add("/favicon.ico");
        patterns.add("/error");
        patterns.add("/img/**");
        patterns.add("/v2/api-docs");
        patterns.add("v2/**");
        patterns.add("/swagger-resources/**");
        // 需要放开用户登录接口
        patterns.add("/user/**");
        patterns.add("/test/**");
        registry.addInterceptor(signInterceptor)
                .excludePathPatterns(patterns);
    }
}
