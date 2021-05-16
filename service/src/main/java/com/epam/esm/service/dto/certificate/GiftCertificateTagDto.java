package com.epam.esm.service.dto.certificate;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.validation.SaveGroup;
import com.epam.esm.service.validation.UpdateGroup;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class GiftCertificateTagDto extends RepresentationModel<GiftCertificateTagDto> {
    @Null(message = "certificate id must not be specified", groups = SaveGroup.class)
    @Min(value = 1, message = "certificate id must be positive", groups = UpdateGroup.class)
    private Long id;

    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @NotNull(message = "certificate description must be specified", groups = {UpdateGroup.class, SaveGroup.class})
    @NotBlank(message = "certificate description must be specified", groups = {UpdateGroup.class, SaveGroup.class})
    @Size(max = 255, min = 1, message = "certificate description max length must be less than 256 symbol and more than 1!")
    private String description;

    @NotBlank(message = "certificate name must be specified", groups = {UpdateGroup.class, SaveGroup.class})
    @Size(max = 50, min = 1, message = "certificate name max length must be less than 51 symbol and more than 0!")
    private String name;

    @DecimalMin(value = "0", message = "certificate price cannot be negative")
    @NotNull(message = "certificate price must be specified", groups = {UpdateGroup.class, SaveGroup.class})
    private BigDecimal price;

    @Min(value = 0, message = "certificate duration cannot be negative")
    @NotNull(message = "certificate duration must be specified", groups = {UpdateGroup.class, SaveGroup.class})
    private Integer duration;

    private Set<TagDto> tags;

    public GiftCertificateTagDto() {
    }

    private GiftCertificateTagDto(Long id, LocalDateTime createDate, LocalDateTime lastUpdateDate, String name, String description, BigDecimal price, Integer duration, Set<TagDto> tags) {
        this.id = id;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.name = name;
        this.description = description;
        this.price = price;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiftCertificateTagDto that = (GiftCertificateTagDto) o;
        return getId().equals(that.getId()) && getCreateDate().equals(that.getCreateDate()) && getLastUpdateDate().equals(that.getLastUpdateDate()) && getName().equals(that.getName()) && getDescription().equals(that.getDescription()) && getPrice().equals(that.getPrice()) && getDuration().equals(that.getDuration()) && getTags().equals(that.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateDate(), getLastUpdateDate(), getName(), getDescription(), getPrice(), getDuration(), getTags());
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private Set<TagDto> tags;

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

        public Builder setTags(Set<TagDto> tags) {
            this.tags = tags;
            return this;
        }

        public GiftCertificateTagDto build() {
            return new GiftCertificateTagDto(id, createDate, lastUpdateDate, name, description, price, duration, tags);
        }
    }
}
