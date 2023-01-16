package com.segwarez.ruleevaluator.application.rest;

import com.segwarez.ruleevaluator.domain.model.Order;
import com.segwarez.ruleevaluator.domain.model.ValidationDTO;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/validate")
@RequiredArgsConstructor
public class ValidationController {

    private final KieSession session;

    @PostMapping("/order")
    public ValidationDTO validateOrder(@RequestBody Order order) {
        ValidationDTO validationDTO = new ValidationDTO(order);
        session.insert(validationDTO);
        session.fireAllRules();
        return validationDTO;
    }
}
