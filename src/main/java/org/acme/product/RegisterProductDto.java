package org.acme.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RegisterProductDto {
    @NotNull(message = "{sku.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "{sku.pattern}")
    public String sku;

    @NotNull(message = "{nome.obrigatorio}")
    @Size(min = 3, max = 40, message = "{nome.size}")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "{nome.pattern}")
    public String name;
}
