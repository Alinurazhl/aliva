package com.aliva.aliva.dto;

import jakarta.validation.constraints.*;

public class ProductDto {

    @NotBlank(message = "Название товара обязательно")
    private String name;

    @NotBlank(message = "Бренд обязателен")
    private String brand;

    @NotNull(message = "Цена обязательна")
    @Positive(message = "Цена должна быть больше 0")
    private Double price;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotBlank(message = "Цвет обязателен")
    private String color;

    @NotBlank(message = "Размер обязателен")
    private String size;

    @NotNull(message = "Количество товара обязательно")
    @PositiveOrZero(message = "Количество не может быть отрицательным")
    private Integer stock;

    private String image;

    private Long categoryId;

    public ProductDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}