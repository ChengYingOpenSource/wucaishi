package com.cy.onepush.common.framework.infrastructure.web;

import java.util.List;

public class PaginationResult<T> extends Result<PaginationResult.PaginationResultData<T>> {

    public static <T> Builder<T> paginationBuilder() {
        return new Builder<>();
    }

    protected PaginationResult(int code, String msg, List<T> data, Pagination pagination) {
        super(code, new PaginationResultData<>(data, pagination), msg);
    }

    public static class PaginationResultData<T> {

        public static <T> Builder<T> builder() {
            return new Builder<>();
        }

        private final List<T> list;
        private final Pagination pagination;

        private PaginationResultData(List<T> list, Pagination pagination) {
            this.list = list;
            this.pagination = pagination;
        }

        public List<T> getList() {
            return list;
        }

        public Pagination getPagination() {
            return pagination;
        }

    }

    public static class Builder<T> extends Result.Builder {

        private List<T> list;
        private Pagination pagination;

        public Builder<T> list(List<T> list) {
            this.list = list;
            return this;
        }

        public Builder<T> pagination(int pageNum, int pageSize, long total) {
            this.pagination = new Pagination();
            this.pagination.setPageNum(pageNum);
            this.pagination.setPageSize(pageSize);
            this.pagination.setTotal(total);
            return this;
        }

        @Override
        public PaginationResult<T> build() {
            return new PaginationResult<T>(code, msg, list, pagination);
        }

    }

}