package com.segwarez.micronautweb.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SortOrder {
    String field;
    SortDirection direction;
}
