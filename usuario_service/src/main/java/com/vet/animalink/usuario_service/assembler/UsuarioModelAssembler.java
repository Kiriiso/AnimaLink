package com.vet.animalink.usuario_service.assembler;

import com.vet.animalink.usuario_service.controller.UsuarioController;
import com.vet.animalink.usuario_service.dto.UsuarioResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponse, EntityModel<UsuarioResponse>> {

    @Override
    public EntityModel<UsuarioResponse> toModel(UsuarioResponse usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtener(usuario.id())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
    }

    public CollectionModel<EntityModel<UsuarioResponse>> toCollection(List<UsuarioResponse> usuarios) {
        List<EntityModel<UsuarioResponse>> items = usuarios.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
    }
}
