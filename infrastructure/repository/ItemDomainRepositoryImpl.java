package com.example.booking.infrastructure.repository;

import com.example.booking.domain.model.BookableItem;
import com.example.booking.domain.repository.ItemDomainRepository;
import com.example.booking.infrastructure.entity.BookableItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface SpringJpaItemRepository extends JpaRepository<BookableItemEntity, Long> {}

@Component
@RequiredArgsConstructor
public class ItemDomainRepositoryImpl implements ItemDomainRepository {

    private final SpringJpaItemRepository jpaRepository;

    @Override
    public List<BookableItem> findAll() {
        return jpaRepository.findAll().stream()
                .map(e -> new BookableItem(e.getId(), e.getTitle(), e.getItemType(), e.getPricePerUnit(), e.getAvailableInventory()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookableItem> findById(Long id) {
        return jpaRepository.findById(id)
                .map(e -> new BookableItem(e.getId(), e.getTitle(), e.getItemType(), e.getPricePerUnit(), e.getAvailableInventory()));
    }

    @Override
    public BookableItem save(BookableItem item) {
        BookableItemEntity entity = new BookableItemEntity(item.getId(), item.getTitle(), item.getItemType(), item.getPricePerUnit(), item.getAvailableInventory());
        BookableItemEntity saved = jpaRepository.save(entity);
        return new BookableItem(saved.getId(), saved.getTitle(), saved.getItemType(), saved.getPricePerUnit(), saved.getAvailableInventory());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
