package org.acme.product;

import java.util.List;
import java.util.Map;

import org.acme.core.edaes.Command;
import org.acme.core.edaes.Header;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterProductCommand implements Command {

    @NotNull(message = "{sku.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "{sku.pattern}")
    String sku;

    @NotNull(message = "{nome.obrigatorio}")
    @Size(min = 3, max = 40, message = "{nome.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "{nome.pattern}")
    String name;

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "{id.pattern}")
    private String id;

    private String type = RegisterProductCommand.class.getSimpleName();

    public RegisterProductCommand(
            String id,
            String sku,
            String name) {
        this.id = id;
        this.sku = sku;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return Map.of(
            "sku", sku,
            "name", name
        );
    }

    public List<Header> getHeaders() {
        return List.of();
    }
}
