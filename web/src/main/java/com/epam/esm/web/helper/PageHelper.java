package com.epam.esm.web.helper;

import com.epam.esm.persistence.model.page.Pageable;

import java.util.Map;

/**
 * Interface for building new parameters based on received params
 */
public interface PageHelper {
    /**
     * Method for building request param map with specified page
     *
     * @param requestParams build from
     * @param page          new page reference
     * @return new request params map
     */
    Map<String, String> getPageParamMap(Map<String, String> requestParams, Integer page);

    /**
     * Method for building request param map with current page
     *
     * @param requestParams build from
     * @param currentPage   current page reference
     * @return new request params map
     */
    Map<String, String> getThisPageParamMap(Map<String, String> requestParams, Integer currentPage);

    /**
     * Method for building request param map with next page
     *
     * @param requestParams build from
     * @param nextPage      next page reference
     * @return new request params map
     */
    Map<String, String> getNextPageParamMap(Map<String, String> requestParams, Integer nextPage);

    /**
     * Method for building request param map with previous page
     *
     * @param requestParams build from
     * @param previousPage  previous page reference
     * @return previous request params map
     */
    Map<String, String> getPreviousPageParamMap(Map<String, String> requestParams, Integer previousPage);

    /**
     * Method for pageable from request param map
     *
     * @param requestParams to build pageable with
     * @return pageable build based on params
     */
    Pageable getPageable(Map<String, String> requestParams);
}
