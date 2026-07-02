package com.vet.animalink.inventario_service.assembler;

import com.vet.animalink.inventario_service.controller.InsumoController;
import com.vet.animalink.inventario_service.dto.InsumoResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InsumoModelAssembler implements RepresentationModelAssembler<InsumoResponse, EntityModel<InsumoResponse>> {

    @Override
    public EntityModel<InsumoResponse> toModel(InsumoResponse insumo) {
        return EntityModel.of(insumo,
                linkTo(methodOn(InsumoController.class).obtener(insumo.id())).withSelfRel(),
                linkTo(methodOn(InsumoController.class).listar()).withRel("insumos"));
    }

    public CollectionModel<EntityModel<InsumoResponse>> toCollection(List<InsumoResponse> insumos) {
        List<EntityModel<InsumoResponse>> items = insumos.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(InsumoController.class).listar()).withSelfRel());
    }
}
