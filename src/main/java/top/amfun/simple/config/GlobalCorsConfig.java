package top.amfun.simple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @date 2020/11/4 9:56
 * @description: 全局跨域支持
 */
@Configuration
public class GlobalCorsConfig {

    private CorsConfiguration builder() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 放行全部原始头信息
        configuration.addAllowedHeader("*");
        // 允许所有方法跨域访问
        configuration.addAllowedMethod("*");
        // 允许所有域名跨域访问
        configuration.addAllowedOrigin("*");
        // 允许跨域Cookie
        configuration.setAllowCredentials(true);
        return configuration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", builder());
        return new CorsFilter(source);
    }
}
