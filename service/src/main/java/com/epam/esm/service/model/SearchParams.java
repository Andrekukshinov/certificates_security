package com.epam.esm.service.model;

import java.util.List;
import java.util.Set;

public class SearchParams {
    private Set<String> tagNames;
    private List<Long> ids;
    private String certificateDescription;
    private String certificateName;


    private SearchParams(Set<String> tagNames, List<Long> ids, String certificateDescription, String certificateName) {
        this.tagNames = tagNames;
        this.ids = ids;
        this.certificateDescription = certificateDescription;
        this.certificateName = certificateName;
    }

    public SearchParams() {
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<String> tagNames) {
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

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public static SearchParamsBuilder builder() {
        return new SearchParamsBuilder();
    }

    public static final class SearchParamsBuilder {
        private Set<String> tagNames;
        private List<Long> ids;
        private String certificateDescription;
        private String certificateName;

        private SearchParamsBuilder() {
        }

        public SearchParamsBuilder setTagNames(Set<String> tagNames) {
            this.tagNames = tagNames;
            return this;
        }

        public SearchParamsBuilder setIds(List<Long> ids) {
            this.ids = ids;
            return this;
        }

        public SearchParamsBuilder setCertificateDescription(String certificateDescription) {
            this.certificateDescription = certificateDescription;
            return this;
        }

        public SearchParamsBuilder setCertificateName(String certificateName) {
            this.certificateName = certificateName;
            return this;
        }

        public SearchParams build() {
            return new SearchParams(tagNames, ids, certificateDescription, certificateName);
        }
    }
}
