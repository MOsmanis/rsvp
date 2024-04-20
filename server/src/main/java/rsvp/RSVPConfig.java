package wedding;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WeddingConfig implements WebMvcConfigurer {
    @Value("${cross.origin.host}")
    private String crossOriginHost;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(crossOriginHost)
            .allowedMethods("POST","GET")
            .allowCredentials(false).maxAge(3600);
    }
}
