package com.vet.animalink.mascota_service.assembler;

import com.vet.animalink.mascota_service.controller.MascotaController;
import com.vet.animalink.mascota_service.dto.MascotaResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MascotaModelAssembler implements RepresentationModelAssembler<MascotaResponse, EntityModel<MascotaResponse>> {

    @Override
    public EntityModel<MascotaResponse> toModel(MascotaResponse mascota) {
        return EntityModel.of(mascota,
                linkTo(methodOn(MascotaController.class).obtener(mascota.id())).withSelfRel(),
                linkTo(methodOn(MascotaController.class).listar()).withRel("mascotas"));
    }

    public CollectionModel<EntityModel<MascotaResponse>> toCollection(List<MascotaResponse> mascotas) {
        List<EntityModel<MascotaResponse>> items = mascotas.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(MascotaController.class).listar()).withSelfRel());
    }
}
