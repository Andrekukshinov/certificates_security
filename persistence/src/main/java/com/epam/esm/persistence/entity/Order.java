package com.epam.esm.persistence.entity;


import com.epam.esm.persistence.audit.listeners.EntityListener;
import com.epam.esm.persistence.entity.enums.OrderStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "orders")
@EntityListeners(EntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderCertificate> orderCertificates;

    public Order(Long id, LocalDateTime createDate, OrderStatus status, BigDecimal totalPrice, User user, List<OrderCertificate> orderCertificates) {
        this.id = id;
        this.createDate = createDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.user = user;
        this.orderCertificates = orderCertificates;
    }

    public Order() {
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderCertificate> getOrderCertificates() {
        return orderCertificates;
    }

    public void setOrderCertificates(List<OrderCertificate> orderCertificates) {
        this.orderCertificates = orderCertificates;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                ", orderCertificates=" + orderCertificates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId()) && Objects.equals(getCreateDate(), order.getCreateDate()) && getStatus() == order.getStatus() && Objects.equals(getTotalPrice(), order.getTotalPrice()) && Objects.equals(getUser(), order.getUser()) && Objects.equals(getOrderCertificates(), order.getOrderCertificates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateDate(), getStatus(), getTotalPrice(), getUser(), getOrderCertificates());
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Long id;
        private LocalDateTime createDate;
        private OrderStatus status;
        private BigDecimal totalPrice;
        private User user;
        private List<OrderCertificate> orderCertificates;

        public OrderBuilder() {
        }

        private OrderBuilder(Long id, LocalDateTime createDate, OrderStatus status, BigDecimal totalPrice, User user, List<OrderCertificate> orderCertificates) {
            this.id = id;
            this.createDate = createDate;
            this.status = status;
            this.totalPrice = totalPrice;
            this.user = user;
            this.orderCertificates = orderCertificates;
        }

        public OrderBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public OrderBuilder setStatus(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderBuilder setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public OrderBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public OrderBuilder setOrderCertificates(List<OrderCertificate> orderCertificates) {
            this.orderCertificates = orderCertificates;
            return this;
        }

        public Order build() {
            return new Order(id, createDate, status, totalPrice, user, orderCertificates);
        }
    }

}
