package pengstore.tk.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsProcessor;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class FilterForCORS {

    @Bean
    public CorsWebFilter corsWebFilter(){
        // CorsConfigurationSource接口的实现类
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedHeader("*");  // 允许哪些请求头进行跨域
        config.addAllowedMethod("*");  // 允许哪些请求方式进行跨域
        config.addAllowedOrigin("*");  // 允许哪些请求来源进行跨域
        config.setAllowCredentials(true);  // 是否允许cookies进行跨域

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);
        CorsWebFilter corsWebFilter = new CorsWebFilter(urlBasedCorsConfigurationSource);
        return corsWebFilter;
    }
}
