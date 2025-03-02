package com.segwarez.micronautweb.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class Pagination {
    private int pageNumber;
    private int pageSize;
    private List<SortOrder> sortOrders;
}
