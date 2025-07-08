package org.acme.product;

import org.acme.core.edaes.Command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegisterProductCommand implements Command {

    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "{aggregateId.pattern}")
    private String aggregateId;

    @NotBlank(message = "{sku.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "{sku.pattern}")
    private String sku;

    @NotBlank(message = "{nome.obrigatorio}")
    @Size(min = 3, max = 40, message = "{nome.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "{nome.pattern}")
    private String name;
}
