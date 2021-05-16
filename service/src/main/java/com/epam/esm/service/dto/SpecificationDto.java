package com.epam.esm.service.dto;

import com.epam.esm.persistence.model.enums.SortDirection;

import java.util.List;
import java.util.Objects;

public class SpecificationDto {
    private List<String> tagNames;
    private String certificateDescription;
    private String certificateName;
    private SortDirection nameSortDir;
    private SortDirection createDateSortDir;

    public SpecificationDto(List<String> tagNames, String certificateDescription, String certificateName, SortDirection nameSortDir, SortDirection createDateSortDir) {
        this.tagNames = tagNames;
        this.certificateDescription = certificateDescription;
        this.certificateName = certificateName;
        this.nameSortDir = nameSortDir;
        this.createDateSortDir = createDateSortDir;
    }

    public SpecificationDto() {
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public String getCertificateDescription() {
        return certificateDescription;
    }

    public void setCertificateDescription(String certificateDescription) {
        this.certificateDescription = certificateDescription;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public SortDirection getNameSortDir() {
        return nameSortDir;
    }

    public void setNameSortDir(SortDirection nameSortDir) {
        this.nameSortDir = nameSortDir;
    }

    public SortDirection getCreateDateSortDir() {
        return createDateSortDir;
    }

    public void setCreateDateSortDir(SortDirection createDateSortDir) {
        this.createDateSortDir = createDateSortDir;
    }

    @Override
    public String toString() {
        return "SpecificationDto{" +
                "tagNames=" + tagNames +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", certificateName='" + certificateName + '\'' +
                ", nameSortDir=" + nameSortDir +
                ", createDateSortDir=" + createDateSortDir +
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
        SpecificationDto that = (SpecificationDto) o;
        return Objects.equals(getTagNames(), that.getTagNames()) && Objects.equals(getCertificateDescription(), that.getCertificateDescription()) && Objects.equals(getCertificateName(), that.getCertificateName()) && getNameSortDir() == that.getNameSortDir() && getCreateDateSortDir() == that.getCreateDateSortDir();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagNames(), getCertificateDescription(), getCertificateName(), getNameSortDir(), getCreateDateSortDir());
    }
}
