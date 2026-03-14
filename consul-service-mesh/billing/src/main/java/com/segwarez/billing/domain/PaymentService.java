package com.segwarez.billing.domain;

import com.segwarez.billing.infrastructure.external.WarehouseClient;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final WarehouseClient warehouseClient;

    public void pay() {
        log.info("Making payment");
        waitForConfirmation();
        log.info("Confirmation received");
        updateWarehouse();
        log.info("Warehouse updated");
    }

    @WithSpan("billing.wait_for_confirmation")
    public void waitForConfirmation() {
        int delaySeconds = ThreadLocalRandom.current().nextInt(2, 6);
        try {
            TimeUnit.SECONDS.sleep(delaySeconds);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }

    @WithSpan("billing.update_warehouse")
    public void updateWarehouse() {
        warehouseClient.update();
    }
}