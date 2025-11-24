package org.example.apirest.repository;

import org.example.apirest.entity.Participante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    List<Participante> findByEmailContainingIgnoreCase(String email);
    List<Participante> findByEventoId(Long eventoId);
    Page<Participante> findByEventoId(Long eventoId, Pageable pageable);
    Optional<Participante> findByEmailAndEventoId(String email, Long eventoId);
}
