package com.epam.esm.persistence.entity;

import com.epam.esm.persistence.audit.listeners.EntityListener;
import com.epam.esm.persistence.entity.enums.GiftCertificateStatus;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "gift_certificates")
@EntityListeners(EntityListener.class)
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    private String name;
    private String description;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private GiftCertificateStatus status;
    private Integer duration;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "tags_gift_certificates",
            joinColumns = {
                    @JoinColumn(name = "gift_certificate_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id")
            }
    )
    private Set<Tag> tags;

    public GiftCertificate() {
    }

    public GiftCertificate(
            Long id, LocalDateTime createDate, LocalDateTime lastUpdateDate,
            String name, String description, BigDecimal price, GiftCertificateStatus status,
            Integer duration, Set<Tag> tags) {
        this.id = id;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.duration = duration;
        this.tags = tags;

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

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public GiftCertificateStatus getStatus() {
        return status;
    }

    public void setStatus(GiftCertificateStatus status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public static final Builder getBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", duration=" + duration +
                ", tags=" + tags +
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
        GiftCertificate that = (GiftCertificate) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCreateDate(), that.getCreateDate()) && Objects.equals(getLastUpdateDate(), that.getLastUpdateDate()) && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getPrice(), that.getPrice()) && getStatus() == that.getStatus() && Objects.equals(getDuration(), that.getDuration()) && Objects.equals(getTags(), that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateDate(), getLastUpdateDate(), getName(), getDescription(), getPrice(), getStatus(), getDuration(), getTags());
    }

    public static final class Builder {
        private Long id;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private GiftCertificateStatus status;
        private Set<Tag> tags;

        private Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Builder setStatus(GiftCertificateStatus status) {
            this.status = status;
            return this;
        }


        public GiftCertificate build() {
            return new GiftCertificate(id, createDate, lastUpdateDate, name, description, price, status, duration, tags);
        }


    }
}
