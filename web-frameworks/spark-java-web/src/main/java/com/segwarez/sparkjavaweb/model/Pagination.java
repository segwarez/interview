package com.segwarez.sparkjavaweb.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Pagination {
    private int pageNumber = 0;
    private int pageSize = 20;
    private List<SortOrder> sortOrders = Collections.emptyList();
}