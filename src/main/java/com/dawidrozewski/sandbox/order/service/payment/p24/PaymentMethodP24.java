package com.dawidrozewski.sandbox.order.service.payment.p24;

import com.dawidrozewski.sandbox.order.model.Order;
import com.dawidrozewski.sandbox.order.model.dto.NotificationReceiveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.dawidrozewski.sandbox.order.service.payment.p24.RequestUtil.createRegisterRequest;
import static com.dawidrozewski.sandbox.order.service.payment.p24.RequestUtil.createVerifyRequest;
import static com.dawidrozewski.sandbox.order.service.payment.p24.RequestUtil.validate;
import static com.dawidrozewski.sandbox.order.service.payment.p24.RequestUtil.validateIpAddress;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodP24 {

    private final PaymentMethodP24Config config;
    private final WebClient p24Client;

    public String initPayment(Order newOrder) {
        log.info("Payment initialization");

        ResponseEntity<TransactionRegisterResponse> result = p24Client.post().uri("/transaction/register")
                .bodyValue(createRegisterRequest(config, newOrder))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                            log.error("Something went wrong: {}", clientResponse.statusCode().value());
                            return Mono.empty();
                        })
                .toEntity(TransactionRegisterResponse.class)
                .block();

        if (result != null && result.getBody() != null && result.getBody().getData() != null) {
            return (config.isTestMode() ? config.getTestUrl() : config.getUrl()) +
                    "/trnRequest/" +
                    result.getBody().getData().token();
        }
        return null;

    }

    public String receiveNotification(Order order, NotificationReceiveDto receiveDto, String remoteAddr) {
        log.info(receiveDto.toString());
        validateIpAddress(remoteAddr, config);
        validate(receiveDto, order, config);
        return verifyPayment(receiveDto, order);
    }

    private String verifyPayment(NotificationReceiveDto receiveDto, Order order) {
        ResponseEntity<TransactionVerifyResponse> result = p24Client.put().uri("/transaction/verify")
                .bodyValue(createVerifyRequest(config, order, receiveDto))
                .retrieve()
                .toEntity(TransactionVerifyResponse.class)
                .block();

        log.info("Verification transaction status: " + result.getBody().getData().status());
        return result.getBody().getData().status();
    }
}
