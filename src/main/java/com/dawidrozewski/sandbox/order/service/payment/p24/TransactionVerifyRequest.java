package com.dawidrozewski.sandbox.order.service.payment.p24;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionVerifyRequest {
    private Integer merchantId;
    private Integer posId;
    private String sessionId;
    private Integer amount;
    private String currency;
    private Integer orderId;
    private String sign;
}
