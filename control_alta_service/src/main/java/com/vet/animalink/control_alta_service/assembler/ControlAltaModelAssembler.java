package com.vet.animalink.control_alta_service.assembler;

import com.vet.animalink.control_alta_service.controller.ControlAltaController;
import com.vet.animalink.control_alta_service.dto.ControlAltaResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ControlAltaModelAssembler implements RepresentationModelAssembler<ControlAltaResponse, EntityModel<ControlAltaResponse>> {

    @Override
    public EntityModel<ControlAltaResponse> toModel(ControlAltaResponse control) {
        return EntityModel.of(control,
                linkTo(methodOn(ControlAltaController.class).obtener(control.id())).withSelfRel(),
                linkTo(methodOn(ControlAltaController.class).listar()).withRel("controles"));
    }

    public CollectionModel<EntityModel<ControlAltaResponse>> toCollection(List<ControlAltaResponse> controles) {
        List<EntityModel<ControlAltaResponse>> items = controles.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(ControlAltaController.class).listar()).withSelfRel());
    }
}
