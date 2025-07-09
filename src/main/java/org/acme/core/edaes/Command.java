package org.acme.core.edaes;

import java.util.List;

public interface Command<T> {
    T getData();
    List<Header> getHeaders();
}
