package com.example.booking.domain.service;

import com.example.booking.domain.model.BookableItem;
import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingDomainRepository;
import com.example.booking.domain.repository.ItemDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingUseCase {

    private final BookingDomainRepository bookingRepository;
    private final ItemDomainRepository itemRepository;

    @Transactional
    public Booking executeCreateBooking(Long userId, Long itemId, int quantity) {
        BookableItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Объект бронирования не найден"));

        item.reserveSeats(quantity);
        itemRepository.save(item);

        Booking booking = new Booking(null, userId, item, LocalDateTime.now(), quantity, "PENDING");
        return bookingRepository.save(booking);
    }

    @Transactional
    public void executeCancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));
        
        BookableItem item = booking.getBookableItem();
        item.releaseSeats(booking.getQuantity());
        itemRepository.save(item);

        bookingRepository.delete(booking);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<BookableItem> getAllAvailableItems() {
        return itemRepository.findAll();
    }

    public BookableItem createItem(BookableItem item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
