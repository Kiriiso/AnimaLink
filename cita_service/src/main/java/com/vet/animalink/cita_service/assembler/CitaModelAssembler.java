package com.vet.animalink.cita_service.assembler;

import com.vet.animalink.cita_service.controller.CitaController;
import com.vet.animalink.cita_service.dto.CitaResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CitaModelAssembler implements RepresentationModelAssembler<CitaResponse, EntityModel<CitaResponse>> {

    @Override
    public EntityModel<CitaResponse> toModel(CitaResponse cita) {
        return EntityModel.of(cita,
                linkTo(methodOn(CitaController.class).obtener(cita.id())).withSelfRel(),
                linkTo(methodOn(CitaController.class).listar()).withRel("citas"));
    }

    public CollectionModel<EntityModel<CitaResponse>> toCollection(List<CitaResponse> citas) {
        List<EntityModel<CitaResponse>> items = citas.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(CitaController.class).listar()).withSelfRel());
    }
}
