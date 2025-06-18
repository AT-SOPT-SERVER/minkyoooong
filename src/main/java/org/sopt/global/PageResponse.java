package org.sopt.global;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {

    private int page;
    private Boolean hasNext;
    private Integer totalPages;
    private T result;

    public PageResponse(int page, Boolean hasNext, Integer totalPages, T result) {
        this.page = page;
        this.hasNext = hasNext;
        this.totalPages = totalPages;
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public T getResult() {
        return result;
    }
}