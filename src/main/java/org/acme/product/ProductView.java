package org.acme.product;

import org.acme.core.cqrsedaes.projection.Projection;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_view")
public class ProductView extends PanacheEntity implements Projection {

    @Column(nullable = false, unique = true)
    public String productId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String sku;

    public String getId() {
        return productId;
    }
}
