package com.segwarez.vertxrs.service;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SortOrder {
    private String field;
    private SortDirection direction;
}
