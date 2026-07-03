package com.vet.animalink.historico_service.assembler;

import com.vet.animalink.historico_service.controller.HistoricoController;
import com.vet.animalink.historico_service.dto.HistoricoResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HistoricoModelAssembler implements RepresentationModelAssembler<HistoricoResponse, EntityModel<HistoricoResponse>> {

    @Override
    public EntityModel<HistoricoResponse> toModel(HistoricoResponse historico) {
        return EntityModel.of(historico,
                linkTo(methodOn(HistoricoController.class).obtener(historico.id())).withSelfRel(),
                linkTo(methodOn(HistoricoController.class).listar()).withRel("historicos"));
    }

    public CollectionModel<EntityModel<HistoricoResponse>> toCollection(List<HistoricoResponse> historicos) {
        List<EntityModel<HistoricoResponse>> items = historicos.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(HistoricoController.class).listar()).withSelfRel());
    }
}