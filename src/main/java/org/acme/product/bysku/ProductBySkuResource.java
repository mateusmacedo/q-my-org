package org.acme.product.bysku;

import org.acme.core.cqrsedaes.cqrs.CommandBus;
import org.acme.core.cqrsedaes.cqrs.Query;
import org.acme.core.cqrsedaes.cqrs.QueryBus;
import org.acme.core.filter.HeaderService;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products/api/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductBySkuResource {

    @Inject
    HeaderService headerService;

    @Inject
    QueryBus queryBus;

    @Inject
    ProductViewRepository productViewRepository;

    @GET
    @Path("/{sku}")
    public Uni<Response> execute(@PathParam("sku") String sku) {
        ProductBySkuDto dto = new ProductBySkuDto(sku);
        Query<ProductBySkuDto> query = ProductViewBySkuQuery.fromDto(dto)
                .withHeaders(headerService.generateDefaultHeaders());

        return queryBus.execute(query)
                .onItem().ifNull().failWith(new NotFoundException())
                .onItem().transform(product -> Response.ok(product).build());
    }
}
