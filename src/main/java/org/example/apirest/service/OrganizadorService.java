package org.example.apirest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apirest.entity.Organizador;
import org.example.apirest.exception.DuplicateResourceException;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.repository.OrganizadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;

    @Transactional(readOnly = true)
    public List<Organizador> findAll() {
        log.debug("Buscando todos los organizadores");
        return organizadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Organizador> findAll(Pageable pageable) {
        log.debug("Buscando organizadores con paginación: {}", pageable);
        return organizadorRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Organizador findById(Long id) {
        log.debug("Buscando organizador con id: {}", id);
        return organizadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organizador", "id", id));
    }

    @Transactional
    public Organizador save(Organizador organizador) {
        log.debug("Guardando nuevo organizador: {}", organizador.getEmail());
        validateEmailNotExists(organizador.getEmail(), null);
        return organizadorRepository.save(organizador);
    }

    @Transactional
    public Organizador update(Long id, Organizador organizador) {
        log.debug("Actualizando organizador con id: {}", id);
        Organizador existingOrganizador = findById(id);
        validateEmailNotExists(organizador.getEmail(), id);

        existingOrganizador.setNombre(organizador.getNombre());
        existingOrganizador.setEmail(organizador.getEmail());
        existingOrganizador.setTelefono(organizador.getTelefono());

        return organizadorRepository.save(existingOrganizador);
    }

    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando organizador con id: {}", id);
        if (!organizadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organizador", "id", id);
        }
        organizadorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Organizador> searchByNombre(String nombre) {
        log.debug("Buscando organizadores por nombre: {}", nombre);
        return organizadorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    private void validateEmailNotExists(String email, Long excludeId) {
        organizadorRepository.findByEmail(email).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new DuplicateResourceException("Organizador", "email", email);
            }
        });
    }
}
