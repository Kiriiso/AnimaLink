package com.vet.animalink.usuario_service.service;

import com.vet.animalink.usuario_service.exception.ResourceNotFoundException;
import com.vet.animalink.usuario_service.model.Usuario;
import com.vet.animalink.usuario_service.model.enums.Rol;
import com.vet.animalink.usuario_service.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }

    @Transactional
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new IllegalArgumentException("Ya existe un usuario con el RUT " + usuario.getRut());
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario datos) {
        Usuario existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setRol(datos.getRol());
        existente.setEspecialidad(datos.getEspecialidad());
        return usuarioRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario existente = obtenerPorId(id);
        usuarioRepository.delete(existente);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public List<Usuario> buscarPorNombre(String texto) {
        return usuarioRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(texto, texto);
    }
}
