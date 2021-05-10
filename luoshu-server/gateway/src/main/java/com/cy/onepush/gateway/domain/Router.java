package com.cy.onepush.gateway.domain;

import com.cy.onepush.common.framework.domain.AbstractEntity;

import java.net.URI;
import java.net.URISyntaxException;

public class Router extends AbstractEntity<String> {

    private MethodType method;
    private URI uri;

    public void defineUri(String method, String uri) {
        this.method = MethodType.valueOf(method);

        try {
            this.uri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("the uri %s is illegal", uri));
        }
    }

    public URI getUri() {
        return uri;
    }

    public MethodType getMethod() {
        return method;
    }
}
