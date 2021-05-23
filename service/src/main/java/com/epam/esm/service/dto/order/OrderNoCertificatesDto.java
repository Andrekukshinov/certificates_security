package com.epam.esm.service.dto.order;

import com.epam.esm.persistence.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderNoCertificatesDto {
    private Long id;
    private LocalDateTime createDate;
    private BigDecimal totalPrice;
    private OrderStatus status;

    public OrderNoCertificatesDto() {
    }

    public OrderNoCertificatesDto(Long id, LocalDateTime createDate, BigDecimal totalPrice, OrderStatus status) {
        this.id = id;
        this.createDate = createDate;
        this.totalPrice = totalPrice;
        this.status = status;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderNoCertificatesDto that = (OrderNoCertificatesDto) o;
        return Objects.equals(id, that.id) && Objects.equals(createDate, that.createDate) && Objects.equals(totalPrice, that.totalPrice) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, totalPrice, status);
    }

    @Override
    public String toString() {
        return "OrderNoCertificatesDto{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }
}
