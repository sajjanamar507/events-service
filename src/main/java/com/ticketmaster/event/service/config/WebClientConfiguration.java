package com.ticketmaster.event.service.config;

import com.ticketmaster.event.service.exception.UrlNotFoundException;
import com.ticketmaster.event.service.exception.WebClientCustomException;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfiguration {
    private final String baseUrl;

    private final WebClient.Builder webClientBuilder;

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfiguration.class);

    public WebClientConfiguration(@Value("${base.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.baseUrl = baseUrl;
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public WebClient webClient() throws UrlNotFoundException {

        if (StringUtils.isBlank(baseUrl)) {
            logger.error("Base URL is null or empty");
            throw new UrlNotFoundException("Base URL is null or empty : {} " + baseUrl);
        }
        try {
            logger.info("WebClient created with base URL: {}", baseUrl);
            return webClientBuilder.baseUrl(baseUrl).build();
        } catch (Exception e) {
            logger.error("Error creating WebClient: {}", e.getMessage(), e);
            throw new WebClientCustomException("Error while creating WebClient: " + e.getMessage());
        }
    }
}
