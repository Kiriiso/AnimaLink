package com.vet.animalink.factura_service.assembler;

import com.vet.animalink.factura_service.controller.FacturaController;
import com.vet.animalink.factura_service.dto.FacturaResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FacturaModelAssembler implements RepresentationModelAssembler<FacturaResponse, EntityModel<FacturaResponse>> {

    @Override
    public EntityModel<FacturaResponse> toModel(FacturaResponse factura) {
        return EntityModel.of(factura,
                linkTo(methodOn(FacturaController.class).obtener(factura.id())).withSelfRel(),
                linkTo(methodOn(FacturaController.class).listar()).withRel("facturas"));
    }

    public CollectionModel<EntityModel<FacturaResponse>> toCollection(List<FacturaResponse> facturas) {
        List<EntityModel<FacturaResponse>> items = facturas.stream().map(this::toModel).toList();
        return CollectionModel.of(items,
                linkTo(methodOn(FacturaController.class).listar()).withSelfRel());
    }
}
