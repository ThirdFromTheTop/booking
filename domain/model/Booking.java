package com.example.booking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private Long userId;
    private BookableItem bookableItem;
    private LocalDateTime bookingDateFrom;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String status; // PENDING, CONFIRMED, CANCELED

    public Booking(Long id, Long userId, BookableItem bookableItem, LocalDateTime bookingDateFrom, Integer quantity, String status) {
        this.id = id;
        this.userId = userId;
        this.bookableItem = bookableItem;
        this.bookingDateFrom = bookingDateFrom;
        this.quantity = quantity;
        this.status = status;
        this.totalPrice = calculateTotalPrice();
    }

    public BigDecimal calculateTotalPrice() {
        if (this.bookableItem == null) return BigDecimal.ZERO;
        return this.bookableItem.getPricePerUnit().multiply(BigDecimal.valueOf(this.quantity));
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public BookableItem getBookableItem() { return bookableItem; }
    public LocalDateTime getBookingDateFrom() { return bookingDateFrom; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
}
