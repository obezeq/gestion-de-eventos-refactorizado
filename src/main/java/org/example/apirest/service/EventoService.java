package org.example.apirest.service;

import org.example.apirest.entity.Evento;
import org.example.apirest.repository.EventoRepository;
import org.example.apirest.repository.OrganizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    OrganizadorRepository organizadorRepository;

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> findById(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento save(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Optional<Evento> update(Long id, Evento eventoDetails) {
        return eventoRepository.findById(id)
                .map(evento -> {
                    evento.setTitulo(eventoDetails.getTitulo());
                    evento.setDescripcion(eventoDetails.getDescripcion());
                    evento.setFecha(eventoDetails.getFecha());
                    evento.setUbicacion(eventoDetails.getUbicacion());
                    if (eventoDetails.getOrganizador() != null) {
                        evento.setOrganizador(eventoDetails.getOrganizador());
                    }
                    return eventoRepository.save(evento);
                });
    }

    public boolean delete(Long id) {
        return eventoRepository.findById(id)
                .map(evento -> {
                    eventoRepository.delete(evento);
                    return true;
                })
                .orElse(false);
    }

    public List<Evento> findByTitulo(String titulo) {
        return eventoRepository.findByTituloContainingIgnoreCase(titulo);
    }

}
