package com.vet.animalink.cliente_service.assembler;

import com.vet.animalink.cliente_service.controller.ClienteController;
import com.vet.animalink.cliente_service.dto.ClienteResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Agrega enlaces HATEOAS (self, colección) a las respuestas de cliente,
 * haciendo el API navegable y trazable.
 */
@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteResponse, EntityModel<ClienteResponse>> {

    @Override
    public EntityModel<ClienteResponse> toModel(ClienteResponse cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtener(cliente.id())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listar()).withRel("clientes"));
    }

    public CollectionModel<EntityModel<ClienteResponse>> toCollection(List<ClienteResponse> clientes) {
        List<EntityModel<ClienteResponse>> items = clientes.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(ClienteController.class).listar()).withSelfRel());
    }
}
