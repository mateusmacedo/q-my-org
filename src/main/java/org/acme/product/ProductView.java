package org.acme.product;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_view")
public class ProductView extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String productId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String sku;
}
