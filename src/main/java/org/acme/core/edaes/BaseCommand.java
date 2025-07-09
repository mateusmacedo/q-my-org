package org.acme.core.edaes;

import java.util.List;

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
