package com.segwarez.ruleevaluator.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ValidationDTO {
    Order order;
    List<String> validationErrors;

    public ValidationDTO(Order order) {
        this.order = order;
        this.validationErrors = new ArrayList<>();
    }
}
