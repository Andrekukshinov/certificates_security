package com.epam.esm.service.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDetailsDto {
    private Long id;
    private LocalDateTime createDate;
    private BigDecimal totalPrice;

    public OrderDetailsDto() {
    }

    public OrderDetailsDto(Long id, LocalDateTime createDate, BigDecimal totalPrice) {
        this.id = id;
        this.createDate = createDate;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDetailsDto that = (OrderDetailsDto) o;
        return Objects.equals(id, that.id) && Objects.equals(createDate, that.createDate) && Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderDetailsDto{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
