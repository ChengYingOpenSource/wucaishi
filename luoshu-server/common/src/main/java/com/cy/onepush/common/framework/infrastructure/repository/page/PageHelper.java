package com.cy.onepush.common.framework.infrastructure.repository.page;

import com.github.pagehelper.ISelect;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PageHelper {

    public static <T> PageInfo<T> doSelectPage(int pageNum, int pageSize, ISelect select) {
        final com.github.pagehelper.PageInfo<T> raw = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize)
            .doSelectPageInfo(select);

        final PageInfo<T> pageInfo = new PageInfo<>(raw.getPageNum(), raw.getPageSize());
        pageInfo.setTotal(raw.getTotal());
        pageInfo.setList(raw.getList());
        return pageInfo;
    }

    public static <T> PageInfo<T> doSelectPageInMemory(int pageNum, int pageSize, Provider<T> provider, Comparator<T> comparator) {
        final List<T> data = provider.provide();
        if (CollectionUtils.isEmpty(data)) {
            return getEmptyPageInfo(pageNum, pageSize);
        }

        // transfer to new collection
        List<T> newData = new ArrayList<>(data);

        // sort
        newData.sort(comparator);

        // page it.
        int startIndex = (pageNum - 1) * pageSize;
        int endExclusionIndex = Math.min(pageNum * pageSize, data.size());

        // start and end index check
        if (startIndex > endExclusionIndex) {
            return getEmptyPageInfo(pageNum, pageSize);
        }

        final List<T> segment = newData.subList(startIndex, endExclusionIndex);

        final PageInfo<T> pageInfo = new PageInfo<>(pageNum, pageSize);
        pageInfo.setTotal(newData.size());
        pageInfo.setList(segment);
        return pageInfo;
    }

    private static <T> PageInfo<T> getEmptyPageInfo(int pageNum, int pageSize) {
        final PageInfo<T> pageInfo = new PageInfo<>(pageNum, pageSize);
        pageInfo.setTotal(0);
        pageInfo.setList(Collections.emptyList());
        return pageInfo;
    }

    private PageHelper() {
    }

    public interface Provider<T> {

        List<T> provide();

    }

}
