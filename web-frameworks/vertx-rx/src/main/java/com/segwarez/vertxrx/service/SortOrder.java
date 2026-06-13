package com.segwarez.vertxrx.service;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SortOrder {
    String field;
    SortDirection direction;
}
