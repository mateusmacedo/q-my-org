package org.acme.core.cqrsedaes.cqrs;

import java.io.Serializable;
import java.util.List;

import org.acme.core.Header;

public interface Command<T> extends Serializable {
    T getData();
    List<Header> getHeaders();
}
