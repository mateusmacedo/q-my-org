package org.acme.product.bysku;

import org.acme.core.cqrsedaes.cqrs.BaseQuery;

public class ProductViewBySkuQuery extends BaseQuery<ProductBySkuDto>{

    private ProductViewBySkuQuery() {
    }

    public static ProductViewBySkuQuery fromDto(ProductBySkuDto dto) {
        return (ProductViewBySkuQuery) new ProductViewBySkuQuery().withData(dto);
    }
}