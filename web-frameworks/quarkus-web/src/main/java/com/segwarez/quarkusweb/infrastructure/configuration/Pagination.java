package com.segwarez.quarkusweb.infrastructure.configuration;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class Pagination {
    private Page page;
    private Sort sort = Sort.empty();

    public void of(int pageNumber, int pageSize, List<String> sortQuery) {
        this.page = Page.of(pageNumber, pageSize);

        if (sortQuery != null && !sortQuery.isEmpty()) {
            sortQuery.forEach(sortString -> {
                String[] parts = sortString.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid sort query format. Expected 'fieldName,asc' or 'fieldName,desc'");
                }

                String fieldName = parts[0];
                String direction = parts[1];

                if ("asc".equalsIgnoreCase(direction)) {
                    sort.and(fieldName, Sort.Direction.Ascending);
                } else if ("desc".equalsIgnoreCase(direction)) {
                    sort.and(fieldName, Sort.Direction.Descending);
                } else {
                    throw new IllegalArgumentException("Invalid sort direction. Use 'asc' or 'desc'.");
                }
            });
        }
        this.page = Page.of(pageNumber, pageSize);
    }

    public Sort getSort() {
        return sort;
    }

    public Page getPage() {
        return page;
    }
}
