package com.seniorlearn.onlinelearning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    // 从配置文件读取允许的域名（支持多环境配置）
    @Value("${app.allowedOrigins:http://localhost:3000}")
    private List<String> allowedOrigins;

    @Bean("customCorsConfig")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 生产环境建议配置具体域名，以逗号分隔
        configuration.setAllowedOrigins(allowedOrigins);

        // 明确允许的HTTP方法（根据实际API需求调整）
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // 明确允许的请求头（根据前端实际发送的头部配置）
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept", "X-Requested-With"
        ));

        // 允许携带凭证（如Cookie）
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        configuration.addExposedHeader("Content-Disposition");

        // 预检请求缓存时间（秒）
        configuration.setMaxAge(3600L);

        // 配置生效路径（根据需要调整）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/​**​", configuration); // 匹配所有路径

        return source;
    }
}
