package com.segwarez.payment.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @GetMapping("/")
    public String getPayments(){
        return "Tadam!";
    }
}
