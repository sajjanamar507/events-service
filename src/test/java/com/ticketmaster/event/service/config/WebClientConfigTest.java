package com.ticketmaster.event.service.config;

import com.ticketmaster.event.service.exception.UrlNotFoundException;
import com.ticketmaster.event.service.exception.WebClientCustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

@SpringBootTest
@TestPropertySource(properties = "base.url=http://localhost:8080")
@ActiveProfiles("test")
public class WebClientConfigTest {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${base.url}")
    private String baseUrl;

    @Test
    public void webClient_returnsWebClientInstance() throws WebClientCustomException {
        WebClient webClient = new WebClientConfiguration(baseUrl, webClientBuilder).webClient();
        assertThat(webClient).isNotNull();
    }

    @Test
    void testWebClientWithNullBaseUrl() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClientConfiguration webClientConfig = new WebClientConfiguration(null, webClientBuilder);

        assertThrows(UrlNotFoundException.class, () -> {
            webClientConfig.webClient();
        });
    }

    @Test
    void testWebClientWithEmptyBaseUrl() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        WebClientConfiguration webClientConfig = new WebClientConfiguration("", webClientBuilder);

        assertThrows(UrlNotFoundException.class, () -> {
            webClientConfig.webClient();
        });
    }
}