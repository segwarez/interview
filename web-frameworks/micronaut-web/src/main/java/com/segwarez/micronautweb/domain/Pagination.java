package com.segwarez.micronautweb.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class Pagination {
    int pageNumber;
    int pageSize;
    List<SortOrder> sortOrders;
}
