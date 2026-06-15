package com.vet.pets_service.controller;

import com.vet.pets_service.model.Mascota;
import com.vet.pets_service.service.MascotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class MascotaController {

    private final MascotaService service;

    public MascotaController(MascotaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Mascota>> all() {
        return ResponseEntity.ok(service.findByClienteId(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> get(@PathVariable Long id) {
        Mascota m = service.findById(id);
        if (m == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(m);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Mascota>> byCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.findByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<Mascota> create(@RequestBody Mascota mascota) {
        Mascota saved = service.save(mascota);
        return ResponseEntity.created(URI.create("/pets/" + saved.getId())).body(saved);
    }

}
