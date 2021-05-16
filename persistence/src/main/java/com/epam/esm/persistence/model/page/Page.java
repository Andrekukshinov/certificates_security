package com.epam.esm.persistence.model.page;

import java.util.List;

/**
 * Interface for retrieving list of information from data source, represented as list.
 *
 * @param <T> - content type that this page contains
 */
public interface Page<T> {
    /**
     * Method that returns list of page content
     *
     * @return list of content
     */
    List<T> getContent();

    /**
     * Method that returns first page number from data source
     *
     * @return first page number
     */
    Integer getFirstPage();

    /**
     * Method that returns last page number from data source
     *
     * @return last page number
     */
    Integer getLastPage();

    /**
     * Method that returns current page number from data source
     *
     * @return current page number
     */
    Integer getPage();

    /**
     * Checks whether current page has next page
     *
     * @return true if next page exists
     */
    boolean hasNext();

    /**
     * Checks whether current page has previous page
     *
     * @return true if previous page exists
     */
    boolean hasPrevious();

    /**
     * Checks whether data source contains required content
     *
     * @return true if data source contains required content
     */
    boolean hasFirst();

    /**
     * Checks whether data source contains required content
     *
     * @return true if data source contains required content
     */
    boolean hasLast();

    /**
     * Method that returns next page number from data source
     *
     * @return next page number
     */
    Integer getNextPage();

    /**
     * Method that returns previous page number from data source
     *
     * @return previous page number
     */
    Integer getPreviousPage();
}
