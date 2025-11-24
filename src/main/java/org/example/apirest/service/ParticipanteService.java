package org.example.apirest.service;

import jakarta.transaction.Transactional;
import org.example.apirest.entity.Participante;
import org.example.apirest.repository.EventoRepository;
import org.example.apirest.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private EventoRepository eventoRepository;

    public List<Participante> findAll() {
        return participanteRepository.findAll();
    }

    public Optional<Participante> findById(Long id) {
        return participanteRepository.findById(id);
    }

    public Optional<Participante> save(Long eventoId, Participante participanteDetails) {
        return eventoRepository.findById(eventoId).map(evento -> {
            participanteDetails.setEvento(evento);
            return participanteRepository.save(participanteDetails);
        });
    }

    public Optional<Participante> update(Long id, Participante participanteDetails) {
        return participanteRepository.findById(id).map(participante -> {
            participante.setNombre(participanteDetails.getNombre());
            participante.setEmail(participanteDetails.getEmail());
            participante.setTelefono(participanteDetails.getTelefono());
            return participanteRepository.save(participante);
        });
    }

    public boolean delete(Long id) {
        return participanteRepository.findById(id).map(participante -> {
            participanteRepository.delete(participante);
            return true;
        }).orElse(false);
    }

    public List<Participante> findByEmail(String email) {
        return participanteRepository.findByEmailContainingIgnoreCase(email);
    }

    public List<Participante> findByEventoId(Long eventoId) {
        return participanteRepository.findByEventoId(eventoId);
    };

    public boolean emailExists(String email) {
        return participanteRepository.existsByEmail(email);
    }

}

