package com.epam.esm.persistence.model.page;

import java.util.Objects;

public class Pageable {
    private boolean isPaged = true;
    private Integer page;
    private Integer size;
    private String sort;
    private String sortDir;

    private Pageable() {
        isPaged = false;
    }

    public static Pageable unpaged() {
        Pageable pageable = new Pageable(0, 1, null, null);
        pageable.isPaged = false;
        return pageable;
    }

    public Pageable(Integer page, Integer size, String sort, String sortDir) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.sortDir = sortDir;
    }

    public boolean isPaged() {
        return isPaged;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public String getSort() {
        return sort;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setPaged(boolean paged) {
        isPaged = paged;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    @Override
    public String toString() {
        return "Pageable{" +
                "isPaged=" + isPaged +
                ", page=" + page +
                ", size=" + size +
                ", sort='" + sort + '\'' +
                ", sortDir='" + sortDir + '\'' +
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
        Pageable pageable = (Pageable) o;
        return isPaged() == pageable.isPaged() && Objects.equals(getPage(), pageable.getPage()) && Objects.equals(getSize(), pageable.getSize()) && Objects.equals(getSort(), pageable.getSort()) && Objects.equals(getSortDir(), pageable.getSortDir());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPaged(), getPage(), getSize(), getSort(), getSortDir());
    }
}
