package org.example.apirest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apirest.entity.Participante;
import org.example.apirest.exception.DuplicateResourceException;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.repository.ParticipanteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    @Transactional(readOnly = true)
    public List<Participante> findAll() {
        log.debug("Buscando todos los participantes");
        return participanteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Participante> findAll(Pageable pageable) {
        log.debug("Buscando participantes con paginación: {}", pageable);
        return participanteRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Participante findById(Long id) {
        log.debug("Buscando participante con id: {}", id);
        return participanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participante", "id", id));
    }

    @Transactional
    public Participante save(Participante participante) {
        log.debug("Guardando nuevo participante: {}", participante.getEmail());
        validateEmailNotExistsInEvento(participante.getEmail(), participante.getEvento().getId(), null);
        return participanteRepository.save(participante);
    }

    @Transactional
    public Participante update(Long id, Participante participante) {
        log.debug("Actualizando participante con id: {}", id);
        Participante existingParticipante = findById(id);
        validateEmailNotExistsInEvento(participante.getEmail(), participante.getEvento().getId(), id);

        existingParticipante.setNombre(participante.getNombre());
        existingParticipante.setEmail(participante.getEmail());
        existingParticipante.setTelefono(participante.getTelefono());
        existingParticipante.setEvento(participante.getEvento());

        return participanteRepository.save(existingParticipante);
    }

    @Transactional
    public void deleteById(Long id) {
        log.debug("Eliminando participante con id: {}", id);
        if (!participanteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Participante", "id", id);
        }
        participanteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Participante> searchByEmail(String email) {
        log.debug("Buscando participantes por email: {}", email);
        return participanteRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public List<Participante> findByEventoId(Long eventoId) {
        log.debug("Buscando participantes del evento: {}", eventoId);
        return participanteRepository.findByEventoId(eventoId);
    }

    @Transactional(readOnly = true)
    public Page<Participante> findByEventoId(Long eventoId, Pageable pageable) {
        log.debug("Buscando participantes del evento {} con paginación", eventoId);
        return participanteRepository.findByEventoId(eventoId, pageable);
    }

    private void validateEmailNotExistsInEvento(String email, Long eventoId, Long excludeParticipanteId) {
        participanteRepository.findByEmailAndEventoId(email, eventoId).ifPresent(existing -> {
            if (excludeParticipanteId == null || !existing.getId().equals(excludeParticipanteId)) {
                throw new DuplicateResourceException("Participante", "email", email);
            }
        });
    }
}
