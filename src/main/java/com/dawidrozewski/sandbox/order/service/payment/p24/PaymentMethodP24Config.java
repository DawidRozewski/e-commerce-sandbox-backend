package com.dawidrozewski.sandbox.order.service.payment.p24;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app.payments.p24")
public class PaymentMethodP24Config {
    private boolean testMode;

    private String url;
    private String apiUrl;
    private Integer merchantId;
    private Integer posId;
    private String urlReturn;
    private String urlStatus;
    private String crc;
    private String secretKey;

    private String testUrl;
    private String testApiUrl;
    private String testUrlReturn;
    private String testUrlStatus;
    private String testCrc;
    private String testSecretKey;

    private List<String> servers;

    @Bean
    public WebClient p24Client(PaymentMethodP24Config config) {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.encodeBasicAuth(
                                config.getPosId().toString(),
                                config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey(),
                                StandardCharsets.UTF_8
                        )
                ).baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();
    }
}
