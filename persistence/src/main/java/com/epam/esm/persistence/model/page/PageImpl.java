package com.epam.esm.persistence.model.page;

import java.util.List;
import java.util.Objects;

public class PageImpl<T> implements Page<T> {
    private final List<T> content;
    private final Integer lastPage;
    private final Integer firstPage;
    private final Integer page;
    private final Integer nextPage;
    private final Integer previousPage;

    public PageImpl(List<T> content, Pageable pageable, Integer lastPage) {
        this.content = content;
        this.page = pageable.getPage();
        this.lastPage = lastPage;
        this.firstPage = 1;
        this.nextPage = page + 1;
        this.previousPage = page - 1;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public Integer getPage() {
        return page;
    }

    @Override
    public Integer getLastPage() {
        return lastPage;
    }

    @Override
    public Integer getFirstPage() {
        return firstPage;
    }

    @Override
    public Integer getNextPage() {
        return nextPage;
    }

    @Override
    public Integer getPreviousPage() {
        return previousPage;
    }

    @Override
    public boolean hasPrevious() {
        return page > 0 && page < (lastPage);
    }

    @Override
    public boolean hasFirst() {
        return lastPage > 0;
    }

    @Override
    public boolean hasLast() {
        return hasFirst();
    }

    @Override
    public boolean hasNext() {
        return page < lastPage - 1 && page > -1;
    }

    @Override
    public String toString() {
        return "PageImpl{" +
                "content=" + content +
                ", lastPage=" + lastPage +
                ", firstPage=" + firstPage +
                ", page=" + page +
                ", nextPage=" + nextPage +
                ", previousPage=" + previousPage +
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
        PageImpl<?> page1 = (PageImpl<?>) o;
        return Objects.equals(getContent(), page1.getContent()) && Objects.equals(getLastPage(), page1.getLastPage()) && Objects.equals(getFirstPage(), page1.getFirstPage()) && Objects.equals(getPage(), page1.getPage()) && Objects.equals(getNextPage(), page1.getNextPage()) && Objects.equals(getPreviousPage(), page1.getPreviousPage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContent(), getLastPage(), getFirstPage(), getPage(), getNextPage(), getPreviousPage());
    }
}
