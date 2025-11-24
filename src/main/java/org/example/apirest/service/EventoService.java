package org.example.apirest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apirest.entity.Evento;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.repository.EventoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    @Transactional(readOnly = true)
    public List<Evento> findAll() {
        log.debug("Buscando todos los eventos");
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Evento> findAll(Pageable pageable) {
        log.debug("Buscando eventos con paginación: {}", pageable);
        return eventoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Evento findById(Long id) {
        log.debug("Buscando evento con id: {}", id);
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", "id", id));
    }

    @Transactional
    public Evento save(Evento evento) {
        log.debug("Guardando nuevo evento: {}", evento.getTitulo());
        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento update(Long id, Evento evento) {
        log.debug("Actualizando evento con id: {}", id);
        Evento existingEvento = findById(id);

        existingEvento.setTitulo(evento.getTitulo());
        existingEvento.setDescripcion(evento.getDescripcion());
        existingEvento.setFecha(evento.getFecha());
        existingEvento.setUbicacion(evento.getUbicacion());
        existingEvento.setOrganizador(evento.getOrganizador());

        return eventoRepository.save(existingEvento);
    }

    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando evento con id: {}", id);
        if (!eventoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evento", "id", id);
        }
        eventoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Evento> searchByTitulo(String titulo) {
        log.debug("Buscando eventos por título: {}", titulo);
        return eventoRepository.findByTituloContainingIgnoreCase(titulo);
    }
}
