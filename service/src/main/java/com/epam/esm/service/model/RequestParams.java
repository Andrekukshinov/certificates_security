package com.epam.esm.service.model;

import java.util.List;
import java.util.Set;

public class RequestParams {
    private Set<String> tagNames;
    private List<Long> ids;
    private String certificateDescription;
    private String certificateName;

    private RequestParams(Set<String> tagNames, List<Long> ids, String certificateDescription, String certificateName) {
        this.tagNames = tagNames;
        this.ids = ids;
        this.certificateDescription = certificateDescription;
        this.certificateName = certificateName;
    }

    public RequestParams() {
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

    public static RequestParamsBuilder builder() {
        return new RequestParamsBuilder();
    }

    public static final class RequestParamsBuilder {
        private Set<String> tagNames;
        private List<Long> ids;
        private String certificateDescription;
        private String certificateName;

        private RequestParamsBuilder() {
        }

        public RequestParamsBuilder setTagNames(Set<String> tagNames) {
            this.tagNames = tagNames;
            return this;
        }

        public RequestParamsBuilder setIds(List<Long> ids) {
            this.ids = ids;
            return this;
        }

        public RequestParamsBuilder setCertificateDescription(String certificateDescription) {
            this.certificateDescription = certificateDescription;
            return this;
        }

        public RequestParamsBuilder setCertificateName(String certificateName) {
            this.certificateName = certificateName;
            return this;
        }

        public RequestParams build() {
            return new RequestParams(tagNames, ids, certificateDescription, certificateName);
        }
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "tagNames=" + tagNames +
                ", ids=" + ids +
                ", certificateDescription='" + certificateDescription + '\'' +
                ", certificateName='" + certificateName + '\'' +
                '}';
    }
}
