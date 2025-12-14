package com.segwarez.modularmonolith.billing.application;

import com.segwarez.modularmonolith.billing.api.BillingFacade;
import com.segwarez.modularmonolith.billing.domain.Payment;
import com.segwarez.modularmonolith.billing.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingApplicationService implements BillingFacade {
    private final PaymentRepository paymentRepository;

    @Override
    public boolean makePayment(UUID orderId, BigDecimal amount) {
        Payment payment = new Payment(orderId, amount);

        if (amount == null || amount.signum() <= 0) {
            payment.fail();
            paymentRepository.save(payment);
            return false;
        }

        payment.complete();
        paymentRepository.save(payment);
        return true;
    }
}
