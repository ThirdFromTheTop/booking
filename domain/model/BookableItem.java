package com.example.booking.domain.model;

import java.math.BigDecimal;

public class BookableItem {
    private Long id;
    private String title;
    private String itemType; // HOTEL, FLIGHT, TRAIN, BUS
    private BigDecimal pricePerUnit;
    private Integer availableInventory;

    public BookableItem(Long id, String title, String itemType, BigDecimal pricePerUnit, Integer availableInventory) {
        this.id = id;
        this.title = title;
        this.itemType = itemType;
        this.pricePerUnit = pricePerUnit;
        this.availableInventory = availableInventory;
    }

    public void reserveSeats(int quantity) {
        if (this.availableInventory < quantity) {
            throw new IllegalArgumentException("Недостаточно свободных мест!");
        }
        this.availableInventory -= quantity;
    }

    public void releaseSeats(int quantity) {
        this.availableInventory += quantity;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getItemType() { return itemType; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public Integer getAvailableInventory() { return availableInventory; }
    
    public void setTitle(String title) { this.title = title; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public void setAvailableInventory(Integer availableInventory) { this.availableInventory = availableInventory; }
}
