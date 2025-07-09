package org.acme.core.edaes;

import java.io.Serializable;
import java.util.List;

public interface Command<T> extends Serializable {
    T getData();
    List<Header> getHeaders();
}
