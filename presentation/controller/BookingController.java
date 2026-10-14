package com.example.booking.presentation.controller;

import com.example.booking.domain.model.BookableItem;
import com.example.booking.domain.model.Booking;
import com.example.booking.domain.service.BookingUseCase;
import com.example.booking.infrastructure.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingUseCase bookingUseCase;
    private final JwtService jwtService;

    @PostMapping("/bookings/create")
    public ResponseEntity<Booking> createBooking(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam Long itemId, 
            @RequestBody BookingRequest req) {
        
        String token = tokenHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);
        
        Booking booking = bookingUseCase.executeCreateBooking(userId, itemId, req.getQuantity());
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/bookings/my")
    public ResponseEntity<List<Booking>> getMyBookings(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        Claims claims = jwtService.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);
        
        return ResponseEntity.ok(bookingUseCase.getBookingsByUserId(userId));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingUseCase.executeCancelBooking(id);
        return ResponseEntity.ok("Бронирование успешно отменено.");
    }


    @GetMapping("/items")
    public ResponseEntity<List<BookableItem>> getAllItems() {
        return ResponseEntity.ok(bookingUseCase.getAllAvailableItems());
    }

    @PostMapping("/items")
    public ResponseEntity<BookableItem> createItem(@RequestBody BookableItem item) {
        return ResponseEntity.ok(bookingUseCase.createItem(item));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        bookingUseCase.deleteItem(id);
        return ResponseEntity.ok("Объект успешно удален из базы данных.");
    }
}

@Data
class BookingRequest {
    private int quantity;
}
