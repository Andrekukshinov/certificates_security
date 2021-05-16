package com.epam.esm.service.dto.order;

import com.epam.esm.persistence.entity.enums.OrderStatus;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class OrderCertificatesDto extends RepresentationModel<OrderCertificatesDto> {
    @Null(message = "order id must not be specified")
    private Long id;
    private LocalDateTime createDate;
    private BigDecimal totalPrice;
    private Long userId;
    @Valid
    @NotEmpty(message = "certificate amount must be > 1!")
    @NotNull(message = "certificates must be specified")
    private Set<OrderCertificateUnitDto> orderCertificates;

    public OrderCertificatesDto() {
    }

    public OrderCertificatesDto(Long id, LocalDateTime createDate, BigDecimal totalPrice, Long userId, Set<OrderCertificateUnitDto> orderCertificates) {
        this.id = id;
        this.createDate = createDate;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderCertificates = orderCertificates;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<OrderCertificateUnitDto> getOrderCertificates() {
        return orderCertificates;
    }

    public void setOrderCertificates(Set<OrderCertificateUnitDto> orderCertificates) {
        this.orderCertificates = orderCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderCertificatesDto orderCertificatesDto = (OrderCertificatesDto) o;
        return Objects.equals(getId(), orderCertificatesDto.getId()) && Objects.equals(getCreateDate(), orderCertificatesDto.getCreateDate()) && Objects.equals(getTotalPrice(), orderCertificatesDto.getTotalPrice()) && Objects.equals(getUserId(), orderCertificatesDto.getUserId()) && Objects.equals(getOrderCertificates(), orderCertificatesDto.getOrderCertificates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateDate(), getTotalPrice(), getUserId(), getOrderCertificates());
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", totalPrice=" + totalPrice +
                ", userId=" + userId +
                ", orderCertificates=" + orderCertificates +
                '}';
    }

    public static OrderCertificatesDtoBuilder builder() {
        return new OrderCertificatesDtoBuilder();
    }

    public static final class OrderCertificatesDtoBuilder {
        private Long id;
        private LocalDateTime createDate;
        private BigDecimal totalPrice;
        private OrderStatus status;
        private Long userId;
        private Set<OrderCertificateUnitDto> orderCertificates;

        public OrderCertificatesDtoBuilder() {
        }

        private OrderCertificatesDtoBuilder(Long id, LocalDateTime createDate, BigDecimal totalPrice, OrderStatus status, Long userId, Set<OrderCertificateUnitDto> orderCertificates) {
            this.id = id;
            this.createDate = createDate;
            this.totalPrice = totalPrice;
            this.userId = userId;
            this.orderCertificates = orderCertificates;
        }

        public OrderCertificatesDtoBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderCertificatesDtoBuilder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public OrderCertificatesDtoBuilder setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderCertificatesDtoBuilder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public OrderCertificatesDtoBuilder setOrderCertificates(Set<OrderCertificateUnitDto> orderCertificates) {
            this.orderCertificates = orderCertificates;
            return this;
        }

        public OrderCertificatesDto build() {
            return new OrderCertificatesDto(id, createDate, totalPrice, userId, orderCertificates);
        }
    }
}
