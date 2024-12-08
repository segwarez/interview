package com.segwarez.vertxweb.web;

import com.segwarez.vertxweb.service.Pagination;
import com.segwarez.vertxweb.service.SortDirection;
import com.segwarez.vertxweb.service.SortOrder;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaginationMapper {
    private static final String PAGE_PARAMETER = "page";
    private static final String SIZE_PARAMETER = "size";
    private static final String SORT_PARAMETER = "sort";
    private static final String DELIMITER = ",";

    private PaginationMapper() {
    }

    public static Pagination fromContext(RoutingContext rc) {
        var instance = new Pagination();
        rc.queryParam(PAGE_PARAMETER).forEach(page -> instance.setPageNumber(Integer.parseInt(page)));
        rc.queryParam(SIZE_PARAMETER).forEach(size -> instance.setPageSize(Integer.parseInt(size)));
        instance.setSortOrders(
            rc.queryParam(SORT_PARAMETER).stream()
                .map(PaginationMapper::parseSortOrders)
                .flatMap(List::stream)
                .toList()
        );
        return instance;
    }

    private static List<SortOrder> parseSortOrders(String sort) {
        List<SortOrder> sortOrders = new ArrayList<>();
        String[] elements = sort.split(DELIMITER);
        Optional<SortDirection> sortDirection = sortDirectionPresent(elements);
        int lastIndex = elements.length - 1;
        if (sortDirection.isPresent()) lastIndex--;
        for (int i = 0; i <= lastIndex; i++) {
            sortOrders.add(new SortOrder(elements[i], sortDirection.isPresent() ? sortDirection.get() : SortDirection.ASC));
        }
        return sortOrders;
    }

    private static Optional<SortDirection> sortDirectionPresent(String[] elements) {
        if (elements.length > 1) {
            try {
                return Optional.of(SortDirection.valueOf(elements[elements.length - 1].toUpperCase()));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
