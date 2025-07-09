package org.acme.core.edaes;

import java.util.List;

public interface Command {
    String getId();
    String getType();
    Object getData();
    List<Header> getHeaders();
}
