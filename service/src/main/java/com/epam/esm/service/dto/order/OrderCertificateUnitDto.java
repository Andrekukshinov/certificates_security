package com.epam.esm.service.dto.order;

import com.epam.esm.service.dto.certificate.GiftCertificatesNoTagDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Objects;

public class OrderCertificateUnitDto {
    @Null(message = "id must be null")
    private Long id;
    @NotNull(message = "certificate must not be null")
    @Valid
    private GiftCertificatesNoTagDto orderCertificate;
    @Min(value = 1, message = "certificate amount cannot be less than 1!")
    @NotNull(message = "certificate quantity must not be null")
    private Integer quantity;

    public OrderCertificateUnitDto(Long id, GiftCertificatesNoTagDto orderCertificate, Integer quantity) {
        this.id = id;
        this.orderCertificate = orderCertificate;
        this.quantity = quantity;
    }

    public OrderCertificateUnitDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GiftCertificatesNoTagDto getOrderCertificate() {
        return orderCertificate;
    }

    public void setOrderCertificate(GiftCertificatesNoTagDto orderCertificate) {
        this.orderCertificate = orderCertificate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderCertificateUnitDto{" +
                "id=" + id +
                ", orderCertificate=" + orderCertificate +
                ", quantity=" + quantity +
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
        OrderCertificateUnitDto that = (OrderCertificateUnitDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrderCertificate(), that.getOrderCertificate()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderCertificate(), getQuantity());
    }
}
