package com.segwarez.vertxweb.service;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Pagination {
    private int pageNumber = 0;
    private int pageSize = 20;
    private List<SortOrder> sortOrders = Collections.emptyList();

    public String prepareQueryParams() {
        var queryParams = new StringBuilder();
        if (!sortOrders.isEmpty()) {
            queryParams.append(" ORDER BY ");
            sortOrders.forEach(sortOrder -> addSortOrder(queryParams, sortOrder));
            queryParams.setLength(queryParams.length() - 2);
        }
        queryParams.append(" LIMIT ");
        queryParams.append(pageSize);
        queryParams.append(" OFFSET ");
        queryParams.append(pageNumber * pageSize);
        return queryParams.toString();

    }

    private void addSortOrder(StringBuilder sb, SortOrder sortOrder) {
        sb.append(sortOrder.getField());
        sb.append(" ");
        sb.append(sortOrder.getDirection().toString());
        sb.append(", ");
    }}
