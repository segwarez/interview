package com.segwarez.ruleevaluator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private BigInteger productId;
    private int capacity;
    private String name;
}
