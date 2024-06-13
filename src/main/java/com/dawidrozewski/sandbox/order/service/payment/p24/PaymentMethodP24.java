package com.dawidrozewski.sandbox.order.service.payment.p24;

import com.dawidrozewski.sandbox.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodP24 {

    private final PaymentMethodP24Config config;

    public String initPayment(Order newOrder) {
        log.info("Payment initialization");

        WebClient webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.encodeBasicAuth(
                        config.getPosId().toString(),
                        config.isTestMode() ?
                        config.getTestSecretKey() :
                        config.getSecretKey(),
                        StandardCharsets.UTF_8
                        )
                ).baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();

        ResponseEntity<TransactionRegisterResponse> result = webClient.post().uri("/transaction/register")
                .bodyValue(TransactionRegisterRequest.builder()
                        .merchantId(config.getMerchantId())
                        .posId(config.getPosId())
                        .sessionId(createSessionId(newOrder))
                        .amount(convertAmountToInteger(newOrder))
                        .currency("PLN")
                        .description("Order id: " +newOrder.getId())
                        .email(newOrder.getEmail())
                        .client(newOrder.getFirstname() + " " + newOrder.getLastname())
                        .country("PL")
                        .language("pl")
                        .urlReturn(config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn())
                        .sign(createSign(newOrder))
                        .encoding("UTF-8")
                        .build())
                .retrieve()
                .onStatus(httpStatusCode -> {
                    log.error("Something went wrong: {}", httpStatusCode.toString());
                    return httpStatusCode.is4xxClientError();
                }, clientResponse -> Mono.empty())
                .toEntity(TransactionRegisterResponse.class)
                .block();

        if(result != null && result.getBody() != null && result.getBody().getData() != null) {
            return (config.isTestMode() ? config.getTestUrl() : config.getUrl()) +
                    "/trnRequest/" +
                    result.getBody().getData().token();
        }
        return null;

    }

    private String createSign(Order newOrder) {
        String json = "{" +
                "\"sessionId\":\"" + createSessionId(newOrder) + "\"," +
                "\"merchantId\":" + config.getMerchantId() + "," +
                "\"amount\":" + convertAmountToInteger(newOrder) + "," +
                "\"currency\":\"" + "PLN" + "\"," +
                "\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"" +
                "}";
        return DigestUtils.sha384Hex(json);
    }

    private int convertAmountToInteger(Order newOrder) {
        return newOrder.getGrossValue().movePointRight(2).intValue();
    }

    private String createSessionId(Order newOrder) {
        return "order_id_" + newOrder.getId().toString();
    }
}
