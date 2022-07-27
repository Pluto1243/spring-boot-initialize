package com.wj.boot.config;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 *
 * @author wangjie
 * @date 10:27 2022年07月26日
 **/
@Configuration
public class CorsConfig {
    /**
     * 解决跨域
     *
     * @return
     */
    @Bean
    public CorsFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置允许的方法
        config.addAllowedMethod("*");
        // 设置允许跨域请求的域名
        config.addAllowedOriginPattern("*");
        config.setAllowCredentials(true);
        // 设置允许请求的请求头
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 设置允许跨域的路由
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }

    @Bean
    public TomcatContextCustomizer sameSiteCookiesConfig() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            // 设置Cookie的SameSite
            cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
            context.setCookieProcessor(cookieProcessor);
        };
    }
}
