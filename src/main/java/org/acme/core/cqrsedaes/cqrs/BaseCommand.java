package org.acme.core.cqrsedaes.cqrs;

import java.util.List;

import org.acme.core.Header;

public abstract class BaseCommand<T> implements Command<T> {

    protected T data = null;

    protected List<Header> headers = List.of();

    public BaseCommand<T> withData(T data) {
        this.data = data;
        return this;
    }

    public BaseCommand<T> withHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public T getData() {
        return data;
    }
}
