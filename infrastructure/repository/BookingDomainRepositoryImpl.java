package com.example.booking.infrastructure.repository;

import com.example.booking.domain.model.BookableItem;
import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingDomainRepository;
import com.example.booking.infrastructure.entity.BookableItemEntity;
import com.example.booking.infrastructure.entity.BookingEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
interface SpringJpaBookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUserId(Long userId);
}

@Component
@RequiredArgsConstructor
public class BookingDomainRepositoryImpl implements BookingDomainRepository {

    private final SpringJpaBookingRepository jpaRepository;

    @Override
    public Booking save(Booking booking) {
        BookableItem item = booking.getBookableItem();
        BookableItemEntity itemEntity = new BookableItemEntity(
                item.getId(), item.getTitle(), item.getItemType(), item.getPricePerUnit(), item.getAvailableInventory()
        );

        BookingEntity entity = new BookingEntity(
                booking.getId(),
                booking.getUserId(),
                itemEntity,
                booking.getBookingDateFrom(),
                booking.getQuantity(),
                booking.getTotalPrice(),
                booking.getStatus()
        );

        BookingEntity saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Booking booking) {
        jpaRepository.deleteById(booking.getId());
    }

    private Booking mapToDomain(BookingEntity e) {
        BookableItem domainItem = new BookableItem(
                e.getBookableItem().getId(),
                e.getBookableItem().getTitle(),
                e.getBookableItem().getItemType(),
                e.getBookableItem().getPricePerUnit(),
                e.getBookableItem().getAvailableInventory()
        );
        return new Booking(e.getId(), e.getUserId(), domainItem, e.getBookingDateFrom(), e.getQuantity(), e.getStatus());
    }
}
