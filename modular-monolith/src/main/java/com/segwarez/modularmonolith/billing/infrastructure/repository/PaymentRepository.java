package com.segwarez.modularmonolith.billing.infrastructure.repository;

import com.segwarez.modularmonolith.billing.domain.Payment;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PaymentRepository {
    private final Map<UUID, Payment> storage = new ConcurrentHashMap<>();

    public void save(Payment payment) {
        storage.put(payment.getId(), payment);
    }
}
