package org.example.apirest.service;

import jakarta.transaction.Transactional;
import org.example.apirest.entity.Organizador;
import org.example.apirest.repository.OrganizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizadorService {

    @Autowired
    private OrganizadorRepository organizadorRepository;

    public List<Organizador> findAll() {
        return organizadorRepository.findAll();
    }

    public Optional<Organizador> findById(Long id) {
        return organizadorRepository.findById(id);
    }

    public Organizador save(Organizador organizador) {
        return organizadorRepository.save(organizador);
    }

    public Optional<Organizador> update(Long id, Organizador organizadorDetails) {
        return organizadorRepository.findById(id).map(organizador -> {
            organizador.setNombre(organizadorDetails.getNombre());
            organizador.setEmail(organizadorDetails.getEmail());
            organizador.setTelefono(organizadorDetails.getTelefono());
            return organizadorRepository.save(organizador);
        });
    }

    public boolean delete(Long id) {
        return organizadorRepository.findById(id).map(organizador -> {
            organizadorRepository.delete(organizador);
            return true;
        }).orElse(false);
    }

    public List<Organizador> findByNombre(String nombre) {
        return organizadorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public boolean emailExists(String email) {
        return organizadorRepository.existsByEmail(email);
    }
}
