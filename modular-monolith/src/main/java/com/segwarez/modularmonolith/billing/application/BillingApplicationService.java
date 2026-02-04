package com.segwarez.modularmonolith.billing.application;

import com.segwarez.modularmonolith.billing.api.BillingFacade;
import com.segwarez.modularmonolith.billing.api.PaymentMethod;
import com.segwarez.modularmonolith.billing.domain.Payment;
import com.segwarez.modularmonolith.billing.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingApplicationService implements BillingFacade {
    private final PaymentRepository paymentRepository;

    @Override
    public boolean makePayment(UUID orderId, BigDecimal amount, PaymentMethod paymentMethod) {
        Payment payment = new Payment(orderId, amount);
        process(payment, paymentMethod);
        paymentRepository.save(payment);
        return true;
    }

    public void process(Payment payment, PaymentMethod paymentMethod) {
        if (Objects.requireNonNull(paymentMethod) == PaymentMethod.CARD) {
            payment.fail();
        } else {
            payment.complete();
        }
    }
}
