package org.acme.core.cqrsedaes.cqrs;

import java.util.List;

import org.acme.core.Header;

public abstract class BaseQuery<T> implements Query<T> {

    protected T data = null;

    protected List<Header> headers = List.of();

    public BaseQuery<T> withData(T data) {
        this.data = data;
        return this;
    }

    public BaseQuery<T> withHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public List<Header> getHeaders() {
        return headers;
    }

    @Override
    public T getData() {
        return data;
    }
}