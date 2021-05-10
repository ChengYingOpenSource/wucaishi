package com.cy.onepush.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.util.UriEncoder;

public class URLAppender {

    public static URLAppender getInstance() {
        return new URLAppender();
    }

    private static final String SPLIT = "/";

    private final StringBuilder uriBuilder;

    public URLAppender append(String segment) {
        if (StringUtils.isBlank(segment)) {
            return this;
        }

        String tmp = segment;
        tmp = tmp.startsWith(SPLIT) ? tmp.substring(1) : tmp;
        tmp = tmp.endsWith(SPLIT) ? tmp : (tmp + SPLIT);

        uriBuilder.append(UriEncoder.encode(tmp));
        return this;
    }

    public String build() {
        return build(false);
    }

    public String build(boolean endsWithSplit) {
        return uriBuilder.substring(0, endsWithSplit ? uriBuilder.length() : uriBuilder.length() - 1);
    }

    private URLAppender() {
        this.uriBuilder = new StringBuilder(SPLIT);
    }

}
