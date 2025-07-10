package org.acme.product;

import org.acme.core.cqrsedaes.cqrs.Command;
import org.acme.core.cqrsedaes.cqrs.CommandBus;
import org.acme.core.filter.HeaderService;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.NotFoundException;

import org.acme.product.ProductViewRepository;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products/api/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegisterProductResource {

    @Inject
    HeaderService headerService;

    @Inject
    CommandBus commandBus;

    @Inject
    ProductViewRepository productViewRepository;

    @POST
    @WithTransaction
    public Uni<Response> registerProduct(@Valid RegisterProductDto dto) {
        Command<RegisterProductDto> command = RegisterProductCommand.fromDto(dto)
        .withHeaders(headerService.generateDefaultHeaders());

        return commandBus.dispatch(command)
            .onItem().transform(ignored -> Response.created(null).entity(command.getData()).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getProduct(@PathParam("id") String id) {
        return productViewRepository.findById(id)
                .onItem().ifNull().failWith(new NotFoundException())
                .onItem().transform(product -> Response.ok(product).build());
    }
}
