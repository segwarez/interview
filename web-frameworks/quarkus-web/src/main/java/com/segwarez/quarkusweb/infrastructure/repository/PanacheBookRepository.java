package com.segwarez.quarkusweb.infrastructure.repository;

import com.segwarez.quarkusweb.infrastructure.configuration.Pagination;
import com.segwarez.quarkusweb.infrastructure.repository.entity.BookEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PanacheBookRepository implements PanacheRepositoryBase<BookEntity, UUID> {
    public List<BookEntity> findAll(Pagination pagination) {
        return findAll(pagination.getSort()).page(pagination.getPage()).list();
    }

    public List<BookEntity> findByTitleContaining(String title, Pagination pagination) {
        return find("title like ?1", pagination.getSort(), "%" + title + "%")
                .page(pagination.getPage()).list();
    }

    public List<BookEntity> findByPublished(boolean published, Pagination pagination) {
        return find("published = ?1", pagination.getSort(), published)
                .page(pagination.getPage()).list();
    }
}
