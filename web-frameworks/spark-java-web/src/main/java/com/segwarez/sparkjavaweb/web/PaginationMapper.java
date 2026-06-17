package com.segwarez.sparkjavaweb.web;

import com.segwarez.sparkjavaweb.model.Pagination;
import com.segwarez.sparkjavaweb.model.SortDirection;
import com.segwarez.sparkjavaweb.model.SortOrder;
import spark.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PaginationMapper {
    private static final String PAGE_PARAMETER = "page";
    private static final String SIZE_PARAMETER = "size";
    private static final String SORT_PARAMETER = "sort";
    private static final String DELIMITER = ",";

    private PaginationMapper() {
    }

    public static Pagination fromRequest(Request request) {
        var instance = new Pagination();
        Optional.ofNullable(request.queryParams(PAGE_PARAMETER))
                .ifPresent(page -> instance.setPageNumber(Integer.parseInt(page)));
        Optional.ofNullable(request.queryParams(SIZE_PARAMETER))
                .ifPresent(size -> instance.setPageSize(Integer.parseInt(size)));
        instance.setSortOrders(
                Arrays.stream(Optional.ofNullable(request.queryParamsValues(SORT_PARAMETER)).orElse(new String[0]))
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
            sortOrders.add(new SortOrder(elements[i], sortDirection.orElse(SortDirection.ASC)));
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