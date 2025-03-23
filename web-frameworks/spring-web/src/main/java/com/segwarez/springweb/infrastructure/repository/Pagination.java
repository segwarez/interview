package com.segwarez.springweb.infrastructure.repository;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Data
@Component
@RequestScope
public class Pagination {
    private Pageable pageable;
}
