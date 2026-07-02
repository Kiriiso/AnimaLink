package com.vet.animalink.historial_service.assembler;

import com.vet.animalink.historial_service.controller.HistorialController;
import com.vet.animalink.historial_service.dto.HistorialResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HistorialModelAssembler implements RepresentationModelAssembler<HistorialResponse, EntityModel<HistorialResponse>> {

    @Override
    public EntityModel<HistorialResponse> toModel(HistorialResponse historial) {
        return EntityModel.of(historial,
                linkTo(methodOn(HistorialController.class).obtener(historial.id())).withSelfRel(),
                linkTo(methodOn(HistorialController.class).listar()).withRel("historiales"));
    }

    public CollectionModel<EntityModel<HistorialResponse>> toCollection(List<HistorialResponse> historiales) {
        List<EntityModel<HistorialResponse>> items = historiales.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(HistorialController.class).listar()).withSelfRel());
    }
}
