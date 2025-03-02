package com.segwarez.micronautweb.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SortOrder {
    private String field;
    private SortDirection direction;
}
