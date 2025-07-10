package org.acme.product.bysku;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ProductBySkuDto {
    @NotNull(message = "{sku.obrigatorio}")
    @Pattern(regexp = "^[a-zA-Z0-9]{8}$", message = "{sku.pattern}")
    public String sku;

    public ProductBySkuDto(String sku) {
        this.sku = sku;
    }
}
