package com.epam.esm.web.helper;

import com.epam.esm.persistence.model.page.Pageable;
import com.epam.esm.web.exception.InvalidValueException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PageHelperImpl implements PageHelper {
    private static final long DEFAULT_SIZE = 7;
    private static final long DEFAULT_PAGE = 0;

    public Map<String, String> getPageParamMap(Map<String, String> requestParams, Integer page) {
        HashMap<String, String> result = new HashMap<>(requestParams);
        result.put("page", page.toString());
        return result;
    }

    public Map<String, String> getThisPageParamMap(Map<String, String> requestParams, Integer currentPage) {
        return getPageParamMap(requestParams, currentPage + 1);
    }

    public Map<String, String> getNextPageParamMap(Map<String, String> requestParams, Integer nextPage) {
        return getThisPageParamMap(requestParams, nextPage);
    }

    public Map<String, String> getPreviousPageParamMap(Map<String, String> requestParams, Integer previousPage) {
        return getThisPageParamMap(requestParams, previousPage);
    }

    public Pageable getPageable(Map<String, String> requestParams) {
        Long page = getPage(requestParams);
        Long size = getSize(requestParams);
        String sort = requestParams.get("sort");
        String sortDir = requestParams.get("sortDir");
        return new Pageable(Math.toIntExact(page), Math.toIntExact(size), sort, sortDir);
    }

    private Long getSize(Map<String, String> requestParams) {
        String sizeString = requestParams.get("size");
        Long value;
        if (sizeString == null) {
            return DEFAULT_SIZE;
        } else if ((sizeString.matches("[1-9][0-9]*")) && (Integer.MAX_VALUE > (value = Long.valueOf(sizeString)))) {
            return value;
        } else {
            throw new InvalidValueException("size must be a number between zero and " + Integer.MAX_VALUE);
        }
    }

    private Long getPage(Map<String, String> requestParams) {
        String sizeString = requestParams.get("page");
        Long value;
        if (sizeString == null) {
            return DEFAULT_PAGE;
        } else if ((sizeString.matches("[1-9][0-9]*")) && (Integer.MAX_VALUE > (value = Long.valueOf(sizeString)))) {
            return value;
        } else {
            throw new InvalidValueException("page must be a number between zero and " + Integer.MAX_VALUE);
        }
    }

}
