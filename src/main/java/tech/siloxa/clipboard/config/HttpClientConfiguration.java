package tech.siloxa.clipboard.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Duration;

@Configuration
public class HttpClientConfiguration {

    @Resource
    private ApplicationProperties applicationProperties;

    @Bean
    public RestTemplate customRestTemplate(RestTemplateBuilder builder) {
        return builder
            .setReadTimeout(Duration.ofSeconds(applicationProperties.getHttpClient().getReadTimeout()))
            .setConnectTimeout(Duration.ofSeconds(applicationProperties.getHttpClient().getConnectionTimeout()))
            .build();
    }
}

