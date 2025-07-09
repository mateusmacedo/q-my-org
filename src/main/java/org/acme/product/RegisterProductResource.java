package org.acme.product;

import org.acme.core.edaes.Command;
import org.acme.core.filter.HeaderService;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
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

    @POST
    public Uni<Response> registerProduct(@Valid RegisterProductDto dto) {
        Command<RegisterProductDto> command = RegisterProductCommand.fromDto(dto)
        .withHeaders(headerService.generateDefaultHeaders());

        return Uni.createFrom().item(Response.created(null).entity(command.getData()).build());
    }
}
