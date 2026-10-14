package com.example.booking.domain.repository;

import com.example.booking.domain.model.BookableItem;
import java.util.List;
import java.util.Optional;

public interface ItemDomainRepository {
    List<BookableItem> findAll();
    Optional<BookableItem> findById(Long id);
    BookableItem save(BookableItem item);
    void deleteById(Long id);
}
